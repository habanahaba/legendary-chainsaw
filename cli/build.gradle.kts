plugins {
    kotlin("jvm") version "1.9.23"
    application
}

group = "org.hnh.mv"

kotlin {
    jvmToolchain(17)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    implementation("com.varabyte.kotter:kotter-jvm:1.1.2")
    testImplementation(kotlin("test"))
}

application {
    mainClass = "org.hnh.mv.cli.CliKt"
}

tasks.test {
    useJUnitPlatform()
}