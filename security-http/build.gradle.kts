plugins {
    id("org.projectcheckins.micronaut-http-modules-conventions")
}
dependencies {
    api(project(":security"))
    api(project(":bootstrap"))
    compileOnly("io.micronaut.security:micronaut-security-session")
    testImplementation(project(":thymeleaf-fieldset"))
    testImplementation("io.micronaut.security:micronaut-security-session")
    testImplementation("io.micronaut.security:micronaut-security-jwt")
    compileOnly("com.fasterxml.jackson.core:jackson-databind")
    testCompileOnly("com.fasterxml.jackson.core:jackson-databind")
}