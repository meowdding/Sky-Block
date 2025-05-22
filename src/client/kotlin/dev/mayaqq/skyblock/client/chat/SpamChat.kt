package dev.mayaqq.skyblock.client.chat

import dev.mayaqq.skyblock.client.config.Config
import dev.mayaqq.skyblock.client.utils.pushPop
import net.minecraft.network.chat.Component
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.chat.ActionBarReceivedEvent
import tech.thatgravyboat.skyblockapi.api.events.chat.ChatReceivedEvent
import tech.thatgravyboat.skyblockapi.api.events.hypixel.ServerChangeEvent
import tech.thatgravyboat.skyblockapi.api.events.info.ActionBarWidget
import tech.thatgravyboat.skyblockapi.api.events.info.RenderActionBarWidgetEvent
import tech.thatgravyboat.skyblockapi.api.events.render.RenderHudEvent
import tech.thatgravyboat.skyblockapi.api.location.LocationAPI
import tech.thatgravyboat.skyblockapi.helpers.McClient
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentMatchResult
import tech.thatgravyboat.skyblockapi.utils.text.Text
import java.util.*
import kotlin.math.sin

object SpamChat {

    private var messages = Collections.synchronizedList(ArrayList<Message>())
    private var previousRender = Date().time

    private var extraMessages = Config.extra.mapNotNull { runCatching { Regex(it) }.getOrNull() }
    private var extraUiMessages = Config.extraUi.mapNotNull { runCatching { Regex(it) }.getOrNull() }

    fun onExtraChange(new: Array<out String>) {
        extraMessages = new.mapNotNull {
            runCatching { Regex(it) }.getOrNull()
        }
    }

    fun onExtraUiChange(new: Array<out String>) {
        extraUiMessages = new.mapNotNull {
            runCatching { Regex(it) }.getOrNull()
        }
    }

    @Subscription
    fun onHudRender(event: RenderHudEvent) {
        if (!Config.enabled) return
        if (McClient.options.hideGui) return

        val font = McClient.self.font
        val now = System.currentTimeMillis()
        val timePassed = now - previousRender

        val animDiv = timePassed.toDouble() / 1000.0
        previousRender = now

        messages.reversed().withIndex().forEach { (index, message) ->
            val messageWidth = font.width(message.component)
            message.height += (index * 10 - message.height) * (animDiv * 5)
            var animOnOff = 0.0
            if (message.time < 500) {
                animOnOff = 1 - message.time / 500.0
            }
            if (message.time > 3500) {
                animOnOff = (message.time - 3500) / 500.0
            }

            animOnOff *= 90.0
            animOnOff += 90.0
            animOnOff *= Math.PI / 180
            animOnOff = sin(animOnOff)
            animOnOff *= -1.0
            animOnOff += 1.0
            val x = -animOnOff * (messageWidth + 30)
            val y = message.height

            // render
            event.graphics.pushPop {
                translate(
                    event.graphics.guiWidth().toFloat() - (x + messageWidth).toFloat(),
                    event.graphics.guiHeight().toFloat() - y.toFloat(),
                    0.0F,
                )

                event.graphics.drawStringWithBackdrop(
                    font,
                    message.component,
                    0,
                    -50,
                    0,
                    0,
                )
            }
            message.time += timePassed
        }

        messages.removeIf { it.time > 4000 }
    }

    private var removing = false
    private var removingRegex: Regex? = null
    private var removingHidingOption: HidingOption? = null
    private var skipLast: Boolean = false

    @Subscription
    fun onChatReceived(event: ChatReceivedEvent.Pre) {
        if (!Config.enabled) return

        if (removingRegex != null && removingRegex!!.find(event.text) != null) {
            removing = false
            removingRegex = null
            if (skipLast) {
                event.cancel()
                skipLast = false
            } else {
                removeMessage(removingHidingOption!!, event)
            }
            removingHidingOption = null
            return
        }

        if (removing && removingHidingOption != null) {
            removeMessage(removingHidingOption!!, event)
            return
        }

        if (extraMessages.isNotEmpty()) {
            for (regex in extraMessages) {
                if (regex.find(event.text) != null) {
                    event.cancel()
                    return
                }
            }
        }

        if (extraUiMessages.isNotEmpty()) {
            for (regex in extraUiMessages) {
                if (regex.find(event.text) != null) {
                    messages.add(Message(event.component, 0, 0.0))
                    event.cancel()
                    return
                }
            }
        }

        SpamMessage.entries.forEach { message ->
            val regex = message.regex
            val option = message.option()
            val found = regex.find(event.component) ?: return@forEach
            if (message.islands.isNotEmpty() && LocationAPI.island !in message.islands) return@forEach
            if (message.multilineData != null) {
                removing = true
                removingRegex = Regex(message.multilineData.endRegex)
                removingHidingOption = option
                skipLast = message.multilineData.skipLast
                if (message.multilineData.skipFirst) {
                    event.cancel()
                    return
                }
            }
            if (removeMessage(option, event, message, found)) return
        }
    }

    private fun removeMessage(option: HidingOption, event: ChatReceivedEvent.Pre, message: SpamMessage? = null, regexResult: ComponentMatchResult? = null): Boolean {
        when (option) {
            HidingOption.SEPARATE -> {
                separate(event.component)
                event.cancel()
                return true
            }

            HidingOption.TOAST -> {
                if (message?.toast == null || regexResult == null) return false
                McClient.self.toastManager.addToast(
                    SkyBlockToast(
                        message.toast.icon,
                        message.toast.buildTitle(regexResult),
                        message.toast.buildBody(regexResult),
                        message.toast.duration
                    ),
                )
                event.cancel()
                return true
            }

            HidingOption.HIDE -> {
                event.cancel()
                return true
            }

            else -> {
                return false
            }
        }
    }

    @Subscription
    fun onServerChange(event: ServerChangeEvent) {
        removing = false
        removingRegex = null
        removingHidingOption = null
    }

    private var abilityUses = 0
    private var lastAbilityUsed = ""
    private val abilityRegex = Regex("§.-?(?<amount>[\\d,]+) Mana \\(§.(?<ability>[^)]+)§.\\)")

    @Subscription
    fun onActionBarWidget(event: ActionBarReceivedEvent.Pre) {

        if (!Config.enabled) return
        if (!Config.abilityUse) return

        event.coloredText.split("     ").forEach { message ->
            if (abilityRegex.matches(message)) {
                if (lastAbilityUsed != message || abilityUses % 3 == 0) {
                    lastAbilityUsed = message
                    abilityUses = 1
                    messages.add(Message(Component.literal(lastAbilityUsed), 0, 0.0))
                } else abilityUses++
            }
        }
    }

    @Subscription
    fun onWidgetRender(event: RenderActionBarWidgetEvent) {
        if (Config.abilityUse && event.widget == ActionBarWidget.ABILITY) {
            event.cancel()
        }
    }

    private fun separate(string: String) = separate(Text.of(string))

    private fun separate(component: Component) {
        val message = Message(component, 0, 0.0)
        messages.add(message)
    }

    data class Message(val component: Component, var time: Long, var height: Double)
}
