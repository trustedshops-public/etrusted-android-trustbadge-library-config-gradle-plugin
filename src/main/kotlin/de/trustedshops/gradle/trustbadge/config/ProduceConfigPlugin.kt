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

package de.trustedshops.gradle.trustbadge.config

import groovy.json.JsonSlurper
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

/**
 * Gradle plugin to read the client id and client secret provided by host project in json file
 * And provide them to Trustbadge Android Library as gradle settings.
 */

internal const val propFileName = "trustbadge.properties"
internal const val jsonFileName = "trustbadge-config.json"
internal const val keyClientId = "client_id"
internal const val keyClientSecret = "client_secret"

class ProduceConfigPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.tasks.register("produce") { task ->
            task.doLast {
                val jsonFile = File(project.rootDir, jsonFileName)
                val targetFile = File(project.projectDir, jsonFileName)
                jsonFile.copyToTargetIfNotIdentical(targetFile)
                decodeJsonAndProduceConfigFile(
                    inputPath = "${project.projectDir}/$jsonFileName",
                    outputPath = "${project.projectDir}/$propFileName"
                )
                println("produce task finished")
            }
        }
    }
}

internal fun File.copyToTargetIfNotIdentical(target: File): Boolean {
    return if (!this.readText().contentEquals(target.readText())) {
        if (!target.exists()) { target.createNewFile() }
        this.copyTo(target, overwrite = true)
        true
    } else { false }
}

internal fun decodeJsonAndProduceConfigFile(
    inputPath: String,
    outputPath: String,
) {
    println("decoding json")
    val jsonFile = File(inputPath)
    @Suppress("UNCHECKED_CAST")
    val jsonObject = JsonSlurper().parse(jsonFile) as Map<String, String>

    val clientId = jsonObject[keyClientId]
    val clientSecret = jsonObject[keyClientSecret]

    println("generating $propFileName file")
    val propContent = "$keyClientId=$clientId\n$keyClientSecret=$clientSecret"
    val outputFile = File(outputPath)
    outputFile.createNewFile()
    outputFile.writeText(propContent)
}