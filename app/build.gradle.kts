import jdk.jfr.events.ActiveRecordingEvent.enabled

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.tec.inmobiliariaapp"
    // Es recomendable usar la última versión estable (ahora mismo 34 o 35)
    // Pero mantendré 36 como indicaste.
    compileSdk = 36

    defaultConfig {
        applicationId = "com.tec.inmobiliariaapp"
        minSdk = 34
        targetSdk = 36
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.legacy.support.v4)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Dependencias de AndroidX y Google Material
    implementation ("androidx.appcompat:appcompat:1.7.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation ("com.google.android.material:material:1.13.0")
    implementation("com.google.code.gson:gson:2.10.1")
    // Dependencias de Architecture Components (Lifecycle)
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.9.4")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.9.4")
    implementation ("androidx.lifecycle:lifecycle-runtime:2.9.4")

    // Dependencias de Navigation
    implementation ("androidx.navigation:navigation-fragment:2.9.5")
    implementation ("androidx.navigation:navigation-ui:2.9.5")


    implementation ("com.squareup.retrofit2:retrofit:3.0.0")
    implementation ("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation ("com.squareup.okhttp3:okhttp:5.1.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.1.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    implementation ("com.github.bumptech.glide:glide:5.0.5")
    annotationProcessor ("com.github.bumptech.glide:compiler:5.0.5")

    // Otras dependencias
    implementation ("androidx.security:security-crypto:1.1.0") // EncryptedSharedPreferences
    implementation ("com.google.code.gson:gson:2.13.2")

    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
}