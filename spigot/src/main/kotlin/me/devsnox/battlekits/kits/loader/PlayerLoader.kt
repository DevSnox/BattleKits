package me.devsnox.battlekits.kits.loader

import com.google.gson.JsonObject
import me.devsnox.battlekits.KitManager
import me.devsnox.battlekits.connection.ConnectionConfig
import me.devsnox.battlekits.connection.SkyConnection
import me.devsnox.battlekits.connection.SyncMySQL
import me.devsnox.battlekits.kits.entites.KitPlayer
import me.devsnox.battlekits.kits.kit.PlayerKit
import net.darkdevelopers.darkbedrock.darkness.general.configs.toConfigMap
import net.darkdevelopers.darkbedrock.darkness.general.functions.load
import net.darkdevelopers.darkbedrock.darkness.general.functions.save
import net.darkdevelopers.darkbedrock.darkness.general.functions.toConfigData
import net.darkdevelopers.darkbedrock.darkness.general.functions.toMap
import org.bukkit.plugin.Plugin
import java.sql.SQLException
import java.util.*

class PlayerLoader(private val kitManager: KitManager) {
    private val plugin: Plugin = kitManager.plugin

    private val table: String = "battlekits_users"

    private val skyConnection: SkyConnection
    private val connection: SyncMySQL?

    init {

        val configData = plugin.dataFolder.toConfigData("mysql")
        val map = configData.load<JsonObject>().toMap()
        val connectionConfig = ConnectionConfig(map)
        configData.save(connectionConfig.toConfigMap())

        this.skyConnection = SkyConnection(connectionConfig)
        this.skyConnection.connect()
        this.connection = this.skyConnection.sync()

        this.connection!!.update("CREATE TABLE IF NOT EXISTS $table (UUID VARCHAR(36), ID INT, LAST_RECEIVE BIGINT)")
    }

    fun existPlayer(uuid: UUID): Boolean {
        val resultSet = this.connection!!.query("SELECT * FROM $table WHERE UUID='$uuid'")
        try {
            if (resultSet!!.next()) {
                return true
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return false
    }

    fun removePlayer(uuid: UUID) {
        connection!!.update("DELETE * FROM $table WHERE UUID='$uuid'")
    }

    fun loadPlayer(uuid: UUID): KitPlayer {
        val resultSet = this.connection!!.query("SELECT * FROM " + this.table + " WHERE UUID='" + uuid + "'")

        val rawKits = this.kitManager.kits

        val kits = HashMap<Int, PlayerKit>()

        try {
            while (resultSet!!.next()) {
                val id = resultSet.getInt("id")
                if (rawKits.containsKey(id)) {
                    kits[resultSet.getInt("ID")] =
                        PlayerKit(resultSet.getInt("ID"), rawKits[id]!!, resultSet.getLong("LAST_RECEIVE"))
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return KitPlayer(uuid, kits)
    }

    fun savePlayer(kitPlayer: KitPlayer) {
        for (playerKit in kitPlayer.kits.values) {
            connection!!.update("INSERT INTO " + this.table + "(UUID, ID, LAST_RECEIVE) VALUES('" + kitPlayer.uuid + "', '" + playerKit.id + "', '" + playerKit.lastReceive + "')")
        }
    }
}
