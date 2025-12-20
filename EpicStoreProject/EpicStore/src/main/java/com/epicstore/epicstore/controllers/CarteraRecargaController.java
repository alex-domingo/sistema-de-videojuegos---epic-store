package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.RecargaSaldoDTO;
import com.epicstore.epicstore.services.RecargaService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/cartera/recarga")
public class CarteraRecargaController extends HttpServlet {

    private final RecargaService service = new RecargaService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            RecargaSaldoDTO dto = gson.fromJson(request.getReader(), RecargaSaldoDTO.class);
            RecargaService.Resultado r = service.recargar(dto);

            if (r.codigo == RecargaService.OK) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                salida.put("exito", true);
                salida.put("mensaje", r.mensaje);
                salida.put("saldoAnterior", r.saldoAnterior);
                salida.put("saldoNuevo", r.saldoNuevo);
            } else if (r.codigo == RecargaService.MONTO_INVALIDO) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                salida.put("exito", false);
                salida.put("mensaje", r.mensaje);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                salida.put("exito", false);
                salida.put("mensaje", r.mensaje);
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
