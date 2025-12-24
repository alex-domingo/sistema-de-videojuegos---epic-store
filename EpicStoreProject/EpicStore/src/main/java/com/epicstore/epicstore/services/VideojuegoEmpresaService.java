package com.epicstore.epicstore.services;

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
}
