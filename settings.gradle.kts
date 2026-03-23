pluginManagement {
    repositories {
        google()          // 👈 MUST
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()          // 👈 MUST
        mavenCentral()
    }
}

rootProject.name = "AIActionEngine"
include(":app")