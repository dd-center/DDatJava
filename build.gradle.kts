buildscript {
    repositories {
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
    }
}

plugins {
    id("java")
    `maven-publish`
}

group = "moe.vtbs"
version = "1.0.0"

repositories {
    maven { url = uri("https://maven.aliyun.com/repository/central") }
    maven { url = uri("https://maven.aliyun.com/repository/public") }
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("org.slf4j:slf4j-log4j12:1.7.36")
    implementation("com.typesafe:config:1.4.2")
    implementation("org.java-websocket:Java-WebSocket:1.5.3")
    implementation("com.google.http-client:google-http-client:1.42.0")
    implementation("commons-io:commons-io:2.11.0")
}

tasks.jar {
    archiveFileName.set("${project.name}-${project.version}-api.jar")
    finalizedBy(tasks["fatJar"])
}

task<Jar>("fatJar") {
    manifest {
        from("src/main/resources/META-INF/MANIFEST.MF")
    }
    duplicatesStrategy = DuplicatesStrategy.WARN
    archiveFileName.set("${project.name}-${project.version}.jar")
    from(zipTree(tasks.jar.get().archiveFile.get().asFile))
    from(configurations.runtimeClasspath.get().files.map { if (it.isDirectory) it else zipTree(it) })
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
            from(components["java"])
            artifact(tasks["sourcesJar"])
        }
    }
}