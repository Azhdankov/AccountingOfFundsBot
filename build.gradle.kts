plugins {
	java
	id("org.springframework.boot") version "3.3.5"
	id("io.spring.dependency-management") version "1.1.6"
	id("com.diffplug.spotless") version "7.0.0.BETA4"
}

group = "Accounting_Of_Funds"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.telegram:telegrambots-spring-boot-starter:6.9.7.1")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.5")
	implementation("javax.xml.bind:jaxb-api:2.3.1")
	implementation("org.postgresql:postgresql:42.7.4")
	implementation("com.vdurmont:emoji-java:4.0.0")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

spotless {
	java {
		googleJavaFormat().aosp()
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks {
	"spotlessCheck" {
		dependsOn("spotlessApply")
	}
}