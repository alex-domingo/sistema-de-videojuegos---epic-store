package com.epicstore.epicstore.models;

import com.epicstore.epicstore.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HealthModel {

    public int contarUsuarios() {
        String sql = "SELECT COUNT(*) AS total FROM USUARIO";
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;

        } catch (Exception e) {
            System.err.println("Error al contar usuarios: " + e.getMessage());
            return -1; // indica error
        }
    }
}
