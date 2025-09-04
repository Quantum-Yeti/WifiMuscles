plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "me.theoria.wifimuscles"
    compileSdk = 36

    defaultConfig {
        applicationId = "me.theoria.wifimuscles"
        minSdk = 26
        targetSdk = 35
        versionCode = 35
        versionName = "2.2.4"

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
    implementation(libs.swiperefreshlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Google Ads
    implementation(libs.play.services.ads)

    // Charting Libraries
    implementation (libs.github.mpandroidchart)

    // In-App-Updates
    implementation (libs.app.update)

    // ViewPager
    implementation (libs.viewpager2)
    implementation (libs.google.material)

    //Lottie Animation-Loading Library
    implementation (libs.lottie)
}