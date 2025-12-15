package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.HealthDTO;
import com.epicstore.epicstore.models.HealthModel;

public class HealthService {

    private final HealthModel healthModel = new HealthModel();

    public HealthDTO obtenerEstado() {
        int total = healthModel.contarUsuarios();

        if (total >= 0) {
            return new HealthDTO("OK", "tienda_videojuegos", total);
        }
        return new HealthDTO("ERROR", "tienda_videojuegos", 0);
    }
}
