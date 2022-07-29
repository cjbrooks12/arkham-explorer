plugins {
    java
    kotlin("jvm")
    id("com.github.gmazzo.buildconfig")
    application
}

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
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.multiplatformSettings.core)
    implementation(libs.multiplatformSettings.noArg)
}

application {
    mainClass.set("com.caseyjbrooks.arkham.MainKt")
}
