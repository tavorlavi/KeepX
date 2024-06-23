plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}



android {
    namespace = "com.example.keepx"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.example.keepx"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-ml-vision:24.1.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-storage:21.0.0")
    implementation("androidx.preference:preference:1.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("io.github.mayuce:AndroidDocumentScanner:1.6.1")
    implementation("com.google.android.gms:play-services-vision:20.1.3")
    implementation("com.google.firebase:firebase-ml-vision:24.1.0")
    implementation("com.google.android.gms:play-services-vision-common:19.1.3")
    implementation("com.journeyapps:zxing-android-embedded:4.1.0")
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.3.0-beta01")
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")
}



