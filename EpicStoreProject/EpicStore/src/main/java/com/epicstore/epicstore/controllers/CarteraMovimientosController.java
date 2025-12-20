package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.MovimientoSaldoDTO;
import com.epicstore.epicstore.services.CarteraService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet("/api/cartera/movimientos")
public class CarteraMovimientosController extends HttpServlet {

    private final CarteraService carteraService = new CarteraService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        String idParam = request.getParameter("idUsuario");

        try (PrintWriter out = response.getWriter()) {

            if (idParam == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                salida.put("exito", false);
                salida.put("mensaje", "Falta el parámetro idUsuario");
                out.print(gson.toJson(salida));
                return;
            }

            int idUsuario = Integer.parseInt(idParam);

            if (!carteraService.validarUsuario(idUsuario)) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                salida.put("exito", false);
                salida.put("mensaje", "El usuario no existe");
                out.print(gson.toJson(salida));
                return;
            }

            double saldo = carteraService.obtenerSaldo(idUsuario);
            ArrayList<MovimientoSaldoDTO> movimientos = carteraService.obtenerMovimientos(idUsuario);

            salida.put("exito", true);
            salida.put("mensaje", "Movimientos de cartera del usuario");
            salida.put("saldoActual", saldo);
            salida.put("totalMovimientos", movimientos.size());
            salida.put("movimientos", movimientos);

            out.print(gson.toJson(salida));
            out.flush();

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            salida.put("exito", false);
            salida.put("mensaje", "El parámetro idUsuario debe ser numérico");
        }
    }
}
