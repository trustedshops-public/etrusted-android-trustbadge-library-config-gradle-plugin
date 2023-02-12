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

import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ProduceConfigPluginTest {

    @field:TempDir
    lateinit var projectDir: File

    @Test fun `plugin registers task`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("com.etrusted.gradle.trustbadge.config.produce")

        // Verify the result
        assertNotNull(project.tasks.findByName("produce"))
    }

    @Test fun `test decode json and producing properties file works`() {

        // arrange
        val fakeId = "fakeId"
        val fakeSecret = "fakeSecret"

        val inputFilePath = projectDir.path + "/$jsonFileName"
        val outputFilePath = projectDir.path + "/$propFileName"

        File(inputFilePath).apply {
            createNewFile()
            writeText("""
                {
                  "client_id": "$fakeId",
                  "client_secret": "$fakeSecret"
                }
            """.trimIndent())
        }

        // act
        decodeJsonAndProduceConfigFile(
            inputPath = inputFilePath,
            outputPath = outputFilePath,
        )
        val outputFile = File(outputFilePath)

        // assert
        assertTrue(outputFile.exists())
        val outputText = outputFile.readText()
        assertTrue(outputText.contains(keyClientId))
        assertTrue(outputText.contains(keyClientSecret))
        assertTrue(outputText.contains(fakeId))
        assertTrue(outputText.contains(fakeSecret))
    }

    @Test fun `copy file to target returns true if not identical`() {

        // arrange
        val sourceContent = "Cologne"
        val targetContent = "Berlin"

        val sourceFile = File(projectDir.path + "/fakeSource.md").apply {
            createNewFile()
            writeText(sourceContent)
        }
        val targetFile = File(projectDir.path + "/fakeTarget.md").apply {
            createNewFile()
            writeText(targetContent)
        }
        println(sourceFile.readText())
        println(targetFile.readText())

        // act
        val result = sourceFile.copyToTargetIfNotIdentical(targetFile)

        // assert
        assertTrue(result)
    }

    @Test fun `copy file to target returns false if identical`() {

        // arrange
        val sourceContent = "Cologne"
        val targetContent = "Cologne"

        val sourceFile = File(projectDir.path + "/fakeSource.md").apply {
            createNewFile()
            writeText(sourceContent)
        }
        val targetFile = File(projectDir.path + "/fakeTarget.md").apply {
            createNewFile()
            writeText(targetContent)
        }
        println(sourceFile.readText())
        println(targetFile.readText())

        // act
        val result = sourceFile.copyToTargetIfNotIdentical(targetFile)

        // assert
        assertFalse(result)
    }
 }