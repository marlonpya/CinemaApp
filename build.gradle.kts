// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    // AGP 9.x bundles Kotlin internally — no separate kotlin plugin needed here.
}
