plugins {
	java
	id("org.springframework.boot") version "3.4.5"
	id("io.spring.dependency-management") version "1.1.7"
	jacoco
}

repositories {
	mavenCentral()
}

group = "com.jkuo"
version = "0.0.1-SNAPSHOT"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework:spring-context")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.google.code.gson:gson")
	implementation("org.redisson:redisson:3.47.0")
	implementation("com.twilio.sdk:twilio:10.9.0")
	implementation("com.googlecode.libphonenumber:libphonenumber:9.0.5")
	
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testImplementation("org.testcontainers:testcontainers:1.21.0")
	testImplementation("org.mockito:mockito-core")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
	reports {
        xml.required = false
        csv.required = true
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

/* 
dependencyLocking {
   lockAllConfigurations()
}
*/