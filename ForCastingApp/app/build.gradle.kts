plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.forcastingapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.forcastingapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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

    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding=true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerview)
    kapt("androidx.room:room-compiler:2.6.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Updated version
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Updated version
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.squareup.picasso:picasso:2.8")

    // Coroutines Dependencies
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0") // Latest stable version
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0") // Latest stable version
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.0") // Needed for await()

    // Google Play Services for Location
    implementation("com.google.android.gms:play-services-location:21.0.1") // Latest version

    // Google Play Services for Maps and Places
    implementation("org.osmdroid:osmdroid-android:6.1.11")
// Latest stable version
}
