
buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        //firebase
        classpath ("com.google.gms:google-services:4.3.15")
    }
}

plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
}