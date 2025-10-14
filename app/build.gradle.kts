import jdk.jfr.events.ActiveRecordingEvent.enabled

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.tec.inmobiliariaapp"
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

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.legacy.support.v4)
    implementation(libs.adapter.rxjava2)
    implementation(libs.converter.gson)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("androidx.appcompat:appcompat:1.7.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation ("com.google.android.material:material:1.13.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.9.4")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.9.4")
    implementation ("androidx.lifecycle:lifecycle-runtime:2.9.4")
    implementation ("androidx.navigation:navigation-fragment:2.9.5")
    implementation ("androidx.navigation:navigation-ui:2.9.5")
    implementation ("com.squareup.retrofit2:retrofit:3.0.0")
    implementation ("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation ("com.squareup.okhttp3:okhttp:5.1.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.1.0")
    implementation ("com.github.bumptech.glide:glide:5.0.5")
    annotationProcessor ("com.github.bumptech.glide:compiler:5.0.5")
    implementation ("androidx.security:security-crypto:1.1.0") // EncryptedSharedPreferences
    implementation ("com.google.code.gson:gson:2.13.2")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation ("com.github.bumptech.glide:glide:4.14.2") // O la versión actual
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")
}