plugins {
    java
    id("com.gradleup.shadow") version "9.0.0-beta13"
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
    implementation("net.dv8tion:JDA:$jdaVersion") {
        exclude(module = "opus-java")
    }

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
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