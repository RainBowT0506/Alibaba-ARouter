plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
//    alias(libs.plugins.alibaba.arouter)
}

android {
    namespace = "com.example.alibaba_arouter"
    compileSdk = 34

    kapt {
        arguments {
            arg("AROUTER_MODULE_NAME", project.getName())
        }
    }

    defaultConfig {
        applicationId = "com.example.alibaba_arouter"
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
}

dependencies {
    implementation(project(":module-java-export"))
    implementation(project(":module-java"))
//    implementation(project(":module-kotlin"))


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.guava)
    implementation(libs.arouter.api)
    implementation(libs.arouter.annotation)
    kapt(libs.arouter.compiler)
}