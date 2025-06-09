package dev.mayaqq.skyblock.client.resource.predicates

import com.mojang.serialization.MapCodec

object AlwaysPredicate : MessagePredicate {
    override fun test() = true
    val CODEC: MapCodec<AlwaysPredicate> = MapCodec.unit(this)
    override val type: MapCodec<out MessagePredicate> = CODEC
}
