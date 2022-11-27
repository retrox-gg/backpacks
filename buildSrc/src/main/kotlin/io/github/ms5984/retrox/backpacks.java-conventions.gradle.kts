plugins {
    java
}

repositories {
    mavenCentral()
    // papermc repo
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    // sonatype snapshots (s01)
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
}

val accessoriesVersion by extra("0.3.0-SNAPSHOT")
val tagLibVersion by extra("0.0.2")

dependencies {
    implementation("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    implementation("io.github.ms5984.libraries:tag-lib:$tagLibVersion")
    implementation("io.github.ms5984.retrox:accessories-api:$accessoriesVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).addBooleanOption("Xdoclint:reference", true)
    options.quiet()
}
