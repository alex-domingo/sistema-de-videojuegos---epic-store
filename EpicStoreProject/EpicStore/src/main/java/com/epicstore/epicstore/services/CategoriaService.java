package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.CategoriaDTO;
import com.epicstore.epicstore.models.CategoriaModel;

import java.util.ArrayList;

public class CategoriaService {

    private final CategoriaModel categoriaModel = new CategoriaModel();

    public ArrayList<CategoriaDTO> listarCategorias() {
        return categoriaModel.obtenerCategorias();
    }
}
