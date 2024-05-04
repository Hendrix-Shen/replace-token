## replace-token

[![JitPack](https://jitpack.io/v/Hendrix-Shen/replace-token.svg)](https://jitpack.io/#Hendrix-Shen/replace-token)

A gradle to replace token in class file.

### Usages

#### 1. Apply

replace-token is available in jitpack. You need to tell gradle how to locate replace-token in jitpack:

```groovy
// settings.gradle
pluginManagement {
    repositories {
        mavenCentral()
        
        maven {
            name("JitPack")
            url("https://jitpack.io")
        }
    }
    
    resolutionStrategy {
        eachPlugin {
            switch (requested.id.id) {
                case "top.hendrixshen.replace-token": {
                    useModule("com.github.Hendrix-Shen:replace-token:${requested.version}")
                    break
                }
            }
        }
    }
}
```

Now you can apply the plugin to your project:

```groovy
// build.gradle
plugins {
    id("top.hendrixshen.replace-token").version("1.0.0")
}
```

#### 2. Configure

```groovy
// build.gradle
replaceToken {
    // Global token
    replace("foo", "bar")
    // If not specified, all classes are processed by default.
    replaceIn("com/example1/Main")
    
    // Local token
    replace("foo", "bar", "com/example2/Main")
    
    // Inner class example
    replace("foo", "bar", "com/example3/Main$Inner")
}
```

#### 3. Done

That's it, everything is done

### License

This project is available under the LGPLv3 license. Feel free to learn from it and incorporate it in your own projects.
