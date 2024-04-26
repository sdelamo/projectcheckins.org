plugins {
    id("org.projectcheckins.micronaut-modules-conventions")
}
dependencies {
    implementation("io.micronaut.views:micronaut-views-fieldset:5.3.0")
    testImplementation(project(":test-utils"))
}