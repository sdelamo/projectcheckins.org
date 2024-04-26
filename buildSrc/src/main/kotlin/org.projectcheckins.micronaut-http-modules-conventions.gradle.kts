plugins {
    id("org.projectcheckins.micronaut-modules-conventions")
}
dependencies {
    annotationProcessor(project(":processor"))
    testAnnotationProcessor(project(":processor"))
    api(project(":annotations"))

    // HTTP Server
    annotationProcessor("io.micronaut:micronaut-http-validation")
    implementation("io.micronaut:micronaut-http-server")

    // Views
    implementation("io.micronaut.views:micronaut-views-fieldset:5.3.0")
    implementation("io.micronaut.views:micronaut-views-core:5.3.0")
    runtimeOnly("io.micronaut.views:micronaut-views-thymeleaf:5.3.0")

    // Test Server
    testImplementation("io.micronaut:micronaut-http-server-netty")
}