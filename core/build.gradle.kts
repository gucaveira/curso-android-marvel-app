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
    api(platform("com.squareup.okhttp3:okhttp-bom:4.9.0"))
    api("com.squareup.okhttp3:okhttp")
    api("com.squareup.okhttp3:logging-interceptor")

    // Retrofit
    val retrofit_version = "2.9.0"
    api("com.squareup.retrofit2:retrofit:$retrofit_version")
    api("com.squareup.retrofit2:converter-gson:$retrofit_version")

    //Gson
    api("com.google.code.gson:gson:2.9.1")

    //Paging3 Common
    val paging_version = "3.3.0"
    implementation("androidx.paging:paging-common-ktx:$paging_version")

    //Javax Inject
    implementation("javax.inject:javax.inject:1")

    //Coroutines cores
    val coroutines_version = "1.6.4"
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")

    //test unit
    testImplementation(project(":testing"))
}