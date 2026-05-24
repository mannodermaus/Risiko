allprojects {
    plugins.withId("java") {
        extensions.getByName<JavaPluginExtension>("java").apply {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
    }
}
