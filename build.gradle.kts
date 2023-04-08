plugins {
    java
    `maven-publish`
    id("com.github.weave-mc.weave") version "8b70bcc707"
    id("com.github.johnrengelman.shadow") version "8.1.1"

}

group = "com.example"
version = "1.0"

minecraft.version("1.8.9")

repositories {
    maven("https://jitpack.io")
    maven("https://repo.spongepowered.org/maven/")
}

dependencies {
    implementation("com.github.twitch4j:twitch4j:1.6.0")
    implementation("org.yaml:snakeyaml:2.0")
    compileOnly("com.github.weave-mc:weave-loader:6a9e6a3245")

    compileOnly("org.spongepowered:mixin:0.8.5")

}

tasks.jar{
    val wantedJar = listOf("twitch4j")
    configurations["compileClasspath"]
        .filter { wantedJar.find { wantedJarName -> it.name.contains(wantedJarName) } != null }
        .forEach { file: File ->
            from(zipTree(file.absoluteFile)) {
                this.duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            }
        }
}

tasks.compileJava {
    options.release.set(11)
    options.encoding = "UTF-8";
}
