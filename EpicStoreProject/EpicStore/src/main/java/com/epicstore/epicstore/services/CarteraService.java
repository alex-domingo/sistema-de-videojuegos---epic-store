package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.MovimientoSaldoDTO;
import com.epicstore.epicstore.models.CarteraModel;

import java.util.ArrayList;

public class CarteraService {

    private final CarteraModel carteraModel = new CarteraModel();

    public boolean validarUsuario(int idUsuario) {
        return carteraModel.usuarioExiste(idUsuario);
    }

    public double obtenerSaldo(int idUsuario) {
        return carteraModel.obtenerSaldoActual(idUsuario);
    }

    public ArrayList<MovimientoSaldoDTO> obtenerMovimientos(int idUsuario) {
        return carteraModel.listarMovimientos(idUsuario);
    }
}
