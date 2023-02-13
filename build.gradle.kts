/*
 * MIT License
 *
 * Copyright (c) 2023 Trusted Shops AG
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

group = "com.etrusted.gradle.trustbadge"
version = "0.0.01"

plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
    signing
    id("com.gradle.plugin-publish") version "1.1.0"
    jacoco
    id("jacoco-report-aggregation")
}

repositories {
    mavenCentral()
}

dependencies {
    // Use JUnit test framework for unit tests
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(gradleTestKit())
}

gradlePlugin {
    website.set("https://github.com/trustedshops-public/trustbadge-config-gradle-plugin")
    vcsUrl.set("https://github.com/trustedshops-public/trustbadge-config-gradle-plugin.git")

    val produce by plugins.creating {
        id = "com.etrusted.gradle.trustbadge.config.produce"
        displayName = "Trustbadge-config gradle plugin"
        description = "This plugin converts the trustbadge-config.json file for Trustbadge into a set of resources that the Trustbadge library can use."
        tags.set(setOf("trustbadge", "trustbadge-config", "android"))
        implementationClass = "com.etrusted.gradle.trustbadge.config.ProduceConfigPlugin"
    }
}

// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") {}

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

// functional tests
val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
    useJUnitPlatform()
}

gradlePlugin.testSourceSets(functionalTestSourceSet)

tasks.named<Task>("check") {
    // Run the functional tests as part of `check`
    dependsOn(functionalTest)
}

tasks.named<Test>("test") {
    // Use JUnit Jupiter for unit tests.
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.8"
    reportsDirectory.set(layout.buildDirectory.dir("mergedReportDir"))
}

tasks.check {
    // generate reports after running tests
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {

    reports {
        // activate jacoco xml for codecov
        xml.required.set(true)
        xml.required.set(true)
    }

    // make sure tests run before generating reports
    dependsOn(tasks.check)
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
}

publishing {
    repositories {
        maven {
            name = "localPluginRepository"
            url = uri("../local-plugin-repository")
        }
    }
}