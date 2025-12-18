package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.ComentarioDTO;
import com.epicstore.epicstore.models.ComentarioModel;

import java.util.ArrayList;

public class ComentarioService {

    private final ComentarioModel comentarioModel = new ComentarioModel();

    public ArrayList<ComentarioDTO> listarComentarios(int idVideojuego) {
        return comentarioModel.listarPorVideojuego(idVideojuego);
    }
}
