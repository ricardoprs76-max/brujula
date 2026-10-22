plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.plugin.compose")
}

android {
  namespace = "com.brujula.vocacional"
  compileSdk { version = release(37) }

  defaultConfig {
    applicationId = "com.brujula.vocacional"
    minSdk = 26
    targetSdk = 36
    versionCode = 2
    versionName = "1.1"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      signingConfig = signingConfigs.getByName("debug")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures { compose = true }
}

kotlin {
  compilerOptions {
    freeCompilerArgs.addAll(
      "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
      "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
    )
  }
}

dependencies {
  implementation(platform("androidx.compose:compose-bom:2026.09.00"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.foundation:foundation")
  implementation("androidx.compose.animation:animation")
  implementation("androidx.compose.material3:material3:1.5.0-beta01")
  implementation("androidx.compose.material:material-icons-core")
  implementation("androidx.core:core-ktx:1.18.0")
  implementation("androidx.activity:activity-compose:1.13.0")
}
