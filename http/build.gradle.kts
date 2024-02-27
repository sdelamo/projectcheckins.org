plugins {
    id("org.projectcheckins.micronaut-modules-conventions")
}
dependencies {
    annotationProcessor(project(":processor"))
    testAnnotationProcessor(project(":processor"))
    implementation(project(":annotations"))

    annotationProcessor("io.micronaut:micronaut-http-validation")
    implementation("io.micronaut:micronaut-http-server")
    api(project(":core"))

    // OpenAPI https://micronaut-projects.github.io/micronaut-openapi/latest/guide/
    annotationProcessor("io.micronaut.openapi:micronaut-openapi")
    compileOnly("io.micronaut.openapi:micronaut-openapi-annotations")

    // Views
    implementation("io.micronaut.views:micronaut-views-core")
    runtimeOnly("io.micronaut.views:micronaut-views-thymeleaf")

    // Test Server
    testImplementation("io.micronaut:micronaut-http-server-netty")
}