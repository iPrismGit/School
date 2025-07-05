pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        jcenter()
        repositories {
            maven { url = uri("https://jitpack.io") }
        }
        mavenCentral()
    }
}

rootProject.name = "School"
include(":app")
 