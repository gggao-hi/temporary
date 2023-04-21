import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.telenav.arp.tool"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            modules("java.sql")
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe)
            packageName = "DisabledFeatureVerifyTool"
            packageVersion = "1.0.0"
            windows {
                menu = true
                menuGroup = "DisabledFeatureVerifyTool"

                iconFile.set(project.file("app_static_widget_map.ico"))
            }
        }
    }
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("org.xerial:sqlite-jdbc:3.18.0")
//                implementation("app.cash.sqlldelight:sqlite-driver:2.0.0-alpha05")
                implementation("com.google.code.gson:gson:2.10")
            }
        }
        val jvmTest by getting
    }


}


