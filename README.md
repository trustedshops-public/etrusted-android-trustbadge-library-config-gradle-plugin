# Trustbadge Config Gradle Plugin
[![GitHub License](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://github.com/trustedshops-public/trustbadge-config-gradle-plugin/blob/main/LICENSE)
![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/trustbadge-config-gradle-plugin)
[![codecov](https://codecov.io/gh/trustedshops-public/trustbadge-config-gradle-plugin/branch/main/graph/badge.svg?token=xhHiZ1MCYz)](https://codecov.io/gh/trustedshops-public/trustbadge-config-gradle-plugin)

> This project is currently work in progress and only used by a few
> customers. Tasks might not be stable yet and could change without
> further notice.

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