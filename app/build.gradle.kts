import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}


// Release signing credentials are kept out of git in local.properties. See
// local.properties for the RELEASE_STORE_FILE/RELEASE_STORE_PASSWORD/
// RELEASE_KEY_ALIAS/RELEASE_KEY_PASSWORD keys this reads.
val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(FileInputStream(localPropertiesFile))
    }
}
val hasReleaseSigningConfig = localProperties.getProperty("RELEASE_STORE_FILE") != null

android {
    namespace = "com.bluevolume.wearlorcanaloretracker"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.bluevolume.wearlorcanaloretracker"
        minSdk = 30
        targetSdk = 36
        versionCode = 2
        versionName = "1.0"
    }

    signingConfigs {
        if (hasReleaseSigningConfig) {
            create("release") {
                storeFile = rootProject.file(localProperties.getProperty("RELEASE_STORE_FILE"))
                storePassword = localProperties.getProperty("RELEASE_STORE_PASSWORD")
                keyAlias = localProperties.getProperty("RELEASE_KEY_ALIAS")
                keyPassword = localProperties.getProperty("RELEASE_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            // Falls back to the debug cert until RELEASE_STORE_FILE etc. are set in
            // local.properties, so local release builds keep working either way.
            signingConfig = if (hasReleaseSigningConfig) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    ndkVersion = "27.1.12297006"
    buildToolsVersion = "35.0.0"
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.compose.foundation)
    implementation(libs.wear.compose.material3)
    implementation(libs.wear.tooling.preview)
    implementation(libs.activity.compose)
    implementation(libs.core.splashscreen)
    implementation(libs.tiles)
    implementation(libs.tiles.material)
    implementation(libs.tiles.tooling.preview)
    implementation(libs.horologist.compose.tools)
    implementation(libs.horologist.tiles)
    implementation(libs.watchface.complications.data.source.ktx)
    implementation(libs.navigation.compose)
    implementation(libs.compose.navigation)
    implementation(libs.wear)

    // Android Test Dependencies
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    debugImplementation(libs.tiles.tooling)

    // Material3 Dependency
    implementation(libs.material3)

    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // implementation(libs.ambient)
    //implementation(libs.wear.ambient)
}
