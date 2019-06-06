package me.devsnox.battlekits.connection

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Copyright by DevSnox
 * E-Mail: me.devsnox@gmail.com
 * Skype: DevSnox
 */
class SyncMySQL(private val connection: Connection?) {

    fun update(statment: String?) {
        if (statment != null) {
            try {
                val st = connection!!.createStatement()
                st.executeUpdate(statment)
                st.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }

        }
    }

    fun preparedUpdate(statment: String?) {
        if (statment != null) {
            try {
                val st = connection!!.prepareStatement(statment)
                st.executeUpdate(statment)
                st.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }

        }
    }

    fun preparedUpdate(statment: PreparedStatement?) {
        if (statment != null) {
            try {
                statment.executeUpdate()
            } catch (e: SQLException) {
                e.printStackTrace()
            }

        }
    }

    fun query(statment: String?): ResultSet? {
        connection ?: return null
        statment ?: return null
        var resultSet: ResultSet? = null

        try {
            val st = connection.createStatement()
            resultSet = st.executeQuery(statment)
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return resultSet
    }
}
