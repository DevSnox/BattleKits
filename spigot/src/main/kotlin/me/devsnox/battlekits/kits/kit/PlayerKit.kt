package me.devsnox.battlekits.kits.kit

import java.util.concurrent.TimeUnit

class PlayerKit(
    val id: Int,
    val battleKit: BattleKit,
    var lastReceive: Long
) {
    fun checkReceive(): Boolean = lastReceive + TimeUnit.DAYS.toMillis(7) >= System.currentTimeMillis()
}
