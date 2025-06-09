package dev.mayaqq.skyblock.client.utils

import com.mojang.serialization.Codec
import me.owdding.ktcodecs.IncludedCodec
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

object IncludedCodecs {

    @IncludedCodec
    val DURATION: Codec<Duration> = Codec.LONG.xmap({ it.milliseconds }, Duration::inWholeMilliseconds)

    @IncludedCodec
    val REGEX: Codec<Regex> = Codec.STRING.xmap({ Regex(it) }, { it.pattern })

    @IncludedCodec
    val COMPONENT_REGEX: Codec<ComponentRegex> = Codec.STRING.xmap({ ComponentRegex(it) }, { it.regex().pattern })

    @IncludedCodec
    var ITEM: Codec<Item> = BuiltInRegistries.ITEM.byNameCodec()
}
