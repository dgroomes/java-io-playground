plugins {
    java
    application
}

val slf4jVersion = "1.7.34" // SLF4J releases: http://www.slf4j.org/news.html
val zstdJniVersion = "1.5.2-1" // zstd-jni releases: https://github.com/luben/zstd-jni/tags

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

dependencies {
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    implementation("com.github.luben:zstd-jni:$zstdJniVersion")
}

application {
    mainClass.set("dgroomes.FileWriterMain")
}
