plugins {
    id("org.projectcheckins.micronaut-http-modules-conventions")
}
dependencies {
    api(project(":core"))
    api(project(":bootstrap"))

    // OpenAPI https://micronaut-projects.github.io/micronaut-openapi/latest/guide/
    annotationProcessor("io.micronaut.openapi:micronaut-openapi")
    compileOnly("io.micronaut.openapi:micronaut-openapi-annotations")
    testImplementation(project(":thymeleaf-fieldset"))
    testImplementation(project(":test-utils"))
}