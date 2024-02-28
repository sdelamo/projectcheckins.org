plugins {
    `java-library`
    jacoco
}

repositories {
    mavenCentral()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
}
tasks.jacocoTestCoverageVerification {
    enabled = true
    violationRules {
        rule {
            limit {
                minimum = "0".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}
java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}

tasks.withType<JavaCompile> {
	val compilerArgs = options.compilerArgs
	compilerArgs.add("-Werror")
	compilerArgs.add("-Xlint:all,-serial,-processing")
}
