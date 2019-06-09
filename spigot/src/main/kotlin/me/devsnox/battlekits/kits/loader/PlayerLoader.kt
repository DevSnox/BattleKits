package me.devsnox.battlekits.kits.loader

import me.devsnox.battlekits.KitManager
import me.devsnox.battlekits.configs.sqlStatements
import me.devsnox.battlekits.functions.KitsP
import me.devsnox.battlekits.functions.javaPlugin
import me.devsnox.battlekits.kits.entites.KitPlayer
import me.devsnox.battlekits.kits.kit.PlayerKit
import net.darkdevelopers.darkbedrock.darkness.general.configs.ConfigData
import net.darkdevelopers.darkbedrock.darkness.general.databases.mysql.MySQL
import net.darkdevelopers.darkbedrock.darkness.general.functions.toConfigData
import java.sql.PreparedStatement
import java.util.*

class PlayerLoader(
    private val kitManager: KitManager
) {

    private val configData: ConfigData = javaPlugin.dataFolder.toConfigData("mysql")
    private val mySQL = MySQL(configData)
    private val tableName: String get() = sqlStatements.tableName

    init {
        createTable()
    }

    private fun createTable() {
        mySQL.updateSync(sqlStatements.createTable/*.prepared { setString(1, tableName) }*/)
    }

    suspend fun existPlayer(uuid: UUID): Boolean {
        val resultSet = mySQL.preparedQuery(sqlStatements.existsPlayer.prepared {
            setString(1, tableName)
            setString(2, uuid.toString())
        })
        return resultSet.next()
    }

    suspend fun loadPlayer(uuid: UUID): KitPlayer {

        val resultSet = mySQL.preparedQuery(sqlStatements.loadPlayer.prepared {
            setString(1, tableName)
            setString(2, uuid.toString())
        })

        val kits: KitsP = mutableMapOf()

        while (resultSet.next()) {
            val id = resultSet.getInt(sqlStatements.usersId)
            val battleKit = kitManager.kits[id] ?: continue
            kits[id] = PlayerKit(id, battleKit, resultSet.getLong(sqlStatements.usersLastReceive))
        }

        return KitPlayer(uuid, kits)
    }

    suspend fun savePlayer(kitPlayer: KitPlayer) {
        //TODO improve this! NOW!!! YOU ARE A STUPID GUY!!!!! AHHHHHHHHHHHHHHH
        kitPlayer.kits.values.forEach { playerKit ->
            mySQL.preparedUpdate(sqlStatements.createPlayer.prepared {
                setString(1, tableName)
                setString(2, kitPlayer.uuid.toString())
                setInt(3, playerKit.id)
                setLong(4, playerKit.lastReceive)
            })
        }
    }

    suspend fun deletePlayer(uuid: UUID): Unit = mySQL.preparedUpdate(sqlStatements.deletePlayer.prepared {
        setString(1, tableName)
        setString(2, uuid.toString())
    })

    private fun String.prepared(change: PreparedStatement.() -> Unit): PreparedStatement {
        val preparedStatement = mySQL.preparedStatement(this).save()
        preparedStatement.change()
        return preparedStatement
    }

    private fun PreparedStatement?.save(): PreparedStatement =
        this ?: throw IllegalStateException("mySQL.connection can not be null")
}
