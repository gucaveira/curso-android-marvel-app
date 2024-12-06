plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    api(project(":core"))

    //Paging3 Common
    val paging_version = "3.1.1"
    implementation("androidx.paging:paging-common-ktx:$paging_version")

    // Unit tests
    api("junit:junit:4.13.2")
    api("androidx.arch.core:core-testing:2.1.0")
    val coroutines_version = "1.6.4"
    api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version")
    api("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
}