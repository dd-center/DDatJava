plugins{
    kotlin("jvm") version "1.7.0"
}

repositories {
    maven("https://maven.aliyun.com/repository/central")
    mavenCentral()
}
dependencies {
    gradleApi()
    gradleKotlinDsl()
    compileOnly(kotlin("stdlib"))
}