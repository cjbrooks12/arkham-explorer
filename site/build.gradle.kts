plugins {
    kotlin("jvm")
    id("com.github.gmazzo.buildconfig")
}

repositories {
    mavenLocal()
    mavenCentral()
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
