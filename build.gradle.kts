plugins {
    kotlin("jvm") version "2.1.21"
    id("com.gradleup.shadow") version "9.2.2"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    // id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
}

group = "net.astrorbits"
version = "1.21.10-1.0.3"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
    compileOnly(files("libs/paper-1.21.10.jar"))
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.21.10")
    }
}

tasks.shadowJar {
    archiveClassifier.set("")
    relocate("kotlin", "net.astrorbits.dontdoit.shadow.kotlin")
    relocate("org.intellij.lang.annotations", "net.astrorbits.dontdoit.shadow.org.intellij.lang.annotations")
    relocate("org.jetbrains.annotations", "net.astrorbits.dontdoit.shadow.org.jetbrains.annotations")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}
