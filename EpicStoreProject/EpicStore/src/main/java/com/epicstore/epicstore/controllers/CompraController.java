package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.CrearCompraDTO;
import com.epicstore.epicstore.services.CompraService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/compras")
public class CompraController extends HttpServlet {

    private final CompraService compraService = new CompraService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            CrearCompraDTO dto = gson.fromJson(request.getReader(), CrearCompraDTO.class);
            CompraService.ResultadoCompra res = compraService.realizarCompra(dto);

            if (res.codigo == CompraService.OK) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                salida.put("exito", true);
                salida.put("mensaje", res.mensaje);
                salida.put("datos", res.data);
            } else if (res.codigo == CompraService.YA_COMPRADO) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                salida.put("exito", false);
                salida.put("mensaje", res.mensaje);
            } else if (res.codigo == CompraService.EDAD_NO_PERMITIDA) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                salida.put("exito", false);
                salida.put("mensaje", res.mensaje);
            } else if (res.codigo == CompraService.SALDO_INSUFICIENTE) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                salida.put("exito", false);
                salida.put("mensaje", res.mensaje);
            } else if (res.codigo == CompraService.JUEGO_NO_EN_VENTA) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                salida.put("exito", false);
                salida.put("mensaje", res.mensaje);
            } else if (res.codigo == CompraService.NO_EXISTE_USUARIO || res.codigo == CompraService.NO_EXISTE_JUEGO) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                salida.put("exito", false);
                salida.put("mensaje", res.mensaje);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                salida.put("exito", false);
                salida.put("mensaje", res.mensaje);
            }

            out.print(gson.toJson(salida));
            out.flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            salida.put("exito", false);
            salida.put("mensaje", "JSON inválido o datos incompletos");
        }
    }
}
