/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package me.devsnox.battlekits.configs

import me.devsnox.battlekits.functions.javaPlugin
import net.darkdevelopers.darkbedrock.darkness.general.configs.default
import net.darkdevelopers.darkbedrock.darkness.general.configs.getValue
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.provider
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.register
import org.bukkit.plugin.ServicesManager

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 07.06.2019 18:21.
 * Last edit 07.06.2019
 */
class SQLStatements(values: Map<String, Any?>) {
    val tableName: String by values.default { "battlekits_users" }
    @Suppress("MemberVisibilityCanBePrivate")
    val usersUuid: String by values.default { "uuid" }
    val usersId: String by values.default { "id" }
    val usersLastReceive: String by values.default { "last_receive" }
    val createTable: String by values.default {
        "CREATE TABLE IF NOT EXISTS `$tableName` (" +
                "$usersUuid VARCHAR(36) NOT NULL, " +
                "$usersId INT NOT NULL, " +
                "$usersLastReceive BIGINT, " +
                "" +
                "PRIMARY KEY (uuid), " +
                "" +
                "start_timestamp TIMESTAMP(9) GENERATED ALWAYS AS ROW START, " +
                "end_timestamp TIMESTAMP(9) GENERATED ALWAYS AS ROW END, " +
                "" +
                "PERIOD FOR SYSTEM TIME (start_timestamp, end_timestamp)" +
                ") WITH SYSTEM VERSIONING"
    }
    val existsPlayer: String by values.default { "SELECT `$usersUuid` FROM ? WHERE $usersUuid='?'" }
    val loadPlayer: String by values.default { "SELECT `*` FROM ? WHERE $usersUuid='?'" }
    val deletePlayer: String by values.default { "DELETE `*` FROM ? WHERE $usersUuid='?'" }
    val createPlayer: String by values.default { "INSERT INTO `?` ($usersUuid, $usersId, $usersLastReceive) VALUES('?', '?', '?')" }
}

private val servicesManager: ServicesManager get() = javaPlugin.server.servicesManager

var sqlStatements: SQLStatements
    get() = servicesManager.provider()!!
    set(value) {
        servicesManager.unregister(servicesManager.provider())
        servicesManager.register(value, javaPlugin)
    }
