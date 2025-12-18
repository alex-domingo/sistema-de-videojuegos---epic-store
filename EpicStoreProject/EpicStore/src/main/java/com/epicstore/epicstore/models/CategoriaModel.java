package com.epicstore.epicstore.models;

import com.epicstore.epicstore.dtos.CategoriaDTO;
import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CategoriaModel {

    public ArrayList<CategoriaDTO> obtenerCategorias() {
        String sql = "SELECT id_categoria, nombre, descripcion FROM CATEGORIA ORDER BY nombre ASC";
        ArrayList<CategoriaDTO> categorias = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CategoriaDTO dto = new CategoriaDTO();
                dto.setIdCategoria(rs.getInt("id_categoria"));
                dto.setNombre(rs.getString("nombre"));
                dto.setDescripcion(rs.getString("descripcion"));
                categorias.add(dto);
            }

        } catch (Exception e) {
            System.err.println("Error al obtener categorías: " + e.getMessage());
        }

        return categorias;
    }
}
