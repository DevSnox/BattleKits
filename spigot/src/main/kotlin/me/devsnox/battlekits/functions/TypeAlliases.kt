package me.devsnox.battlekits.functions

import me.devsnox.battlekits.kits.entites.KitPlayer
import me.devsnox.battlekits.kits.kit.BattleKit
import me.devsnox.battlekits.kits.kit.PlayerKit
import org.bukkit.entity.Player
import java.util.*

/*
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 06.06.2019 16:46.
 * Last edit 06.06.2019
 */

typealias OpeningChache = MutableList<Player>
typealias PlayerCache = MutableMap<UUID, KitPlayer>
typealias KitsP = MutableMap<Int, PlayerKit>
typealias KitsB = MutableMap<Int, BattleKit>
