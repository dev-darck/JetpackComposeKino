import ImplementsDependency.androidTestImplementation
import ImplementsDependency.annotationProcessDependency
import ImplementsDependency.compileOnlyDependency
import ImplementsDependency.debuggerDependency
import ImplementsDependency.kaptDependency
import ImplementsDependency.listImplements
import ImplementsDependency.testImplementations

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
}

repositories {
    google()
    mavenCentral()
}

android {
    compileSdkVersion(Versions.compileSdkVersion)
    buildToolsVersion = Versions.buildToolsVersion

    defaultConfig {
        applicationId = Versions.applicationId
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)
        versionCode = Versions.versionCode
        versionName = Versions.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    viewBinding {
        android.buildFeatures.viewBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
        getByName("debug") {
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    listImplements.forEach {
        implementation(it)
    }
    annotationProcessDependency.forEach {
        annotationProcessor(it)
    }
    compileOnlyDependency.forEach {
        compileOnly(it)
    }
    debuggerDependency.forEach {
        debugImplementation(it)
    }
    kaptDependency.forEach {
        kapt(it)
    }
    testImplementations.forEach {
        testImplementation(it)
    }
    androidTestImplementation.forEach {
        androidTestImplementation(it)
    }
}