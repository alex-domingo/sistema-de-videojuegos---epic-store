package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.CrearGrupoDTO;
import com.epicstore.epicstore.models.GrupoModel;

public class GrupoService {

    private final GrupoModel model = new GrupoModel();

    public static final int OK = 1;
    public static final int ERROR = -1;
    public static final int DUENO_NO_EXISTE = -2;

    public static class Resultado {

        public int codigo;
        public String mensaje;
        public Integer idGrupo;
    }

    public Resultado crearGrupo(CrearGrupoDTO dto) {

        Resultado r = new Resultado();

        if (dto == null || dto.getIdDueno() <= 0 || dto.getNombreGrupo() == null || dto.getNombreGrupo().trim().isEmpty()) {
            r.codigo = ERROR;
            r.mensaje = "Datos inválidos para crear grupo";
            return r;
        }

        try {
            int id = model.crearGrupo(dto.getIdDueno(), dto.getNombreGrupo().trim());

            if (id == DUENO_NO_EXISTE) {
                r.codigo = DUENO_NO_EXISTE;
                r.mensaje = "El dueño no existe";
                return r;
            }

            if (id > 0) {
                r.codigo = OK;
                r.mensaje = "Grupo creado correctamente";
                r.idGrupo = id;
                return r;
            }

            r.codigo = ERROR;
            r.mensaje = "No se pudo crear el grupo";
            return r;

        } catch (Exception e) {
            r.codigo = ERROR;
            r.mensaje = "Error: " + e.getMessage();
            return r;
        }
    }
}
