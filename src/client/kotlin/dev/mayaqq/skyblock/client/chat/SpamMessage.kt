package dev.mayaqq.skyblock.client.chat

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement
import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigEntryElement
import dev.mayaqq.skyblock.client.utils.DynamicEntryData
import dev.mayaqq.skyblock.client.utils.DynamicSeparator
import dev.mayaqq.skyblock.client.utils.ResourcefulConfigEnumValueEntry
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.utils.regex.component.ComponentRegex

enum class MessageCategory {
    LOBBY,
    ABILITY,
    SYSTEM,
    SKYBLOCK,
    TRAVEL,
    DUNGEON_HUB,
    DUNGEON,
    MINING,
    ITEM,
    MISC,
    IRONMAN,
    CENTURY_CAKE,
    FISHING,
    RIFT,
    EVENT
}

enum class SpamMessage(
    // language=RegExp
    regex: String,
    val category: MessageCategory,
    val toast: ToastData? = null,
    val multilineData: MultilineData? = null,
    vararg val islands: SkyBlockIsland
) : ResourcefulConfigEnumValueEntry, ResourcefulConfigEntryElement {

    // Lobby
    LOBBY_JOIN(
        """(?:>>> )?[\s\S]*joined the lobby!(?: <<<)?""",
        MessageCategory.LOBBY,
    ),

    // Ability
    PREVIOUS_ABILITY_REMOVED(
        """Your previous [\s\S]* was removed!""",
        MessageCategory.ABILITY,
    ),
    PICKAXE_ABILITY_COOLDOWN(
        """^Your pickaxe ability is on cooldown for[\s\S]*""",
        MessageCategory.ABILITY,
    ),
    PICKAXE_ABILITY_USED(
        """You used your [\s\S]* Pickaxe Ability!""",
        MessageCategory.ABILITY,
    ),
    PICKAXE_ABILITY_AVAILABLE(
        """[\s\S]* is now available!""",
        MessageCategory.ABILITY,
    ),
    PICKOBULUS_DESTROYED(
        """^Your Pickobulus destroyed[\s\S]*""",
        MessageCategory.ABILITY,
    ),
    PICKOBULUS_DESTROY_NOTHING(
        """Your Pickobulus didn't destroy any blocks!""",
        MessageCategory.ABILITY,
    ),
    ABILITY_ON_COOLDOWN(
        """^This ability is on cooldown for[\s\S]*""",
        MessageCategory.ABILITY,
    ),

    // System
    GEXP_GAIN(
        """You earned (\d+ GEXP) from playing [\s\S]*!|You earned (\d+ GEXP \\+ \d+ Event EXP) from playing [\s\S]*!""",
        MessageCategory.SYSTEM,
        ToastData(4000F, "§2§lPlaytime Reward", "{1}", ItemStack(Items.GOLDEN_APPLE)),
    ),
    PROFILE_ID(
        """Profile ID: [\s\S]*""",
        MessageCategory.SYSTEM,
    ),
    PLAYING_ON_PROFILE(
        """You are playing on profile: [\s\S]*""",
        MessageCategory.SYSTEM,
    ),
    WATCHDOG_REPORT(
        """[\s\S]*(\[WATCHDOG ANNOUNCEMENT]|Watchdog has banned|Staff have banned an additional|Blacklisted modifications are a bannable offense!)[\s\S]*""",
        MessageCategory.SYSTEM,
    ),
    STORAGE_COOLDOWN(
        """You may only use this menu after 4s on the server!""",
        MessageCategory.SYSTEM,
    ),
    LATEST_UPDATE(
        """Latest update: SkyBlock [\s\S]* CLICK""",
        MessageCategory.SYSTEM,
    ),
    FRIEND_STATUS(
        """(Friend) > ([\s\S]*)""",
        MessageCategory.SYSTEM,
        ToastData(4000F, "{1}", "{2}")
    ),
    GUILD_STATUS(
        """(Guild) > ([\s\S]*)""",
        MessageCategory.SYSTEM,
    ),

    // Skyblock
    SKYBLOCK_WELCOME(
        """Welcome to Hypixel SkyBlock!""",
        MessageCategory.SKYBLOCK,
    ),
    FIRE_SALE(
        """A FIRE SALE [\s\S]*""",
        MessageCategory.SKYBLOCK,
    ),
    ALLOWANCE(
        """(ALLOWANCE!) You earned ([\s\S]* coins)!""",
        MessageCategory.SKYBLOCK,
        ToastData(4000F, "{1}", "{2}", Items.GOLD_INGOT.defaultInstance)
    ),
    NPC(
        """\\\[NPC] [\s\S]*""",
        MessageCategory.SKYBLOCK,
    ),
    NPC_BUY(
        """You bought [\s\S]* x\d+ for [\d.]+[\dA-Za-z]* Coins!""",
        MessageCategory.SKYBLOCK,
    ),
    BUY_LIMIT(
        """You may only buy up to [\s\S]* of this item each day!""",
        MessageCategory.SKYBLOCK,
    ),
    SACKS(
        """^\\[Sacks\\][\s\S]*""",
        MessageCategory.SKYBLOCK,
    ),
    TELEPORT_PAD(
        """Warped from the ([\s\S]*) Teleport Pad to the ([\s\S]*) Teleport Pad!*""",
        MessageCategory.SKYBLOCK,
    ),
    FROM_STASH(
        """From stash: [\s\S]*""",
        MessageCategory.SKYBLOCK,
    ),
    PICKED_UP_ALL(
        """^You picked up all items from your[\s\S]*!""",
        MessageCategory.SKYBLOCK,
    ),
    STASH_EMPTY(
        """^Your [\s\S]* stash is empty!""",
        MessageCategory.SKYBLOCK,
    ),
    INVENTORY_FULL(
        """Your inventory is full!""",
        MessageCategory.SKYBLOCK,
    ),
    INVENTORY_FULL_REMINDER(
        """^Inventory full? Don't forget to check out your Storage inside the SkyBlock Menu!""",
        MessageCategory.SKYBLOCK,
    ),
    STASH_NOT_HOLDING_ANYTHING(
        """Your stash isn't holding any items or materials!""",
        MessageCategory.SKYBLOCK,
    ),
    BANK_DEPOSIT(
        """You have deposited[\s\S]*""",
        MessageCategory.SKYBLOCK,
    ),
    SELL(
        """^You sold[\s\S]*""",
        MessageCategory.SKYBLOCK,
    ),
    BANK_WITHDRAWAL(
        """You have withdrawn[\s\S]*""",
        MessageCategory.SKYBLOCK,
    ),
    DEPOSITING(
        """Depositing coins...""",
        MessageCategory.SKYBLOCK,
    ),
    WITHDRAWING(
        """Withdrawing coins...""",
        MessageCategory.SKYBLOCK,
    ),
    CANNOT_DEPOSIT_THIS_LITTLE(
        """You can't deposit this little!""",
        MessageCategory.SKYBLOCK,
    ),
    MAXED_SUPERCRAFT(
        """^Maxed the Supercraft to[\s\S]*""",
        MessageCategory.SKYBLOCK,
    ),
    SUPERCRAFTED(
        """^You Supercrafted [\s\S]*""",
        MessageCategory.SKYBLOCK,
    ),


    // Travel
    WARPING(
        """Warping...""",
        MessageCategory.TRAVEL,
    ),
    SENDING_TO_SERVER(
        """Sending to server [\s\S]*""",
        MessageCategory.TRAVEL,
    ),
    UNKNOWN_DESTINATION(
        """Unknown destination! Check the Fast Travel menu to view options!""",
        MessageCategory.TRAVEL,
    ),

    // Dungeon Hub
    DUNGEON_REWARD(
        """RARE REWARD! .+ found a .+ in their .+!""",
        MessageCategory.DUNGEON_HUB,
    ),

    // Dungeon
    CANNOT_USE_OUTSIDE_DUNGEON(
        """You can only use this item in dungeons!""",
        MessageCategory.DUNGEON,
    ),
    QUEUING(
        """Queuing... \\(Attempt [\s\S]{3}\\)""",
        MessageCategory.DUNGEON,
    ),
    STARTING(
        """Starting in \d second(s)?.""",
        MessageCategory.DUNGEON,
        SkyBlockIsland.THE_CATACOMBS
    ),
    STATS_DOUBLED(
        """Your [\s\S]* stats are doubled because you are the only player using this class!""",
        MessageCategory.DUNGEON,
        SkyBlockIsland.THE_CATACOMBS
    ),
    CLASS_MESSAGE(
        """\[(Mage|Healer|Berserk|Archer|Tank)] [\s\S]*""",
        MessageCategory.DUNGEON,
        SkyBlockIsland.THE_CATACOMBS
    ),
    DUNGEON_REQUESTED_ALREADY(
        """You have already requested a server! Please wait a bit.""",
        MessageCategory.DUNGEON,
    ),
    SELECTED_CLASS_MESSAGE(
        """You have selected the [\s\S]* Dungeon Class!""",
        MessageCategory.DUNGEON,
    ),

    // Mining
    WIND_CHANGED_DIRECTION(
        """The wind has changed direction!""",
        MessageCategory.MINING,
    ),
    EVENT_STARTING(
        """[\s\S]{3}The ([\s\S]*) event (starts in 20 seconds!)[\s\S]*""",
        MessageCategory.MINING,
        ToastData(4000F, "{1}", "{2}", Items.CLOCK.defaultInstance),
    ),
    FALLEN_STAR(
        """✯ A (Fallen Star) has (crashed at [\s\S]*)""",
        MessageCategory.MINING,
        ToastData(4000F, "{1}", "{2}", Items.DIAMOND.defaultInstance),
        SkyBlockIsland.DWARVEN_MINES
    ),
    MONOLITH(
        """(MONOLITH!) You found a mysterious Dark Monolith and were rewarded ([\s\S]*)!""",
        MessageCategory.MINING,
        ToastData(4000F, "{1}", "{2}", Items.DRAGON_EGG.defaultInstance),
        null,
        SkyBlockIsland.DWARVEN_MINES
    ),
    SKYMALL_NEW_DAY(
        "New day! Your Sky Mall buff changed!",
        MessageCategory.MINING
    ),
    SKYMALL(
        """New buff: ([\s\S]*)""",
        MessageCategory.MINING,
        ToastData(4000F, "§9Sky Mall", "{1}"),
    ),
    CHEST_LOCKPICKED(
        """▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬""",
        MessageCategory.MINING, // TODO
        MultilineData("""▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬""", true, true),
        SkyBlockIsland.CRYSTAL_HOLLOWS,
    ),
    TREASURE_CHEST(
        """You uncovered a treasure chest!""",
        MessageCategory.MINING,
        SkyBlockIsland.CRYSTAL_HOLLOWS
    ),
    COMPACT(
        """^COMPACT! You found[\s\S]*""",
        MessageCategory.MINING,
    ),
    ROLLING_MINER(
        """Rolling miner granted you double drops!""",
        MessageCategory.MINING
    ),
    CHEST_ALREADY_LOOTED(
        """This chest has already been looted.""",
        MessageCategory.MINING,
    ),
    NOT_EARNED_CRYSTAL(
        """^You have not found this Crystal yet! Place it here when you have found it!""",
        MessageCategory.MINING,
    ),
    CANNOT_MINE_HERE(
        """A magical force surrounding this area prevents you from breaking blocks![\s\S]*""",
        MessageCategory.MINING,
    ),
    FUEL_REMAINING(
        """Your [\s\S]* has [\s\S]* fuel remaining!""",
        MessageCategory.MINING,
    ),

    // Item
    CANNOT_USE_ITEM(
        """Cannot use this item here!""",
        MessageCategory.ITEM,
    ),
    ABILITY_COOLDOWN(
        """Ability is on cooldown for [\s\S]*""",
        MessageCategory.ITEM,
    ),
    ZOMBIE_SWORD_NO_MORE_CHARGES(
        """No more charges, next one in [\s\S]*""",
        MessageCategory.ITEM,
    ),

    // Miscellaneous
    RINGING(
        """✆ RING...(?![\s\S]*\[PICK UP])[\s\S]*""",
        MessageCategory.MISC,
    ),
    ALREADY_FOUND_FAIRY_SOUL(
        """You have already found that Fairy Soul!""",
        MessageCategory.MISC,
    ),

    // Ironman
    AUCTION_DISALLOWED(
        """You can't use the Auction House while playing on your current profile type!""",
        MessageCategory.IRONMAN,
    ),
    TRADING_DISALLOWED(
        """You can't trade with players while playing on this type of profile!""",
        MessageCategory.IRONMAN,
    ),

    // Fishing
    GOLDEN_FISH_RESISTING(
        """The Golden Fish is resisting...""",
        MessageCategory.FISHING,
    ),
    GOLDEN_FISH_ESCAPES(
        """^The Golden Fish escapes your hook""",
        MessageCategory.FISHING,
    ),

    // Rift
    ALREADY_FOUND_ENIGMA_SOUL(
        """You have already found that Enigma Soul!""",
        MessageCategory.RIFT,
    ),

    // Event
    ALREADY_COLLECTED_EGG(
        """You have already collected this [\s\S]* Egg! Try again when it respawns!""",
        MessageCategory.EVENT
    ),
    EGG_APPEARED(
        """(HOPPITY'S HUNT) (A [\s\S]* has appeared!)""",
        MessageCategory.EVENT,
        ToastData(4000F, "{1}", "{2}"),
    ),
    EGG_FOUND(
        """(HOPPITY'S HUNT) (You found a [\s\S]*!)""",
        MessageCategory.EVENT,
        ToastData(4000F, "{1}", "{2}"),
    ),
    EGG_COLLECTED(
        """(HOPPITY'S HUNT) (You found [\s\S]*!)""",
        MessageCategory.EVENT,
        ToastData(4000F, "{1}", "{2}"),
    ),
    DUPLICATE_RABBIT(
        """(DUPLICATE RABBIT!) ([\s\S]* Chocolate)""",
        MessageCategory.EVENT,
        ToastData(4000F, "{1}", "{2}"),
    ),
    CANNOT_CLAIM_REWARD(
        """^You cannot currently claim this reward!""",
        MessageCategory.EVENT,
    ),
    NO_EGGS_NEARBY(
        """There are no hidden Chocolate Rabbit Eggs nearby! Try again later!""",
        MessageCategory.EVENT,
    ),
    WRONG_TEAM(
        """This person is not on the right team! Feed this cake to someone on the .* Team!""",
        MessageCategory.EVENT,
    ),
    TOO_MUCH_CAKE(
        """This person has had too much cake today!""",
        MessageCategory.EVENT,
    ),
    NOT_A_PLAYER(
        """Click a player on a matching team to feed them this cake! This isn't a player!""",
        MessageCategory.EVENT,
    ),
    NOT_YOUR_CAKE(
        """This Century Cake Slice is for .*, sorry!""",
        MessageCategory.EVENT,
    ),
    ;

    constructor(regex: String, category: MessageCategory, toast: ToastData, vararg islands: SkyBlockIsland) : this(regex, category, toast, null, *islands)
    constructor(regex: String, category: MessageCategory, multilineData: MultilineData?, vararg islands: SkyBlockIsland) : this(regex, category, null, multilineData, *islands)
    constructor(regex: String, category: MessageCategory, vararg islands: SkyBlockIsland) : this(regex, category, null, null, *islands)

    var option: HidingOption = HidingOption.DISABLED

    override fun defaultValue() = HidingOption.DISABLED
    override fun objectType() = HidingOption::class.java
    override fun get() = option

    override fun setEnum(value: Enum<*>?) = runCatching {
        if (!(value == HidingOption.TOAST && this.toast == null)) {
            option = value as HidingOption

        }
    }.isSuccess

    val entryData = DynamicEntryData("config.skyblock.chat.${category.name.lowercase()}.${name.lowercase()}", "")
    override fun options() = entryData

    override fun reset() {
        option = HidingOption.DISABLED
    }

    override fun id() = name
    override fun entry() = this

    val regex: ComponentRegex = ComponentRegex(regex)

    fun option() = option

    companion object {
        fun entriesWithSeparators(): List<ResourcefulConfigElement> {
            return entries.groupBy { it.category }.flatMap { (category, entries) ->
                buildList {
                    add(DynamicSeparator(category.name, ""))
                    addAll(entries)
                }
            }
        }
    }
}
