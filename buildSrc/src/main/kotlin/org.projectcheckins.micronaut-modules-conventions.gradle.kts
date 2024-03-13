plugins {
    id("org.projectcheckins.micronaut-boms")
}

dependencies {
    annotationProcessor("io.micronaut:micronaut-inject-java")
    testAnnotationProcessor("io.micronaut:micronaut-inject-java")

    // Micronaut Serialization
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    implementation("io.micronaut.serde:micronaut-serde-jackson")

    // Validation
    annotationProcessor("io.micronaut.validation:micronaut-validation-processor")
    implementation("io.micronaut.validation:micronaut-validation")
    testAnnotationProcessor("io.micronaut.validation:micronaut-validation-processor")

    // HTTP Client
    testImplementation("io.micronaut:micronaut-http-client-jdk")

    // JUnit Testing
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // AssertJ
    testImplementation("org.assertj:assertj-core")

    // Async testing
    testImplementation("org.awaitility:awaitility:${project.properties["awaitilityVersion"]}")

    // Logging
    testRuntimeOnly("ch.qos.logback:logback-classic")

    // For Warnings treated as errors
    compileOnly("com.fasterxml.jackson.core:jackson-databind")
    testCompileOnly("com.fasterxml.jackson.core:jackson-databind")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
