plugins {
    base
    id("jacoco-report-aggregation")
}

repositories {
    mavenCentral()
}

dependencies {
    jacocoAggregation(project(":multitenancy"))
    jacocoAggregation(project(":core"))
    jacocoAggregation(project(":http"))
    jacocoAggregation(project(":netty"))
    jacocoAggregation(project(":processor"))
    jacocoAggregation(project(":repository-eclipsestore"))
}

reporting {
    reports {
        val testCodeCoverageReport by creating(JacocoCoverageReport::class) {
            testType = TestSuiteType.UNIT_TEST
        }
    }
}

tasks.check {
    dependsOn(tasks.named<JacocoReport>("testCodeCoverageReport")) 
}
