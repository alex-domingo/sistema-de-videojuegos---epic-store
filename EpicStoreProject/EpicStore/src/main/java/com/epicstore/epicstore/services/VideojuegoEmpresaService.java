package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.CambiarVentaVideojuegoDTO;
import com.epicstore.epicstore.dtos.EditarVideojuegoDTO;
import com.epicstore.epicstore.dtos.PublicarVideojuegoDTO;
import com.epicstore.epicstore.dtos.VideojuegoEmpresaDTO;
import com.epicstore.epicstore.models.VideojuegoEmpresaModel;

import java.util.ArrayList;

public class VideojuegoEmpresaService {

    public static class Resultado {

        public boolean exito;
        public String mensaje;
        public Object datos;

        public Resultado(boolean exito, String mensaje) {
            this.exito = exito;
            this.mensaje = mensaje;
        }

        public Resultado(boolean exito, String mensaje, Object datos) {
            this.exito = exito;
            this.mensaje = mensaje;
            this.datos = datos;
        }
    }

    private final VideojuegoEmpresaModel model = new VideojuegoEmpresaModel();

    public Resultado listar(int idEmpresa) {
        ArrayList<VideojuegoEmpresaDTO> lista = model.listarVideojuegosPorEmpresa(idEmpresa);
        return new Resultado(true, "Listado de videojuegos de la empresa", lista);
    }

    public Resultado publicar(int idEmpresa, PublicarVideojuegoDTO dto) {
        if (dto == null) {
            return new Resultado(false, "JSON inválido");
        }

        if (dto.getIdClasificacion() == null
                || dto.getTitulo() == null || dto.getTitulo().trim().isEmpty()
                || dto.getDescripcion() == null || dto.getDescripcion().trim().isEmpty()
                || dto.getPrecio() == null) {
            return new Resultado(false, "Datos incompletos");
        }

        if (dto.getPrecio() <= 0) {
            return new Resultado(false, "El precio debe ser mayor a 0");
        }

        dto.setTitulo(dto.getTitulo().trim());
        dto.setDescripcion(dto.getDescripcion().trim());

        if (!model.existeClasificacion(dto.getIdClasificacion())) {
            return new Resultado(false, "La clasificación indicada no existe");
        }

        if (model.existeTituloEnEmpresa(idEmpresa, dto.getTitulo())) {
            return new Resultado(false, "Ya existe un videojuego con ese título en tu empresa");
        }

        Integer idNuevo = model.publicarVideojuego(idEmpresa, dto);
        if (idNuevo == null) {
            return new Resultado(false, "No se pudo publicar el videojuego");
        }

        return new Resultado(true, "Videojuego publicado correctamente", idNuevo);
    }

    public Resultado editar(int idEmpresa, int idVideojuego, EditarVideojuegoDTO dto) {

        if (dto == null) {
            return new Resultado(false, "JSON inválido");
        }

        if (dto.getIdClasificacion() == null
                || dto.getTitulo() == null || dto.getTitulo().trim().isEmpty()
                || dto.getDescripcion() == null || dto.getDescripcion().trim().isEmpty()
                || dto.getPrecio() == null) {
            return new Resultado(false, "Datos incompletos");
        }

        if (dto.getPrecio() <= 0) {
            return new Resultado(false, "El precio debe ser mayor a 0");
        }

        dto.setTitulo(dto.getTitulo().trim());
        dto.setDescripcion(dto.getDescripcion().trim());

        if (!model.existeVideojuegoEnEmpresa(idEmpresa, idVideojuego)) {
            return new Resultado(false, "No puedes editar este videojuego (no existe o no pertenece a tu empresa)");
        }

        if (!model.existeClasificacion(dto.getIdClasificacion())) {
            return new Resultado(false, "La clasificación indicada no existe");
        }

        if (model.existeTituloEnEmpresaExcluyendo(idEmpresa, dto.getTitulo(), idVideojuego)) {
            return new Resultado(false, "Ya existe otro videojuego con ese título en tu empresa");
        }

        boolean ok = model.editarVideojuego(idEmpresa, idVideojuego, dto);
        if (!ok) {
            return new Resultado(false, "No se pudo editar el videojuego");
        }

        return new Resultado(true, "Videojuego editado correctamente");
    }

    public Resultado cambiarVenta(int idEmpresa, CambiarVentaVideojuegoDTO dto) {

        if (dto == null) {
            return new Resultado(false, "JSON inválido");
        }
        if (dto.getIdVideojuego() == null || dto.getVentaActiva() == null) {
            return new Resultado(false, "Datos incompletos");
        }

        String estado = dto.getVentaActiva().trim().toUpperCase();
        if (!"S".equals(estado) && !"N".equals(estado)) {
            return new Resultado(false, "ventaActiva debe ser 'S' o 'N'");
        }

        int idVideojuego = dto.getIdVideojuego();

        if (!model.existeVideojuegoEnEmpresa(idEmpresa, idVideojuego)) {
            return new Resultado(false, "No puedes modificar este videojuego (no existe o no pertenece a tu empresa)");
        }

        boolean ok = model.cambiarVenta(idEmpresa, idVideojuego, estado);
        if (!ok) {
            return new Resultado(false, "No se pudo actualizar el estado de venta");
        }

        return new Resultado(true, "Estado de venta actualizado correctamente");
    }
}
