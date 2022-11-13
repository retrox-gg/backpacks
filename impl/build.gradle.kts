@file:Suppress("UnstableApiUsage")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("backpacks-conventions")
    kotlin("jvm") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

val kotlinVersion by extra(kotlin.coreLibrariesVersion)

dependencies {
    api(project(":api"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("cloud.commandframework", "cloud-paper", "1.7.1")
    implementation("cloud.commandframework:cloud-annotations:1.7.1")
    annotationProcessor("cloud.commandframework:cloud-annotations:1.7.1")
}

description = "A backpack plugin for RetroX"

tasks.withType<ProcessResources> {
    expand(project.properties)
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("plugin")
    dependencies {
        include(project(":api"))
    }
}
