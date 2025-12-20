package com.epicstore.epicstore.models;

import com.epicstore.epicstore.dtos.BibliotecaItemDTO;
import com.epicstore.epicstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BibliotecaModel {

    public ArrayList<BibliotecaItemDTO> obtenerBibliotecaPorUsuario(int idUsuario) {

        String sql = "SELECT "
                + " bj.id_biblioteca_juego, "
                + " v.id_videojuego, v.titulo, v.imagen_portada, v.precio, "
                + " e.nombre AS empresa, "
                + " bj.tipo_propiedad, bj.estado_instalacion, bj.id_grupo_origen, "
                + " gf.nombre_grupo, "
                + " bj.fecha_ultimo_cambio_estado "
                + "FROM BIBLIOTECA_JUEGO bj "
                + "JOIN VIDEOJUEGO v ON bj.id_videojuego = v.id_videojuego "
                + "JOIN EMPRESA e ON v.id_empresa = e.id_empresa "
                + "LEFT JOIN GRUPO_FAMILIAR gf ON bj.id_grupo_origen = gf.id_grupo "
                + "WHERE bj.id_usuario = ? "
                + "ORDER BY bj.tipo_propiedad DESC, v.titulo ASC";

        ArrayList<BibliotecaItemDTO> lista = new ArrayList<>();
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    BibliotecaItemDTO dto = new BibliotecaItemDTO();
                    dto.setIdBibliotecaJuego(rs.getInt("id_biblioteca_juego"));
                    dto.setIdVideojuego(rs.getInt("id_videojuego"));
                    dto.setTitulo(rs.getString("titulo"));
                    dto.setImagenPortada(rs.getString("imagen_portada"));
                    dto.setPrecio(rs.getDouble("precio"));
                    dto.setEmpresa(rs.getString("empresa"));

                    dto.setTipoPropiedad(rs.getString("tipo_propiedad"));
                    dto.setEstadoInstalacion(rs.getString("estado_instalacion"));

                    int idGrupo = rs.getInt("id_grupo_origen");
                    dto.setIdGrupoOrigen(rs.wasNull() ? null : idGrupo);
                    dto.setNombreGrupoOrigen(rs.getString("nombre_grupo"));

                    if (rs.getTimestamp("fecha_ultimo_cambio_estado") != null) {
                        dto.setFechaUltimoCambioEstado(rs.getTimestamp("fecha_ultimo_cambio_estado").toString());
                    } else {
                        dto.setFechaUltimoCambioEstado(null);
                    }

                    lista.add(dto);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al obtener biblioteca: " + e.getMessage());
        }

        return lista;
    }

    public boolean usuarioExiste(int idUsuario) {
        String sql = "SELECT 1 FROM USUARIO WHERE id_usuario = ? LIMIT 1";
        DBConnection db = new DBConnection();

        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            return false;
        }
    }
}
