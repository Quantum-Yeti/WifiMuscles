plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "me.theoria.wifimuscles"
    compileSdk = 35

    defaultConfig {
        applicationId = "me.theoria.wifimuscles"
        minSdk = 26
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

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Miscellaneous dependencies
    implementation("com.google.android.gms:play-services-ads:24.3.0")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")


}