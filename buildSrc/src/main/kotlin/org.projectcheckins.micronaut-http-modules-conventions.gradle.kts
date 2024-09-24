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
    implementation("io.micronaut.views:micronaut-views-fieldset")
    implementation("io.micronaut.views:micronaut-views-core")
    runtimeOnly("io.micronaut.views:micronaut-views-thymeleaf")

    // Test Server
    testImplementation("io.micronaut:micronaut-http-server-netty")
}