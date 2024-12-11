import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.devtools.ksp)
}


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
        jvmTarget = JavaVersion.VERSION_21.toString()
        freeCompilerArgs += listOf("-Xopt-in=kotlin.RequiresOptIn")
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {

    implementation(project(":core"))
    testImplementation(project(":testing"))

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.compose.material3)
    implementation(platform(libs.androidx.compose.bom))

    // Android Studio Preview support
    implementation(libs.androidx.compose.preview)
    debugImplementation(libs.androidx.compose.tooling)

    // Runtime do Compose
    implementation(libs.androidx.compose.runtime)

    // Optional - Integration with activities
    implementation(libs.androidx.compose.activity)
    // Optional - Integration with ViewModels
    implementation(libs.androidx.compose.lifecycle.viewmodel)
    // Optional - Integration with LiveData
    implementation(libs.androidx.compose.runtime.livedata)

    // Material design
    implementation(libs.android.material)
    // implementation("androidx.legacy:legacy-support-v4:1.0.0"

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // ViewModel and LiveData
    implementation(libs.bundles.androidx.lifecycle)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Dagger Hilt
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)

    // Room
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)

    // Paging3
    implementation(libs.androidx.paging.runtime.ktx)

    // Glide
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    // Other Libs
    implementation(libs.shimmer)
    implementation(libs.gson)
    implementation(libs.androidx.datastore.preferences)

    //Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)

    // Unit tests
    testImplementation(libs.androidx.room.testing)

    // Instrumentation tests
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.contrib)

    //orchestrator
    androidTestImplementation(libs.androidx.orchestrator.test.runner)
    androidTestUtil(libs.androidx.orchestrator)

    debugImplementation(libs.androidx.fragment.testing)

    androidTestImplementation(libs.dagger.hilt.android.testing)
    kspAndroidTest(libs.dagger.hilt.android.compiler)

    androidTestImplementation(libs.okhttp3.mockwebserver)

    androidTestImplementation(libs.coroutines.test)
    androidTestImplementation(libs.androidx.navigation.testing)
}