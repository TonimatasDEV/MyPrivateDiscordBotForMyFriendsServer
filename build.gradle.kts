plugins {
    java
    id("com.gradleup.shadow") version "9.0.0-beta4"
}

group = "dev.tonimatas"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.3.1")
    implementation("ch.qos.logback:logback-classic:1.5.6")
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