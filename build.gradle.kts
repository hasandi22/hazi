// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()  
    }

    dependencies {
        classpath ("com.android.tools.build:gradle:8.2.2")
        classpath ("com.google.gms:google-services:4.4.2")
    }
}

plugins {
    id("com.google.gms.google-services") version "4.4.2" apply false

    alias(libs.plugins.android.application) apply false
}










