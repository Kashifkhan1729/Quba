

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.quba"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.quba"
        minSdk = 34
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.analytics)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.compose.material:material-icons-extended-android:1.7.8")

        // Import the BoM for the Firebase platform
        implementation (platform("com.google.firebase:firebase-bom:33.15.0"))

        // Declare the dependencies for the desired Firebase products without specifying versions
        // For example, declare the dependencies for Firebase Authentication and Cloud Firestore
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.4")
    implementation ("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database-ktx")
        implementation ("com.google.firebase:firebase-firestore")
    implementation("com.google.dagger:hilt-android:2.56.2")
    implementation ("androidx.compose.runtime:runtime-livedata:1.8.2")

}