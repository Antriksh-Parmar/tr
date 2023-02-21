plugins {
    java
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "com.ind"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.solr:solr-solrj:9.1.1")
    implementation("org.mongodb:mongodb-driver-sync:4.8.1")
    implementation("org.springframework.data:spring-data-mongodb:4.0.2")
    implementation("org.json:json:20220924")
    implementation("com.fasterxml.jackson.core:jackson-core:2.14.2")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
