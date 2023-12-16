pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven("https://maven.google.com")
        maven("https://jitpack.io")
        mavenCentral()
        jcenter()



    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
        repositories {
            google()
            mavenCentral()
            maven("https://maven.google.com")
            maven("https://jitpack.io")
            jcenter()

        }

}

rootProject.name = "BookMart"
include(":app")
