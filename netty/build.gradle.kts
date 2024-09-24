plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.minimal.application") version "4.4.2"
}
version = "0.1"
group = "org.projectcheckins"
repositories {
        mavenCentral()
}

dependencies {
    developmentOnly(project(":devseeddata"))
    runtimeOnly(project(":assets"))
    implementation(project(":thymeleaf-fieldset"))
    implementation(project(":security-http"))
    implementation(project(":http"))
    implementation(project(":email-http"))
    testImplementation(project(":test-utils"))
    implementation("io.micronaut.security:micronaut-security-session")
    developmentOnly("io.micronaut.security:micronaut-security-jwt")
    runtimeOnly("ch.qos.logback:logback-classic")

    // JUnit Testing
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // HTTP Client
    testImplementation("io.micronaut:micronaut-http-client")

    // AssertJ
    testImplementation("org.assertj:assertj-core")

    // Persistence
    implementation(project(":repository-eclipsestore"))

    // Notifications
    implementation(project(":notification-logger"))

    // Eclipsestore S3
    runtimeOnly("software.amazon.awssdk:s3")
    runtimeOnly("io.micronaut.aws:micronaut-aws-sdk-v2")
    runtimeOnly("org.eclipse.store:afs-aws-s3")

    // Email
    implementation("io.micronaut.email:micronaut-email-javamail")
    runtimeOnly("org.eclipse.angus:angus-mail")
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
