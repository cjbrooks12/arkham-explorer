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
    implementation(project(":site:shared"))

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
    implementation("com.twelvemonkeys.imageio:imageio-icns:3.8.2")
    implementation("com.twelvemonkeys.imageio:imageio-bmp:3.8.2")
    implementation("org.jsoup:jsoup:1.15.2")
    implementation("commons-codec:commons-codec:1.15")

    implementation("io.ktor:ktor-server-core:2.0.3")
    implementation("io.ktor:ktor-server-cio:2.0.3")
    implementation("io.ktor:ktor-server-call-logging:2.0.3")
    implementation("org.slf4j:slf4j-simple:1.7.36")
}

application {
    mainClass.set("com.caseyjbrooks.arkham.MainKt")
}

buildConfig {
    packageName(project.group.toString())

    buildConfigField("Int", "PORT", "8080")

    if (project.hasProperty("release")) {
        buildConfigField("Boolean", "DEBUG", "false")
        buildConfigField("String", "BASE_URL", "\"https://cjbrooks12.github.io/arkham-explorer\"")
        buildConfigField("String?", "BASE_PATH", "\"/arkham-explorer\"")
    } else {
        buildConfigField("Boolean", "DEBUG", "true")
        buildConfigField("String", "BASE_URL", "\"http://localhost:8080\"")
        buildConfigField("String?", "BASE_PATH", "\"/arkham-explorer\"")
    }
}
