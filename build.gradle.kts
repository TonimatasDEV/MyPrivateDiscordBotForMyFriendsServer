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

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
    maven("https://maven.lavalink.dev/releases")
}

dependencies {
    // https://github.com/discord-jda/JDA/releases
    implementation("net.dv8tion:JDA:6.3.1")
    // https://github.com/qos-ch/logback/releases
    implementation("ch.qos.logback:logback-classic:1.5.32")
    // https://github.com/google/gson/releases
    implementation("com.google.code.gson:gson:2.13.2")
    // https://github.com/Revxrsal/Lamp/releases
    implementation("io.github.revxrsal:lamp.common:4.0.0-rc.14")
    implementation("io.github.revxrsal:lamp.jda:4.0.0-rc.14")
    // https://github.com/lavalink-devs/lavaplayer/releases
    implementation("dev.arbjerg:lavaplayer:2.2.6")
    // https://github.com/lavalink-devs/youtube-source/releases
    implementation("dev.lavalink.youtube:youtube-plugin:1.17.0")
    // https://github.com/MinnDevelopment/jdave/releases
    implementation("club.minnced:jdave-api:0.1.6")
    implementation("club.minnced:jdave-native-linux-x86-64:0.1.6")
    implementation("club.minnced:jdave-native-linux-aarch64:0.1.6")
    implementation("club.minnced:jdave-native-win-x86-64:0.1.6")
    implementation("club.minnced:jdave-native-darwin:0.1.6")
}

application {
    mainClass = "dev.tonimatas.Main"
}

tasks.run {
    val path = rootDir.toPath().resolve("run")
    workingDir = path.toFile()
    if (!path.exists()) path.createDirectory()
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.jar {
    dependsOn("shadowJar")
    archiveClassifier.set("plain")
}

tasks.shadowJar {
    archiveClassifier.set("")

    manifest {
        attributes("Main-Class" to "dev.tonimatas.Main")
    }
}
