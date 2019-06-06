package me.devsnox.battlekits.kits.entites

import me.devsnox.battlekits.functions.KitsP
import me.devsnox.battlekits.kits.kit.BattleKit
import me.devsnox.battlekits.kits.kit.PlayerKit
import java.util.*

class KitPlayer(val uuid: UUID, val kits: KitsP) {

    fun addKit(battleKit: BattleKit) {
        kits[battleKit.id] = PlayerKit(battleKit.id, battleKit, 0)
    }

    fun removeKit(battleKit: BattleKit) {
        kits -= battleKit.id
    }
}
