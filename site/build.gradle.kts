plugins {
    java
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.github.gmazzo.buildconfig")
    application
}

group = "com.caseyjbrooks.arkham.site"
version = "1.0.0"

repositories {
//    mavenLocal()
    mavenCentral()
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation("io.github.copper-leaf:ballast-core:2.0.0-SNAPSHOT")
    implementation("io.github.copper-leaf:json-forms-core:0.4.0")

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.serialization.json)
    implementation(libs.ktor.client.cio)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.multiplatformSettings.core)
    implementation(libs.multiplatformSettings.noArg)

    implementation("org.apache.xmlgraphics:batik:1.14")
    implementation("org.apache.xmlgraphics:batik-transcoder:1.14")
    implementation("org.apache.xmlgraphics:batik-codec:1.14")
    implementation("com.twelvemonkeys.imageio:imageio-batik:3.8.2")
    implementation("com.twelvemonkeys.imageio:imageio-jpeg:3.8.2")
    implementation("org.jsoup:jsoup:1.15.2")
}

application {
    mainClass.set("com.caseyjbrooks.arkham.MainKt")
}

buildConfig {
    packageName(project.group.toString())

    if (project.hasProperty("release")) {
        buildConfigField("Boolean", "DEBUG", "false")
        buildConfigField("String", "BASE_URL", "\"https://cjbrooks12.github.io/arkham-explorer/\"")
        buildConfigField("String?", "BASE_PATH", "\"/arkham-explorer\"")
    } else {
        buildConfigField("Boolean", "DEBUG", "true")
        buildConfigField("String", "BASE_URL", "\"http://localhost:8080/\"")
        buildConfigField("String?", "BASE_PATH", "null")
    }
}
