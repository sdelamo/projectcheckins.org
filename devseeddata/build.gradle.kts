plugins {
    id("org.projectcheckins.micronaut-modules-conventions")
}
dependencies {
    implementation(project(":core"))
    implementation(project(":email"))
    implementation(project(":security"))
}