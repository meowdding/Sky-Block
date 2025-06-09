package dev.mayaqq.skyblock.client.resource.predicates.hypixel

import dev.mayaqq.skyblock.client.resource.predicates.MessagePredicate
import dev.mayaqq.skyblock.generated.SkyBlockCodecs
import me.owdding.ktcodecs.Compact
import me.owdding.ktcodecs.FieldName
import me.owdding.ktcodecs.GenerateCodec
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland

@GenerateCodec
data class IslandPredicate(
    @Compact @FieldName("island") val islands: List<SkyBlockIsland>,
) : MessagePredicate {
    override fun test() = islands.any { it.inIsland() }
    override val type = SkyBlockCodecs.IslandPredicateCodec
}
