plugins {
    java
    application
}

val slf4jVersion = "1.7.30" // releases: http://www.slf4j.org/news.html

repositories {
    mavenLocal()
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(15))
    }
}

dependencies {
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
}

application {
    mainClass.set("dgroomes.TempFilesMain")
}
