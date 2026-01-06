import kotlin.io.path.createDirectory
import kotlin.io.path.exists

plugins {
    java
    application
    id("com.gradleup.shadow") version "9.3.1"
}

val projectVersion: String by extra

group = "dev.tonimatas"
version = projectVersion

repositories {
    mavenCentral()
    maven("https://maven.lavalink.dev/releases")
    maven("https://maven.tonimatas.dev/releases")
}

dependencies {
    // https://github.com/discord-jda/JDA/releases
    implementation("net.dv8tion:JDA:6.2.1")
    // https://github.com/lavalink-devs/lavaplayer/releases
    implementation("dev.arbjerg:lavaplayer:2.2.6")
    // https://github.com/lavalink-devs/youtube-source/releases
    implementation("dev.lavalink.youtube:youtube-plugin:1.16.0")
    // https://github.com/qos-ch/logback/releases
    implementation("ch.qos.logback:logback-classic:1.5.23")
    // https://github.com/google/gson/releases
    implementation("com.google.code.gson:gson:2.13.2")
    // https://github.com/TonimatasDEV/CJDA/releases
    implementation("dev.tonimatas:CJDA:1.0.4")
}

application {
    mainClass = "dev.tonimatas.Main"
}

tasks.run.configure {
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
