@file:Suppress("UnstableApiUsage")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("backpacks-conventions")
    kotlin("jvm") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
    api(project(":api"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.20")
    implementation("cloud.commandframework", "cloud-paper", "1.7.1")
    implementation("cloud.commandframework:cloud-annotations:1.7.1")
    annotationProcessor("cloud.commandframework:cloud-annotations:1.7.1")
}

description = "A backpack plugin for RetroX"

tasks.withType<ProcessResources> {
    expand(project.properties)
}

tasks.withType<ShadowJar> {
    dependencies {
        include(project(":api"))
    }
}
