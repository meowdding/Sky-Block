package dev.mayaqq.skyblock.client.resource.predicates.hypixel

import com.mojang.serialization.MapCodec
import dev.mayaqq.skyblock.client.resource.predicates.MessagePredicate
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI

object OnSkyblockPredicate : MessagePredicate {
    override fun test() = LocationAPI.isOnSkyBlock
    val CODEC: MapCodec<OnSkyblockPredicate> = MapCodec.unit(this)
    override val type: MapCodec<out MessagePredicate> = CODEC
}
