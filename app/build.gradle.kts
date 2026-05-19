plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.volt"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.volt"
        minSdk = 28
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
}
configurations.all {
    resolutionStrategy {
        force("com.robotemi:sdk:1.136.0")
    }
}
dependencies {
    implementation("com.robotemi:sdk:1.136.0")
    implementation("com.otaliastudios:zoomlayout:1.9.0")
    implementation ("com.ernestoyaquello.dragdropswiperecyclerview:drag-drop-swipe-recyclerview:1.2.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}