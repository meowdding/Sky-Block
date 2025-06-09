package dev.mayaqq.skyblock.client.resource

import me.owdding.ktcodecs.GenerateCodec
import net.minecraft.world.item.Item

@GenerateCodec
data class Toast(
    val duration: Float,
    val title: String,
    val body: String,
    val icon: Item?,
) {
}
