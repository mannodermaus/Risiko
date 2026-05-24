plugins {
    id("application")
}

dependencies {
    implementation(project(":commons"))
    implementation(libs.miglayout)
}

application {
    mainClass = "risk.client.Client"
    applicationDefaultJvmArgs = listOf("--add-exports", "java.desktop/com.apple.eawt=ALL-UNNAMED")
}
