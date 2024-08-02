plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.vkbao.notebook"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vkbao.notebook"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.11"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dataBinding {
        enable = true
    }
}

dependencies {
    implementation ("com.google.android.material:material:1.7.0")
    implementation ("androidx.viewpager2:viewpager2:1.1.0")
    implementation ("androidx.room:room-runtime:2.6.1")
    implementation(libs.room.common)
    implementation(libs.legacy.support.v4)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    annotationProcessor ("androidx.room:room-compiler:2.6.1")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}