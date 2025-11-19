plugins {
    id("java")
    id("io.qameta.allure") version "2.10.0"
}

group = "ru.kev35"
version = "1.0-SNAPSHOT"


allure {
    report {
        version.set("2.19.0") //версия Allure Report (https://github.com/allure-framework/allure2)
    }
    adapter {
        aspectjWeaver.set(true) // обработка аннотации @Step
        frameworks {
            junit5 {
                adapterVersion.set("2.19.0") //версия Allure JUnit5 (https://github.com/allure-framework/allure-java)
            }
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("com.codeborne:selenide:7.9.4")
    testImplementation("com.codeborne:pdf-test:1.5.0")
    testImplementation("com.codeborne:xls-test:1.4.3")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("com.github.javafaker:javafaker:1.0.2")

    testImplementation("com.opencsv:opencsv:5.12.0")

    testImplementation("com.google.code.gson:gson:2.13.2")

    testImplementation("com.fasterxml.jackson.core:jackson-core:2.16.0")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
    testImplementation("com.fasterxml.jackson.core:jackson-annotations:2.16.0")

    testImplementation("io.qameta.allure:allure-selenide:2.17.3")
    testRuntimeOnly("org.aspectj:aspectjweaver:1.9.25")

    implementation("org.slf4j:slf4j-api:2.0.7")
}

tasks.test {
    useJUnitPlatform()
}