import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.jichicristina_moldovanpaul.bad_habits_tracker"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.jichicristina_moldovanpaul.bad_habits_tracker"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        val localPropsFile = project.rootProject.file("local.properties")
        if (localPropsFile.exists()) {
            properties.load(localPropsFile.inputStream())
        }
        val groqApiKey = properties.getProperty("GROQ_API_KEY", "\"\"")
        // Dacă utilizatorul a pus ghilimele în local.properties, valoarea le va conține deja. 
        // Dacă nu, trebuie să ne asigurăm că e un String valid în Java adăugându-le noi.
        val finalKey = if (groqApiKey.startsWith("\"") && groqApiKey.endsWith("\"")) groqApiKey else "\"$groqApiKey\""
        buildConfigField("String", "GROQ_API_KEY", finalKey)
    }

    buildFeatures {
        buildConfig = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

ksp {
    arg("room.generateKotlin", "true")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    
    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    
    // Fragment KTX (pentru viewModels())
    implementation("androidx.fragment:fragment-ktx:1.8.1")
    
    // ViewModel & Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    
    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    
    // Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    // Retrofit & OkHttp Logging
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}
