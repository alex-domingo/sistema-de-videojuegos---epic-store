package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.VideojuegoDTO;
import com.epicstore.epicstore.dtos.VideojuegoDetalleDTO;
import com.epicstore.epicstore.models.VideojuegoModel;

import java.util.ArrayList;

public class VideojuegoService {

    private final VideojuegoModel videojuegoModel = new VideojuegoModel();

    public ArrayList<VideojuegoDTO> obtenerVideojuegos() {
        return videojuegoModel.listarVideojuegos();
    }

    public VideojuegoDetalleDTO obtenerDetalle(int idVideojuego) {
        return videojuegoModel.obtenerDetallePorId(idVideojuego);
    }

}
