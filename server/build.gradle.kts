plugins {
    id("application")
}

dependencies {
    implementation(project(":commons"))
}

application {
    mainClass = "risk.server.Server"
    applicationDefaultJvmArgs = listOf("--add-exports", "java.desktop/com.apple.eawt=ALL-UNNAMED")
}
