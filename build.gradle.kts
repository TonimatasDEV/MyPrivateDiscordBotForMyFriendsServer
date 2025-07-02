import kotlin.io.path.createDirectory
import kotlin.io.path.exists

plugins {
    java
    application
    id("com.gradleup.shadow") version "9.0.0-beta17"
}

val projectVersion: String by extra

group = "dev.tonimatas"
version = projectVersion

repositories {
    mavenCentral()
    maven("https://maven.tonimatas.dev/releases")
}

dependencies {
    // https://github.com/discord-jda/JDA/releases
    implementation("net.dv8tion:JDA:5.6.1") {
        exclude(module = "opus-java")
    }

    // https://github.com/qos-ch/logback/releases
    implementation("ch.qos.logback:logback-classic:1.5.18")
    // https://github.com/google/gson/releases
    implementation("com.google.code.gson:gson:2.13.1")
    implementation("dev.tonimatas:CJDA:1.0.3")
}

application {
    mainClass = "dev.tonimatas.Main"
}

tasks.named<JavaExec>("run") {
    val path = rootDir.toPath().resolve("run")
    workingDir = path.toFile()
    if (!path.exists()) path.createDirectory()
}

tasks.compileJava {
    options.encoding = "UTF-8"
    java.sourceCompatibility = JavaVersion.VERSION_21
    java.targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    dependsOn("shadowJar")
    archiveClassifier.set("plain")
}

tasks.shadowJar {
    archiveClassifier.set("")

    minimize {
        exclude(dependency("ch.qos.logback:logback-classic:.*"))
    }

    manifest {
        attributes("Main-Class" to "dev.tonimatas.Main")
    }
}
