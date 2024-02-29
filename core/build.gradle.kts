plugins {
    id("org.projectcheckins.micronaut-modules-conventions")
}
dependencies {
    api(project(":multitenancy"))
    api("io.micronaut.security:micronaut-security")
    api("io.micronaut.views:micronaut-views-fieldset")
    compileOnly("com.github.ksuid:ksuid:${project.properties["ksuidVersion"]}")
    testImplementation("com.github.ksuid:ksuid:${project.properties["ksuidVersion"]}")
}