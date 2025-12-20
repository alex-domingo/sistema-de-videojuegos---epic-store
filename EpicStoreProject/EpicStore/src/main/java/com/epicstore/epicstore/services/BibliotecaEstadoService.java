package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.ActualizarEstadoBibliotecaDTO;
import com.epicstore.epicstore.models.BibliotecaEstadoModel;

import java.sql.Connection;

public class BibliotecaEstadoService {

    private final BibliotecaEstadoModel model = new BibliotecaEstadoModel();

    public static final int OK = 1;
    public static final int ERROR_DATOS = -1;
    public static final int NO_EXISTE_EN_BIBLIOTECA = -2;
    public static final int REGLA_PRESTAMO = -3;

    public static class Resultado {

        public int codigo;
        public String mensaje;
    }

    public Resultado actualizarEstado(ActualizarEstadoBibliotecaDTO dto) {

        Resultado r = new Resultado();

        if (dto == null || dto.getIdUsuario() <= 0 || dto.getIdVideojuego() <= 0 || dto.getNuevoEstado() == null) {
            r.codigo = ERROR_DATOS;
            r.mensaje = "Datos incompletos";
            return r;
        }

        String nuevoEstado = dto.getNuevoEstado().trim().toUpperCase();
        if (!nuevoEstado.equals("INSTALADO") && !nuevoEstado.equals("NO_INSTALADO")) {
            r.codigo = ERROR_DATOS;
            r.mensaje = "nuevoEstado inválido (usa INSTALADO o NO_INSTALADO)";
            return r;
        }

        try (Connection conn = model.abrirConexion()) {

            // Debe existir en biblioteca
            if (!model.existeRegistroBiblioteca(conn, dto.getIdUsuario(), dto.getIdVideojuego())) {
                r.codigo = NO_EXISTE_EN_BIBLIOTECA;
                r.mensaje = "El videojuego no existe en la biblioteca del usuario";
                return r;
            }

            // Validar regla: max 1 prestado INSTALADO
            String tipo = model.obtenerTipoPropiedad(conn, dto.getIdUsuario(), dto.getIdVideojuego());

            if ("PRESTADO".equalsIgnoreCase(tipo) && "INSTALADO".equals(nuevoEstado)) {
                int prestadosInstalados = model.contarPrestadosInstalados(conn, dto.getIdUsuario());

                // Si ya tiene 1 prestado instalado, no permitimos instalar otro
                if (prestadosInstalados >= 1) {
                    r.codigo = REGLA_PRESTAMO;
                    r.mensaje = "No puedes tener más de 1 videojuego prestado instalado al mismo tiempo";
                    return r;
                }
            }

            int filas = model.actualizarEstado(conn, dto.getIdUsuario(), dto.getIdVideojuego(), nuevoEstado);

            if (filas > 0) {
                r.codigo = OK;
                r.mensaje = "Estado de instalación actualizado correctamente";
            } else {
                r.codigo = ERROR_DATOS;
                r.mensaje = "No se pudo actualizar el estado";
            }

            return r;

        } catch (Exception e) {
            r.codigo = ERROR_DATOS;
            r.mensaje = "Error: " + e.getMessage();
            return r;
        }
    }
}
