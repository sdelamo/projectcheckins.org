plugins {
    id("org.projectcheckins.micronaut-http-modules-conventions")
}
dependencies {
    api(project(":email"))
    compileOnly("com.fasterxml.jackson.core:jackson-databind")
    testCompileOnly("com.fasterxml.jackson.core:jackson-databind")
}