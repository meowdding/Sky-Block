@file:Suppress("UnstableApiUsage")

import jdk.tools.jlink.resources.plugins
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ksp)
}

version = project.property("version") as String
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("archives_base_name") as String)
}

val targetJavaVersion = 21
java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    withSourcesJar()
}

loom {
    splitEnvironmentSourceSets()

    accessWidenerPath = file("src/main/resources/skyblock.accesswidener")

    mods {
        register("skyblock") {
            sourceSet("main")
            sourceSet("client")
        }
    }

    runs {
        getByName("client") {
            programArg("--quickPlayMultiplayer=hypixel.net")
            vmArg("-Ddevauth.enabled=true")
            vmArg("-Dskyblockapi.debug=true")
        }
    }
}

repositories {
    maven("https://maven.terraformersmc.com/")  // Mod Menu
    maven("https://maven.parchmentmc.org") // Parchment Mappings
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1") // DevAuth
    maven("https://repo.hypixel.net/repository/Hypixel/") // Hypixel API
    maven("https://api.modrinth.com/maven") // Hypixel API kotlin
    maven("https://maven.teamresourceful.com/repository/maven-public/") // Skyblock API
}

dependencies {
    minecraft(libs.minecraft)

    mappings(loom.layered {
        officialMojangMappings()
        parchment(libs.minecraft.parchment)
    })

    modImplementation(libs.bundles.fabric)

    implementation(libs.bundles.meowdding.processing)
    ksp(libs.bundles.meowdding.processing)

    implementation(libs.kotlinx.coroutines.core)

    modImplementation(libs.resourceful.config)
    modImplementation(libs.resourceful.config.kotlin)

    includeModImplementationBundle(libs.bundles.meowdding.asProvider())
    includeModImplementationBundle(libs.bundles.skyblock.api)

    includeModImplementation(libs.olympus)

    modRuntimeOnly(libs.bundles.runtime.mods)
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", libs.versions.minecraft.asProvider().get())
    inputs.property("loader_version", libs.versions.fabric.loader.get())
    inputs.property("kotlin_loader_version", libs.versions.fabric.language.kotlin.get())
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to libs.versions.minecraft.asProvider().get(),
            "loader_version" to libs.versions.fabric.loader.get(),
            "kotlin_loader_version" to libs.versions.fabric.language.kotlin.get(),
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName}" }
    }
}

ksp {
    arg("meowdding.project_name", "SkyBlock")
    arg("meowdding.package", "dev.mayaqq.skyblock.generated")
}

@Suppress("unused")
fun DependencyHandlerScope.includeImplementationBundle(bundle: Provider<ExternalModuleDependencyBundle>) = bundle.get().forEach {
    includeImplementation(provider { it })
}

fun DependencyHandlerScope.includeModImplementationBundle(bundle: Provider<ExternalModuleDependencyBundle>) = bundle.get().forEach {
    includeModImplementation(provider { it })
}

fun <T : ExternalModuleDependency> DependencyHandlerScope.includeImplementation(dependencyNotation: Provider<T>) =
    with(dependencyNotation.get()) {
        include(this)
        modImplementation(this)
    }

fun <T : ExternalModuleDependency> DependencyHandlerScope.includeModImplementation(dependencyNotation: Provider<T>) =
    with(dependencyNotation.get()) {
        include(this)
        modImplementation(this)
    }
