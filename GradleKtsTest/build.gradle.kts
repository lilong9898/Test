plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(rootProject extInt "compileSdkVersion")
    defaultConfig {
        applicationId = "com.lilong.gradlektstest"
        minSdkVersion(rootProject extInt "minSdkVersion")
        targetSdkVersion(rootProject extInt "targetSdkVersion")
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${rootProject extString "kotlin_version"}")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.1")
}

// region util

infix fun Project.extInt(key: String): Int {
    return rootProject.ext.get(key) as Int
}

infix fun Project.extString(key: String): String {
    return rootProject.ext.get(key).toString()
}

// endregion
