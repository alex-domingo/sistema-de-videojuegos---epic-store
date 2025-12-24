package com.epicstore.epicstore.services;

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
}
