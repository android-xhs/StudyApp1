plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.studyapp01"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.studyapp01"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
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

    kotlin {
        jvmToolchain(17)
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
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
    implementation("androidx.navigation:navigation-ui-ktx:2.9.6")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.6")
    implementation("io.insert-koin:koin-core:3.5.0")
    implementation("io.insert-koin:koin-android:3.5.0")
    implementation("com.airbnb.android:lottie:6.6.6")
    implementation("com.tencent:mmkv:1.3.4")
    implementation(kotlin("reflect"))
    implementation("com.github.android-xhs.mylibrary:mylibrary:dev-SNAPSHOT")
    implementation("com.github.android-xhs.mylibrary:mylibrary2:1.0.6")

}