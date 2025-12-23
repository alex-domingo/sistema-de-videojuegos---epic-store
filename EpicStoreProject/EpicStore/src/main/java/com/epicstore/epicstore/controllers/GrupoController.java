package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.CrearGrupoDTO;
import com.epicstore.epicstore.services.GrupoService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/grupos")
public class GrupoController extends HttpServlet {

    private final GrupoService service = new GrupoService();

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
            var lista = service.listarGrupos(idUsuario);

            response.setStatus(HttpServletResponse.SC_OK);
            salida.put("exito", true);
            salida.put("mensaje", "Grupos del usuario");
            salida.put("total", lista.size());
            salida.put("datos", lista);

            out.print(gson.toJson(salida));
            out.flush();

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            salida.put("exito", false);
            salida.put("mensaje", "El parámetro idUsuario debe ser numérico");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            CrearGrupoDTO dto = gson.fromJson(request.getReader(), CrearGrupoDTO.class);
            GrupoService.Resultado r = service.crearGrupo(dto);

            if (r.codigo == GrupoService.OK) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                salida.put("exito", true);
                salida.put("mensaje", r.mensaje);
                salida.put("idGrupo", r.idGrupo);
            } else if (r.codigo == GrupoService.DUENO_NO_EXISTE) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
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
        }
    }
}
