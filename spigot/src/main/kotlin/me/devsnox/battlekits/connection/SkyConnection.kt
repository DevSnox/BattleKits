package me.devsnox.battlekits.connection

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Copyright by DevSnox
 * E-Mail: me.devsnox@gmail.com
 * Skype: DevSnox
 */
class SkyConnection {
    private var host: String? = null
    private var port: Int = 0
    private var database: String? = null
    private var username: String? = null
    private var password: String? = null

    private var syncMySQL: SyncMySQL? = null

    private var connection: Connection? = null

    val isConnected: Boolean
        get() = this.connection != null


    constructor(host: String, port: Int, database: String, username: String, password: String) {
        this.host = host
        this.port = port
        this.database = database
        this.username = username
        this.password = password
        connect()
        this.syncMySQL = SyncMySQL(connection)
    }

    constructor(connectionConfig: ConnectionConfig) {
        this.host = connectionConfig.host
        this.port = connectionConfig.port
        this.database = connectionConfig.database
        this.username = connectionConfig.username
        this.password = connectionConfig.password
        connect()
        this.syncMySQL = SyncMySQL(connection)
    }

    fun connect() {
        try {
            this.connection = DriverManager.getConnection(
                "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true",
                this.username,
                this.password
            )
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    fun disconnect() {
        try {
            this.connection!!.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun sync(): SyncMySQL? {
        try {
            if (!this.connection!!.isValid(3)) {
                disconnect()
                connect()
            }
        } catch (e: SQLException) {
            disconnect()
            connect()
        }

        return syncMySQL
    }
}
