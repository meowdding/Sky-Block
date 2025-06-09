package dev.mayaqq.skyblock.client.resource

import dev.mayaqq.skyblock.client.resource.predicates.AlwaysPredicate
import dev.mayaqq.skyblock.client.resource.predicates.MessagePredicate
import me.owdding.ktcodecs.GenerateCodec

@GenerateCodec
data class Message(
    val regex: String,
    val toast: Toast?,
    val predicate: MessagePredicate = AlwaysPredicate,
)
