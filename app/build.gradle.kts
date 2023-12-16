plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.bookmart.bookmart"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bookmart.bookmart"
        minSdk = 26
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("com.google.firebase:firebase-analytics-ktx:21.5.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.android.libraries.places:places:3.3.0")
    implementation("androidx.core:core-ktx:+")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //SDP - a scalable size unit
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    //material design
    implementation("com.google.android.material:material:1.12.0-alpha01")
    //For round profileimageview in account fragments
    implementation("de.hdodenhof:circleimageview:3.1.0")
    //For GIF as a animation
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.22")
    // dependency for slider view
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")
    //For google maps implementation
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    //drawer navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    //For animations > https://github.com/gayanvoice/android-animations-kotlin
    implementation("com.github.gayanvoice:android-animations-kotlin:1.0.1")
    // Import the BoM for the Firebase platform
    implementation("com.google.firebase:firebase-bom:32.6.0")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    //facebook librery
    implementation("com.facebook.android:facebook-android-sdk:16.2.0")
    // Facebook Login only
    implementation("com.facebook.android:facebook-login:16.2.0")
    //Volly for android networking
    implementation("com.android.volley:volley:1.2.1")
    //spinner view for loading
    implementation("com.github.razir.progressbutton:progressbutton:2.1.0")
    //Image picker
    implementation ("com.github.dhaval2404:imagepicker:2.1")
//for granting premission
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.7.2")
//country code picker
    implementation ("com.hbb20:ccp:2.5.0") // Check for the latest version on GitHub
    implementation ("com.huangyz0918:androidwm:0.2.3")
//image compreser
    implementation ("id.zelory:compressor:3.0.1")
//picasso for loadingi image
    implementation ("com.squareup.picasso:picasso:2.71828")
//facebook shimmer effect for loading view
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
//lottie animation
    implementation ("com.airbnb.android:lottie:5.2.0")
// dependency for slider view
    implementation ("com.github.smarteist:autoimageslider:1.3.9")
// dependency for loading image from url
// Add this dependency in your app/build.gradle file
    implementation ("com.github.bumptech.glide:glide:4.12.0")
//auto adujustabl recylerview
    implementation ("com.google.android.flexbox:flexbox:3.0.0")
//google maps place finder
    implementation ("com.google.android.libraries.places:places:3.3.0")
}








