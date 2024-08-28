pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
}

rootProject.name = "Alibaba-ARouter"
include(":app")

//include(":arouter-api")
//include(":arouter-compiler")
//include(":arouter-annotation")
//include(":arouter-gradle-plugin")
//include(":arouter-idea-plugin")

include(":module-java")
include(":module-java-export")
include(":module-kotlin")
