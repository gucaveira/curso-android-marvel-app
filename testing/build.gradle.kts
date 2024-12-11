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
    implementation(libs.androidx.paging.common.ktx)

    // Unit tests
    api(libs.junit)
    api(libs.androidx.arch.core.test)
    api(libs.coroutines.test)
    api(libs.mockito.kotlin)
}