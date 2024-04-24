plugins {
    id("org.projectcheckins.micronaut-modules-conventions")
}
dependencies {
    // Email
    api("io.micronaut.email:micronaut-email-template")
    api("io.micronaut.security:micronaut-security-jwt")
    implementation("io.micronaut.reactor:micronaut-reactor")
    testImplementation(project(":test-utils"))
}