package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.ComentarioDTO;
import com.epicstore.epicstore.dtos.CrearComentarioDTO;
import com.epicstore.epicstore.models.ComentarioModel;

import java.util.ArrayList;

public class ComentarioService {

    private final ComentarioModel comentarioModel = new ComentarioModel();

    public ArrayList<ComentarioDTO> listarComentarios(int idVideojuego) {
        return comentarioModel.listarPorVideojuego(idVideojuego);
    }

    public int crearComentario(CrearComentarioDTO dto) {

        // Validaciones básicas
        if (dto.getCalificacion() < 1 || dto.getCalificacion() > 5) {
            return -2; // Calificación inválida
        }

        // Regla del negocio: solo si compró
        boolean compro = comentarioModel.usuarioComproVideojuego(dto.getIdUsuario(), dto.getIdVideojuego());
        if (!compro) {
            return -3; // No compró, no puede comentar
        }

        return comentarioModel.insertarComentario(dto); // Devuelve id generado o -1
    }

}
