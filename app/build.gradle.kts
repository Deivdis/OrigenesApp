plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.origenes"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.origenes"
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
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation("com.squareup.picasso:picasso:2.71828") // Libreria de Picasso
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2") // Libreria de Imagenes
    implementation ("com.github.bumptech.glide:glide:4.12.0") // Libreria de Glide
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0") // Libreria de Glide
    implementation ("org.mindrot:jbcrypt:0.4") // Libreria de Bcrypt
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}