package dev.mayaqq.skyblock.client.resource.predicates

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import dev.mayaqq.skyblock.client.SkyblockClient.builtin
import dev.mayaqq.skyblock.client.resource.predicates.hypixel.OnSkyblockPredicate
import dev.mayaqq.skyblock.generated.SkyBlockCodecs
import me.owdding.ktcodecs.IncludedCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.ExtraCodecs.LateBoundIdMapper

interface MessagePredicate {

    fun test(): Boolean
    val type: MapCodec<out MessagePredicate>

    companion object {
        val ID_MAPPER: LateBoundIdMapper<ResourceLocation, MapCodec<out MessagePredicate>> = LateBoundIdMapper()

        @IncludedCodec
        val CODEC: Codec<MessagePredicate> = ID_MAPPER.codec(ResourceLocation.CODEC).dispatch(MessagePredicate::type) { it }

        init {
            ID_MAPPER.put(builtin("server/hypixel"), MapCodec.unit(ServerPredicate.HYPIXEL))
            ID_MAPPER.put(builtin("server"), SkyBlockCodecs.ServerPredicateCodec)
            ID_MAPPER.put(builtin("skyblock/islands"), SkyBlockCodecs.IslandPredicateCodec)
            ID_MAPPER.put(builtin("hypixel/on_skyblock"), OnSkyblockPredicate.CODEC)
            ID_MAPPER.put(builtin("always"), AlwaysPredicate.CODEC)
        }
    }
}
