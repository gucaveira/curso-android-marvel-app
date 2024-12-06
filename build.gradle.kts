plugins {
    id("io.gitlab.arturbosch.detekt") version "1.18.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("androidx.navigation.safeargs") version "2.7.0" apply false
    id("com.android.application") version "8.7.2" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory.get())
}