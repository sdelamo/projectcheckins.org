plugins {
    id("org.projectcheckins.micronaut-modules-conventions")
}
dependencies {
    implementation(project(":notification"))

    testImplementation(project(":test-utils"))
    testImplementation("ch.qos.logback:logback-classic")
}
