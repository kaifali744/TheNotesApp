// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
    }
    dependencies {
        val navVersion = "2.7.5"
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.21")
    }

}

plugins {
    id ("com.android.application") version "8.8.0" apply false
    id ("com.android.library") version "8.8.0" apply false
    id ("org.jetbrains.kotlin.android") version "1.7.20" apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false

}