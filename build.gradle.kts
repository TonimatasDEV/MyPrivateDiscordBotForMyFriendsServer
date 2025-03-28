plugins {
    java
    id("com.gradleup.shadow") version "9.0.0-beta11"
}

val jdaVersion: String by extra
val logbackVersion: String by extra
val projectVersion: String by extra

group = "dev.tonimatas"
version = projectVersion

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
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