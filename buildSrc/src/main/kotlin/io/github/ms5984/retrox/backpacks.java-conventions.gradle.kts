plugins {
    java
}

repositories {
    mavenCentral()
    // papermc repo
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

val accessoriesVersion by extra("0.2.1")

dependencies {
    implementation("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    implementation("io.github.ms5984.retrox:accessories:$accessoriesVersion")
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
