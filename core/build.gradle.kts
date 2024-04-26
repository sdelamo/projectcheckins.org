plugins {
    id("org.projectcheckins.micronaut-modules-conventions")
}
dependencies {
    api(project(":multitenancy"))
    api(project(":security"))
    api("io.micronaut.security:micronaut-security")
    api("io.micronaut.views:micronaut-views-core:5.3.0")
    api("io.micronaut.views:micronaut-views-fieldset:5.3.0")

    implementation("com.vladsch.flexmark:flexmark:${project.properties["flexmarkVersion"]}")

    compileOnly("com.github.ksuid:ksuid:${project.properties["ksuidVersion"]}")
    testImplementation("com.github.ksuid:ksuid:${project.properties["ksuidVersion"]}")

    testImplementation(project(":test-utils"))
}
