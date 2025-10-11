plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
//    kotlin("plugin.serialization") version "2.2.0"
}

group = "com.samuelsumbane.ssptdesktop"
version = "1.0.0"
application {
    mainClass.set("com.samuelsumbane.ssptdesktop.ApplicationKt")
    
//    val isDevelopment: Boolean = project.ext.has("development")
    val isDevelopment: Boolean = true
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)

    implementation("io.insert-koin:koin-ktor:3.5.6")  // For Ktor

    //CORS
    implementation("io.ktor:ktor-server-cors")
    //
    implementation("io.ktor:ktor-server-auth:2.3.0")
    implementation("io.ktor:ktor-server-auth-jwt:2.3.0")

    implementation("org.jetbrains.exposed:exposed-core:0.43.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.43.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.43.0")

//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:3.3.0")
//    implementation("io.ktor:ktor-client-content-negotiation:3.3.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.3.0")
    implementation("io.ktor:ktor-server-content-negotiation:3.3.0")

    // BCrypt - password hash --------->>
    implementation("org.mindrot:jbcrypt:0.4")

    // For excel ------>>
    implementation("org.apache.poi:poi-ooxml:5.2.5")

    //
    implementation("mysql:mysql-connector-java:8.0.33")

    // For Email ------>>
//    implementation("com.sun.mail:jakarta.mail:2.0.2")
//    implementation("com.sun.mail:javax.mail:1.6.2")

    implementation("jakarta.mail:jakarta.mail-api:2.1.3") // API
    implementation("org.eclipse.angus:jakarta.mail:2.0.3") // Implementação

    // For Caching ------->>
    implementation("io.ktor:ktor-server-caching-headers:2.3.0")
}