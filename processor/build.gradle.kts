plugins {
    id("org.projectcheckins.micronaut-boms")
}
dependencies {
    implementation("io.micronaut:micronaut-aop")
    implementation("io.micronaut:micronaut-core-processor")

    implementation(project(":annotations"))
    implementation("io.swagger.core.v3:swagger-annotations")
    implementation("io.micronaut.views:micronaut-views-core")
    implementation("io.micronaut.security:micronaut-security-annotations")

    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("ch.qos.logback:logback-classic")

    // AssertJ
    testImplementation("org.assertj:assertj-core")
}