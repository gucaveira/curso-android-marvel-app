plugins {
    java
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {

    // OkHttp
    api(libs.bundles.okhttp3)

    // Retrofit
    api(libs.squareup.retrofit)
    api(libs.squareup.retrofit.converter.gson)

    //Gson
    api(libs.gson)

    //Paging3 Common
    implementation(libs.androidx.paging.common.ktx)

    //Javax Inject
    implementation(libs.javax.inject)

    //Coroutines cores
    api(libs.coroutines.core)

    //test unit
    testImplementation(project(":testing"))
}