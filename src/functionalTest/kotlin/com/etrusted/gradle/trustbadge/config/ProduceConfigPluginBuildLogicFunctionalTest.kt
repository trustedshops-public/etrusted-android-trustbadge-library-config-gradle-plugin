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

package com.etrusted.gradle.trustbadge.config

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue


class ProduceConfigPluginBuildLogicFunctionalTest {

    @field:TempDir
    lateinit var projectDir: File

    private val buildFile by lazy { projectDir.resolve("build.gradle") }
    private val settingsFile by lazy { projectDir.resolve("settings.gradle") }
    private val configFile by lazy { projectDir.resolve("trustbadge-config.json") }
    private val outputPropFile by lazy { projectDir.resolve("trustbadge.properties") }

    private val fakeId = "fakeId"
    private val fakeSecret = "fakeSecret"

    @Test fun `can run task`() {
        // Set up the test build and trustbadge-config.json file
        settingsFile.writeText("rootProject.name = \"example-project\"")
        configFile.writeText("""
            {
              "client_id": "$fakeId",
              "client_secret": "$fakeSecret"
            }
        """.trimIndent())
        buildFile.writeText("""
            plugins {
                id("com.etrusted.gradle.trustbadge.config.produce")
            }
        """.trimIndent())

        // Run the build
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("produce")
        runner.withProjectDir(projectDir)
        val result = runner.build()

        // Verify the result
        assertTrue(outputPropFile.exists())
        assertTrue(outputPropFile.readText().contains(fakeId))
        assertTrue(outputPropFile.readText().contains(fakeSecret))
        assertTrue(result.output.contains("decoding"))
        assertTrue(result.output.contains("produce task finished"))
    }

}