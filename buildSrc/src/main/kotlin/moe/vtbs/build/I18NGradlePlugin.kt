/*
 * Copyright (C) 2022 一七年夏
 * 
 * The part of program is free: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published 
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/
 */
package moe.vtbs.build

import moe.vtbs.lang.config.PConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Zip
import org.gradle.jvm.tasks.Jar
import java.net.URLClassLoader
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/**
 *  I18N Gradle 插件
 *
 * @author 一七年夏
 * @since 2022-07-05 13:37
 */
class I18NGradlePlugin : Plugin<Project> {
    lateinit var project: Project
    override fun apply(target: Project) {
        this.project = target
        addGenI18nFileTree()
        addI18nJarTask()
    }

    private val jarFile get() = (project.tasks.getByName("jar") as Jar).archiveFile.get().asFile
    private val tempDir get() = project.buildDir.resolve("tmp/i18n/")
    private val tempJarFile get() = tempDir.resolve(jarFile.name)
    private val targetDir get() = project.buildDir.resolve("generated/i18n")
    private val targetLangDir get() = targetDir.resolve("lang")
    private val tempI18nWordFile get() = targetLangDir.resolve("zh-cn.ini")

    private fun addGenI18nFileTree() {
        project.task("genI18NConfigFile") {
            it.dependsOn("jar")
            it.doLast { _ ->
                if (!tempDir.exists()) require(tempDir.mkdirs()) { "建立构建文件夹失败" }
                project.copy {
                    it.from(jarFile)
                    it.into(tempDir)
                }
                val loader = URLClassLoader(
                    arrayOf(tempJarFile.toURI().toURL()),
                    Thread.currentThread().contextClassLoader
                )
                if (!targetLangDir.exists()) require(targetLangDir.mkdirs()) { "未能创建${targetDir}目录" }
                if (tempI18nWordFile.exists()) tempI18nWordFile.delete()
                tempI18nWordFile.printWriter().use { pw ->
                    pw.write(
                        """
                        # Language configuration file, please use "⮰" instead of carriage return.
                        # Between every two "%" is a variable.
                        # This is the language profile for Simplified Chinese.
                    """.trimIndent() + "\n"
                    )
                    fun List<Pair<String, String>>.write() {
                        forEach { (k, v) ->
                            pw.write("$k=$v\n")
                        }
                    }
                    getI18nNameTree("i18n", "moe.vtbs.i18n.I18N", loader).write()
                }
            }
        }
    }

    private fun getI18nNameTree(
        parentNode: String,
        clazzName: String,
        loader: URLClassLoader
    ): List<Pair<String, String>> {
        val out = mutableListOf<Pair<String, String>>()
        val clazz = loader.loadClass(clazzName).kotlin
        val enter = "⮰"
        fun String.singleLine() = this.replace("\"", "\"\"").replace("\n", enter)
        val instance = try {
            clazz.java.getConstructor(PConfig::class.java).newInstance(null) as PConfig
        } catch (e: Throwable) {
            System.err.println("${e.javaClass.name}: ${e.message}")
            return emptyList()
        }
        clazz.memberProperties.filter { it.visibility == KVisibility.PUBLIC }.forEach { prop ->
            when (prop.name) {
                "parent",
                "parnetProperty",
                "default",
                "notnull" -> return@forEach
            }
            val pos = "$parentNode.${prop.name}"
            if (
                prop.returnType.isSubtypeOf(PConfig::class.createType(nullable = true)) &&
                prop.returnType.classifier != clazz &&
                prop.returnType.classifier != PConfig::class
            ) {
                out.addAll(
                    getI18nNameTree(
                        pos,
                        (prop.returnType.classifier as KClass<*>).java.name,
                        loader
                    )
                )
            } else if (prop.returnType.isSubtypeOf(String::class.createType(nullable = true))) {
                val defaultValue = try {
                    instance.let { obj ->
                        prop.isAccessible = true
                        @Suppress("UNCHECKED_CAST")
                        (prop as KProperty1<PConfig, Any?>).getDelegate(obj)?.let { delegate ->
                            when (delegate) {
                                is PConfig.NotNullValue<*> -> delegate.callback().toString().singleLine()
                                is PConfig.DefaultValue<*> -> delegate.callback().toString().singleLine()
                                else -> return@forEach
                            }
                        } ?: return@forEach
                    }
                } catch (e: Throwable) {
                    System.err.println("${e.javaClass.name}: ${e.message}")
                    return@forEach
                }
                out.add(Pair(pos, defaultValue))
            }
        }
        return out
    }

    fun addI18nJarTask() {
        val jar = project.tasks.getByName("jar") as Jar
        project.tasks.create("i18nJar", Zip::class.java) {
            jar.finalizedBy(it)
            it.dependsOn("genI18NConfigFile")
            project.afterEvaluate { _ ->
                it.from(project.zipTree(tempJarFile))
                it.from(targetDir)
                it.archiveFileName.set(tempJarFile.name)
                it.destinationDirectory.set(jar.destinationDirectory.get().asFile)
            }
        }
    }
}