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
        versionCode = 22
        versionName = "2.1.1"

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

    // Google Ads
    implementation(libs.play.services.ads.v2440)

    // Charting Libraries
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // In-App-Updates
    implementation ("com.google.android.play:app-update:2.1.0")

    // ViewPager
    implementation ("androidx.viewpager2:viewpager2:1.0.0")


}