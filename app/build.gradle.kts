plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
    id("com.github.gmazzo.buildconfig")
}

group = "com.caseyjbrooks.arkham.app"
version = "1.0.0"

repositories {
//    mavenLocal()
    mavenCentral()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
}

kotlin {
    // targets
    js(IR) {
        browser {
            testTask {
                enabled = false
            }
        }
        binaries.executable()
    }

    // sourcesets
    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.time.ExperimentalTime")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }

        // Common Sourcesets
        val commonMain by getting {
            dependencies {
                implementation("io.github.copper-leaf:ballast-core:2.0.0-SNAPSHOT")
                implementation("io.github.copper-leaf:ballast-repository:2.0.0-SNAPSHOT")
                implementation("io.github.copper-leaf:ballast-saved-state:2.0.0-SNAPSHOT")
                implementation("io.github.copper-leaf:ballast-navigation:2.0.0-SNAPSHOT")

                implementation("io.github.copper-leaf:trellis-core:3.0.0")
                implementation("io.github.copper-leaf:trellis-dsl:3.0.0")
//                implementation("io.github.copper-leaf:thistle-core:3.1.0")

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.client.serialization.json)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.multiplatformSettings.core)
                implementation(libs.multiplatformSettings.noArg)
            }
        }

        val jsMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)
                implementation(compose.web.svg)

                implementation(libs.ktor.client.js)
            }
        }
    }
}

buildConfig {
    packageName(project.group.toString())

    if(project.hasProperty("release")) {
        buildConfigField("String", "BASE_URL", "\"https://cjbrooks12.github.io/arkham-explorer/\"")
    } else {
        buildConfigField("String", "BASE_URL", "\"http://localhost:8080/\"")
    }
}
