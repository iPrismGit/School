plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.iprism.school"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.iprism.school"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            excludes.add("META-INF/DEPENDENCIES")
            excludes.add("META-INF/LICENSE")
            excludes.add("META-INF/LICENSE.txt")
            excludes.add("META-INF/NOTICE")
            excludes.add("META-INF/NOTICE.txt")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.code.scanner)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.google.play.base)
    implementation(libs.google.play.location)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.circleimageview)
    implementation(libs.pinview)
    implementation(libs.avloading)
    implementation(libs.android.image.slider)
    implementation(libs.swipe.refresh.layout)
    implementation(libs.photoview)
    implementation(libs.ucrop)
    implementation(libs.facebook.shimmer)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.app.update)
    implementation(libs.bumptech.glide)
    implementation(libs.exo.player)
    implementation(libs.exo.player.ui)
    implementation("com.prolificinteractive:material-calendarview:1.4.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.28")

}