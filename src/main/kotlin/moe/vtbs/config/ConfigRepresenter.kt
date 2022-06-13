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
package moe.vtbs.config

import org.yaml.snakeyaml.comments.CommentLine
import org.yaml.snakeyaml.comments.CommentType
import org.yaml.snakeyaml.introspector.Property
import org.yaml.snakeyaml.nodes.MappingNode
import org.yaml.snakeyaml.nodes.Node
import org.yaml.snakeyaml.nodes.ScalarNode
import org.yaml.snakeyaml.representer.Representer
import kotlin.reflect.full.memberProperties

/**
 *  配置文件Representer
 *
 * @author 一七年夏
 * @since 2022-05-06 13:10
 */
class ConfigRepresenter : Representer() {

    init {
        propertyUtils.isSkipMissingProperties = true
    }

    //val metadatas = mutableMapOf<Class<*>, Map<String, Array<Annotation>>>()

    override fun represent(data: Any?): Node {
        val node = super.represent(data)
        return node
    }

    override fun representJavaBean(properties: MutableSet<Property>?, javaBean: Any?): MappingNode {
        val node = super.representJavaBean(properties, javaBean)
        if (javaBean == null) return node
        val br = javaBean::class.memberProperties
        val annotation = mutableMapOf<String, List<Annotation>>()
        br.forEach { bean ->
            annotation[bean.name] = bean.annotations
        }
        node.value.forEach { pair ->
            val comment = annotation[(pair.keyNode as ScalarNode).value]
                ?.find { it is Comment } as? Comment
                ?: return@forEach
            val string = "${comment.comment.trimIndent()}\n"
            val lineComments = string.split("\n").map { it.trimEnd() }
                .filter { it.isNotEmpty() }
                .map { CommentLine(null, null, it, CommentType.BLOCK) }

            pair.keyNode.blockComments?.plusAssign(lineComments) ?: run {
                pair.keyNode.blockComments = lineComments.toMutableList()
            }
        }
        return node
    }
}