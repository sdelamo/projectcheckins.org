plugins {
    id("org.projectcheckins.micronaut-modules-conventions")
}
dependencies {
    // Email
    api("io.micronaut.email:micronaut-email-template")
    api("io.micronaut.security:micronaut-security-jwt")
    implementation("io.micronaut.reactor:micronaut-reactor")
    compileOnly("com.fasterxml.jackson.core:jackson-databind")
    testCompileOnly("com.fasterxml.jackson.core:jackson-databind")
}