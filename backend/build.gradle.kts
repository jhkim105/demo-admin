plugins {
    id("org.springframework.boot") version "3.5.2"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.asciidoctor.jvm.convert") version "4.0.2"
}

group = "com.example.demo"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

configurations {
    create("asciidoctorExtensions")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    "asciidoctorExtensions"("org.springframework.restdocs:spring-restdocs-asciidoctor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
    // Keep Kotlin compilation aligned with Java 21 for IDEs that still read kotlinOptions
    kotlinOptions {
        jvmTarget = "21"
    }
}

val snippetsDir by extra { file("build/generated-snippets") }

tasks.test {
    outputs.dir(snippetsDir)
}

tasks.named<org.asciidoctor.gradle.jvm.AsciidoctorTask>("asciidoctor") {
    dependsOn(tasks.test)
    inputs.dir(snippetsDir)
    configurations("asciidoctorExtensions")
    baseDirFollowsSourceFile()
    attributes(
        mapOf(
            "snippets" to snippetsDir
        )
    )
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    dependsOn(tasks.named("asciidoctor"))
    from(tasks.named<org.asciidoctor.gradle.jvm.AsciidoctorTask>("asciidoctor").map { it.outputDir }) {
        into("static/docs")
    }
}

tasks.register<Copy>("copyDocs") {
    dependsOn(tasks.named("asciidoctor"))
    from(tasks.named<org.asciidoctor.gradle.jvm.AsciidoctorTask>("asciidoctor").map { it.outputDir })
    into(layout.projectDirectory.dir("../docs"))
}
