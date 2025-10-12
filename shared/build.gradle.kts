
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    
}

kotlin {
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            implementation("io.ktor:ktor-client-content-negotiation:3.3.0")
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.3.0")

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

