package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.BibliotecaItemDTO;
import com.epicstore.epicstore.models.BibliotecaModel;

import java.util.ArrayList;

public class BibliotecaService {

    private final BibliotecaModel bibliotecaModel = new BibliotecaModel();

    public ArrayList<BibliotecaItemDTO> obtenerBiblioteca(int idUsuario) {
        return bibliotecaModel.obtenerBibliotecaPorUsuario(idUsuario);
    }

    public boolean validarUsuario(int idUsuario) {
        return bibliotecaModel.usuarioExiste(idUsuario);
    }
}
