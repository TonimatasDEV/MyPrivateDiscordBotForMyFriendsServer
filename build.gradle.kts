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
    implementation("net.dv8tion:JDA:5.3.0")
    implementation("com.openai:openai-java:0.11.3")
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