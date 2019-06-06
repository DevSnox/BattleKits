package me.devsnox.battlekits.connection

import net.darkdevelopers.darkbedrock.darkness.general.configs.default
import net.darkdevelopers.darkbedrock.darkness.general.configs.getValue

class ConnectionConfig(values: Map<String, Any?>) {
    val host: String by values.default { "localhost" }
    val port: Int by values.default { 3306 }
    val database: String by values.default { "Minecraft" }
    val username: String by values.default { "root" }
    val password: String by values.default { "root" }
}
