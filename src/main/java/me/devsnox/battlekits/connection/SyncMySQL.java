package me.devsnox.battlekits.connection;

import java.sql.*;

/**
 * Copyright by DevSnox
 * E-Mail: me.devsnox@gmail.com
 * Skype: DevSnox
 */
public class SyncMySQL {

    private Connection connection;

    public SyncMySQL(Connection connection) {
        this.connection = connection;
    }

    public void update(String statment) {
        if(statment != null) {
            try {
                Statement st = this.connection.createStatement();
                st.executeUpdate(statment);
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public void preparedUpdate(String statment) {
        if(statment != null) {
            try {
                PreparedStatement st = this.connection.prepareStatement(statment);
                st.executeUpdate(statment);
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public void preparedUpdate(PreparedStatement statment) {
        if(statment != null) {
            try {
                statment.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public ResultSet query(String statment) {
        if(connection != null) {
            if(statment == null) {
                return null;
            } else {
                ResultSet resultSet = null;

                try {
                    Statement st = this.connection.createStatement();
                    resultSet = st.executeQuery(statment);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return resultSet;
            }
        }
        return null;
    }
}
