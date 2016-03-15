# BuildItBigger
Android Nanodedegree 4th project

This is a joke telling app and focuses heavily on **Gradle**, it factores the functionality into libraries and flavors to keep the build simple. You'll also configure a Google Cloud Endpoints development server to supply the jokes.

# Install

Just open it in Android Studio

# Tests

There is an special function that starts the Local backend and runs the tests, it can be called with 
```
task startBackendAndTest(dependsOn: [":backend:appengineRun", ":app:connectedCheck", ":backend:appengineStop"]) {
    connectedCheck.mustRunAfter ':backend:appengineRun'
    evaluationDependsOn(":backend")
    project(":backend") {
        appengine {
            daemon true
        }
    }
    doLast {
        println "all good"
    }

}
```

It can be called with `gradle startBackendAndTest` 
(An emulator or a connected device must be runnning)
