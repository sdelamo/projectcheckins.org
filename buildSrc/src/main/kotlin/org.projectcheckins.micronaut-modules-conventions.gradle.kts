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

    // HTTP Client
    testImplementation("io.micronaut:micronaut-http-client-jdk")

    // JUnit Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // AssertJ
    testImplementation("org.assertj:assertj-core:3.25.1")

    // Logging
    testRuntimeOnly("ch.qos.logback:logback-classic")
}

tasks.withType<Test> {
    useJUnitPlatform()
}