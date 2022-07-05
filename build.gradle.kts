plugins {
    kotlin("jvm") version "1.7.0"
    `maven-publish`
    id("me.him188.kotlin-jvm-blocking-bridge") version "2.1.0-170.1"
}

apply(plugin = "moe.vtbs.build.i18n")

group = "moe.vtbs"
version = "2.1.0"

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testRuntimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.2")

    implementation("com.google.code.gson:gson:2.9.0")
    implementation("org.slf4j:slf4j-log4j12:1.7.36")
    implementation("commons-io:commons-io:2.11.0")

    implementation("org.yaml:snakeyaml:1.30")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    //compileOnly("androidx.annotation:annotation:1.4.0")

    //kotlin
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(kotlin("serialization"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
    compileOnly(fileTree(projectDir.resolve("lib/compileOnly")))
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn", "-Xjvm-default=all")
    }
    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
    useJUnitPlatform()
    workingDir = projectDir.resolve("run").apply {
        if (!exists()) mkdirs()
    }
}

tasks.jar {
    archiveFileName.set("${project.name}-${project.version}-api.jar")
}

task<Jar>("fatJar") {
    manifest {
        from("src/main/resources/META-INF/MANIFEST.MF")
    }
    duplicatesStrategy = DuplicatesStrategy.WARN
    archiveFileName.set("${project.name}-${project.version}.jar")
    from(zipTree(tasks.jar.get().archiveFile.get().asFile))
    from(configurations.runtimeClasspath.get().files.map { if (it.isDirectory) it else zipTree(it) })
    tasks["i18nJar"].finalizedBy(this)
}

task<Jar>("sourcesJar") {
    from(sourceSets["main"].allSource)
    archiveClassifier.set("sources")
}

publishing {
    repositories {
        maven("$projectDir/repo/")
    }

    publications {
        create<MavenPublication>("maven") {
            artifactId = "dd-home-api"
            from(components["java"])
            artifact(tasks["sourcesJar"])
        }
    }
}