package dev.mayaqq.skyblock.client

import dev.mayaqq.skyblock.client.chat.SpamChat
import dev.mayaqq.skyblock.client.config.ConfigManager
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.resources.FileToIdConverter
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tech.thatgravyboat.skyblockapi.api.SkyBlockAPI
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

const val MODID = "skyblock"
const val MODNAME = "Sky-Block"

object SkyblockClient : ModInitializer, Logger by LoggerFactory.getLogger(MODID), IdentifiableResourceReloadListener {
    val self = FabricLoader.getInstance().getModContainer("skyblock").get()
    override fun onInitialize() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(this)
        SkyBlockAPI.eventBus.register(SpamChat)
        SkyBlockAPI.eventBus.register(ConfigManager)
    }

    fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MODID, path)
    fun builtin(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath("builtin", path)

    override fun getFabricId() = id(MODID)

    override fun reload(
        barrier: PreparableReloadListener.PreparationBarrier,
        manager: ResourceManager,
        backgroundExecutor: Executor,
        gameExecutor: Executor,
    ): CompletableFuture<Void>? {
        val location = FileToIdConverter.json("chat_filters")

        return CompletableFuture.completedFuture(null).thenCompose { barrier.wait(it) }
    }

    override fun getName(): String {
        return this.javaClass.getSimpleName()
    }
}
