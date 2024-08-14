plugins {
    kotlin("jvm") version "1.9.23"
}

group = "org.hnh.mv"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    implementation("com.varabyte.kotter:kotter-jvm:1.1.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}