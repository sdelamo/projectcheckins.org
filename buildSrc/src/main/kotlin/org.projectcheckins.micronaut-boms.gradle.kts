plugins {
    id("org.projectcheckins.java-common-conventions")
}

dependencies {
    annotationProcessor(platform("io.micronaut.platform:micronaut-platform:${project.properties["micronautVersion"]}"))
    implementation(platform("io.micronaut.platform:micronaut-platform:${project.properties["micronautVersion"]}"))
    testAnnotationProcessor(platform("io.micronaut.platform:micronaut-platform:${project.properties["micronautVersion"]}"))
    testImplementation(platform("io.micronaut.platform:micronaut-platform:${project.properties["micronautVersion"]}"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}