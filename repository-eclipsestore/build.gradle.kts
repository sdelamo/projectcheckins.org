plugins {
    id("org.projectcheckins.micronaut-modules-conventions")
}
dependencies {
    implementation(project(":core"))
    annotationProcessor("io.micronaut.eclipsestore:micronaut-eclipsestore-annotations")
    implementation("io.micronaut.eclipsestore:micronaut-eclipsestore")
    implementation("io.micronaut.eclipsestore:micronaut-eclipsestore-annotations")
    implementation("com.github.ksuid:ksuid:${project.properties["ksuidVersion"]}")
}