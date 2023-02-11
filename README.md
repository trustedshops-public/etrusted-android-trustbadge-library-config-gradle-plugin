# Trustbadge Config Gradle Plugin

This plugin converts the trustbadge-config.json file for Trustbadge into a set of resources that the Trustbadge library can use. It is primarily used for the Trustbadge library on Android.

## Usage

### Plugins DSL

Add the following to your project's settings.gradle:

```
pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}
```

Apply the plugin in your app's build.gradle:

```
plugins {
    id("com.etrusted.gradle.trustbadge.config.produce") version "0.0.01"
}
```