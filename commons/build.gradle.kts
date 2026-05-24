plugins {
    id("java-library")
}

dependencies {
    // Moved to GitLab: https://gitlab.com/dev.root1.de/simon
    // (linked to locally compiled file as the remote Maven repo shut down)
    api(variantOf(libs.simon) { classifier("jar-with-dependencies") })

    // Optional
    compileOnly(libs.apple)
}
