package com.epicstore.epicstore.models;

import com.epicstore.epicstore.dtos.EmpresaPublicaDTO;
import com.epicstore.epicstore.dtos.VideojuegoPublicoDTO;
import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class EmpresaPublicaModel {

    public EmpresaPublicaDTO obtenerEmpresa(int idEmpresa) {

        String sql = "SELECT id_empresa, nombre, descripcion, comentarios_visibles_global "
                + "FROM EMPRESA WHERE id_empresa = ?";

        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    EmpresaPublicaDTO e = new EmpresaPublicaDTO();
                    e.setIdEmpresa(rs.getInt("id_empresa"));
                    e.setNombre(rs.getString("nombre"));
                    e.setDescripcion(rs.getString("descripcion"));
                    e.setComentariosVisiblesGlobal(rs.getString("comentarios_visibles_global"));
                    return e;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener empresa pública: " + e.getMessage());
        }
        return null;
    }

    public ArrayList<VideojuegoPublicoDTO> listarCatalogo(int idEmpresa) {

        String sql
                = "SELECT v.id_videojuego, v.titulo, v.precio, v.venta_activa, "
                + "       (SELECT AVG(c.calificacion) FROM COMENTARIO c "
                + "        WHERE c.id_videojuego = v.id_videojuego AND c.visible = 'S') AS promedio "
                + "FROM VIDEOJUEGO v "
                + "WHERE v.id_empresa = ? "
                + "ORDER BY v.titulo ASC";

        ArrayList<VideojuegoPublicoDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VideojuegoPublicoDTO v = new VideojuegoPublicoDTO();
                    v.setIdVideojuego(rs.getInt("id_videojuego"));
                    v.setTitulo(rs.getString("titulo"));
                    v.setPrecio(rs.getDouble("precio"));
                    v.setVentaActiva(rs.getString("venta_activa"));

                    Object prom = rs.getObject("promedio");
                    v.setCalificacionPromedio(prom != null ? rs.getDouble("promedio") : null);

                    lista.add(v);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar catálogo público: " + e.getMessage());
        }
        return lista;
    }
}
