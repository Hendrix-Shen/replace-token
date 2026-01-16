## replace-token

[![JitPack](https://img.shields.io/jitpack/version/com.github.hendrix-shen/replace-token?style=flat-square)](https://jitpack.io/#Hendrix-Shen/replace-token)
[![Gradle Plugin Portal Version](https://img.shields.io/gradle-plugin-portal/v/top.hendrixshen.replace-token?style=flat-square)](https://plugins.gradle.org/plugin/top.hendrixshen.replace-token)

A gradle to replace token in class file.

### Usages

#### 1. Apply

Since version `1.2.0`, replace-token is available in the [gradle plugin portal](https://plugins.gradle.org/plugin/top.hendrixshen.replace-token):

```groovy
// settings.gradle
pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}
```

Now you can apply the plugin to your project:

```groovy
// build.gradle
plugins {
    id("top.hendrixshen.replace-token").version("1.2.0")
}
```

#### 2. Configure

```groovy
// build.gradle
replaceToken {
    // Set sourceSets
    targetSourceSets.set([sourceSets.main])
    
    // Global token
    replace("foo", "bar")
    // If not specified, all classes are processed by default.
    replaceIn("com/example1/Main")
    
    // Local token
    replace("foo", "bar", "com/example2/Main")
    
    // Inner class example
    replace("foo", "bar", "com/example3/Main\$Inner")
}
```

#### 3. Done

That's it, everything is done

### License

This project is available under the LGPLv3 license. Feel free to learn from it and incorporate it in your own projects.
