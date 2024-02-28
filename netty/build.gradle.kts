plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.minimal.application") version "4.3.3"
}
version = "0.1"
group = "org.projectcheckins"
repositories {
        mavenCentral()
}

dependencies {
    implementation(project(":security-http"))
    implementation(project(":http"))
    implementation("io.micronaut.security:micronaut-security-session")
    runtimeOnly("ch.qos.logback:logback-classic")

    // HTTP Client
    testImplementation("io.micronaut:micronaut-http-client-jdk")

    // Persistence
    implementation(project(":repository-eclipsestore"))
}
application {
    mainClass.set("org.projectcheckins.netty.Application")
}
java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}
micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("org.projectcheckins.*")
    }
}
