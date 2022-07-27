plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    implementation("org.jetbrains.compose:compose-gradle-plugin:1.2.0-alpha01-dev745")
    implementation("org.jetbrains.kotlin:kotlin-serialization:1.7.10")
    implementation("com.github.gmazzo:gradle-buildconfig-plugin:3.0.3")
}
