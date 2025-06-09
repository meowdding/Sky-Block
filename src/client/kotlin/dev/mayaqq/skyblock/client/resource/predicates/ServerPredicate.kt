package dev.mayaqq.skyblock.client.resource.predicates

import dev.mayaqq.skyblock.generated.SkyBlockCodecs
import me.owdding.ktcodecs.FieldName
import me.owdding.ktcodecs.GenerateCodec
import tech.thatgravyboat.skyblockapi.helpers.McClient

@GenerateCodec
data class ServerPredicate(
    @FieldName("server") val serverRegex: Regex,
) : MessagePredicate {
    override fun test() = McClient.self.connection?.serverData?.ip?.let { serverRegex.matches(it) } == true
    override val type = SkyBlockCodecs.ServerPredicateCodec

    companion object {
        val HYPIXEL = ServerPredicate(Regex(".*\\.?hypixel\\.net"))
    }
}
