import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    // id ("com.google.gms.google-services")
    id("kotlin-android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs")
    id("kotlin-parcelize")
}

apply(from = "../config/detekt/detekt.gradle")

val apikeyPropertiesFile = rootProject.file("apikey.properties")
val apikeyProperties = Properties()
apikeyProperties.load(FileInputStream(apikeyPropertiesFile))

android {
    compileSdk = 34
    namespace = "com.example.marvelapp"

    signingConfigs {
        create("release") {
            storeFile = file("$rootDir/store/marvel_app_key_store.jks")
            storePassword = "marvel@123"
            keyPassword = "marvel@123"
            keyAlias = "marvel-app"
        }
    }

    defaultConfig {
        applicationId = "com.example.marvelapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.marvelapp.CustomTestRunner"

        // The following argument makes the Android Test Orchestrator run its
        // "pm clear" command after each test invocation. This command ensures
        // that the app's state is completely cleared between tests.
        testInstrumentationRunnerArguments["clearPackageData"] = "true"

        buildConfigField("String", "PUBLIC_KEY", apikeyProperties["PUBLIC_KEY"].toString())
        buildConfigField("String", "PRIVATE_KEY", apikeyProperties["PRIVATE_KEY"].toString())
        buildConfigField("String", "BASE_URL", "\"https://gateway.marvel.com/v1/public/\"")
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        animationsDisabled = true
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
        create("staging") {
            //habilita configuracao variants de debug
            initWith(getByName("debug"))
            isMinifyEnabled = true
            applicationIdSuffix = ".staging"

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
                //habilita via proguard fazer debug e add break point
                "proguard-rules-staging.pro"
            )
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "21"
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {

    implementation(project(":core"))

    // AndroidX
    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.compose.material3:material3")

    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Runtime do Compose
    implementation("androidx.compose.runtime:runtime")

    // Optional - Integration with activities
    implementation("androidx.activity:activity-compose:1.9.0")
    // Optional - Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
    // Optional - Integration with LiveData
    implementation("androidx.compose.runtime:runtime-livedata")

    // Material design
    implementation("com.google.android.material:material:1.12.0")
    // implementation("androidx.legacy:legacy-support-v4:1.0.0"

    // Navigation
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // ViewModel and LiveData
    val lifecycle_version = "2.8.3"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")

    val coroutines = "1.7.3"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines")


    // Dagger Hilt
    val hilt_version = "2.50"
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")

    // Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-ktx:$room_version")
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-paging:$room_version")
    //kapt("androidx.room:room-compiler:$room_version")

    // Paging3
    val paging_version = "3.3.0"
    implementation("androidx.paging:paging-runtime-ktx:$paging_version")

    // Glide
    val glide_version = "4.13.2"
    implementation("com.github.bumptech.glide:glide:$glide_version")
    //kapt("com.github.bumptech.glide:compiler:$glide_version")

    // Other Libs
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:30.4.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")

    // Unit tests
    testImplementation(project(":testing"))
    testImplementation("androidx.room:room-testing:$room_version")

    // Instrumentation tests
    val espresso_version = "3.4.0"
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espresso_version")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:$espresso_version")

    //orchestrator
    androidTestImplementation("androidx.test:runner:1.6.1")
    androidTestUtil("androidx.test:orchestrator:1.5.0")

    val fragment_version = "1.5.3"
    debugImplementation("androidx.fragment:fragment-testing:$fragment_version")

    androidTestImplementation("com.google.dagger:hilt-android-testing:$hilt_version")
    //kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hilt_version")

    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")

    val coroutines_version = "1.6.4"
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version")
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")
}