import com.ncorti.ktfmt.gradle.tasks.KtfmtFormatTask

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

plugins {
    kotlin("jvm") version "1.9.20"
    id("com.ncorti.ktfmt.gradle") version "0.12.0"
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "7.6"
    }
}

ktfmt {
    kotlinLangStyle()
    removeUnusedImports.set(true)
    maxWidth.set(120)
}

val deleteJavaSourceDirs by tasks.registering {
    // ktfmt creates empty "java" folders during its run, this cleans all the empty Java source folders afterwards.
    doFirst {
        sourceSets.asMap.values
            .flatMap { it.java.sourceDirectories }
            .filter { it.list()?.isEmpty() ?: false }
            .forEach {
                logger.info("Deleting empty Java source dir: $it")
                delete(it)
            }
    }
}

tasks.withType<KtfmtFormatTask> {
    finalizedBy(deleteJavaSourceDirs)
}
