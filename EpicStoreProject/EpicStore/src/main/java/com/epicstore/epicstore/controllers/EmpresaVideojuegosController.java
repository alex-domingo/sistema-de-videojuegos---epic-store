package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.EditarVideojuegoDTO;
import com.epicstore.epicstore.dtos.PublicarVideojuegoDTO;
import com.epicstore.epicstore.services.VideojuegoEmpresaService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/empresa/videojuegos")
public class EmpresaVideojuegosController extends HttpServlet {

    private final VideojuegoEmpresaService service = new VideojuegoEmpresaService();

    private Integer obtenerIdEmpresaSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        Object tipo = session.getAttribute("tipoSesion");
        if (tipo == null || !"EMPRESA".equals(tipo.toString())) {
            return null;
        }

        return (Integer) session.getAttribute("idEmpresa");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            Integer idEmpresa = obtenerIdEmpresaSesion(request);
            if (idEmpresa == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                salida.put("exito", false);
                salida.put("mensaje", "No autenticado como empresa");
                out.print(gson.toJson(salida));
                return;
            }

            VideojuegoEmpresaService.Resultado r = service.listar(idEmpresa);

            response.setStatus(HttpServletResponse.SC_OK);
            salida.put("exito", r.exito);
            salida.put("mensaje", r.mensaje);
            salida.put("datos", r.datos);

            out.print(gson.toJson(salida));
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            Integer idEmpresa = obtenerIdEmpresaSesion(request);
            if (idEmpresa == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                salida.put("exito", false);
                salida.put("mensaje", "No autenticado como empresa");
                out.print(gson.toJson(salida));
                return;
            }

            PublicarVideojuegoDTO dto = gson.fromJson(request.getReader(), PublicarVideojuegoDTO.class);
            VideojuegoEmpresaService.Resultado r = service.publicar(idEmpresa, dto);

            if (r.exito) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                salida.put("exito", true);
                salida.put("mensaje", r.mensaje);
                salida.put("idVideojuego", r.datos);
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

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            Integer idEmpresa = obtenerIdEmpresaSesion(request);
            if (idEmpresa == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                salida.put("exito", false);
                salida.put("mensaje", "No autenticado como empresa");
                out.print(gson.toJson(salida));
                return;
            }

            String idParam = request.getParameter("id");
            if (idParam == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                salida.put("exito", false);
                salida.put("mensaje", "Falta parámetro id");
                out.print(gson.toJson(salida));
                return;
            }

            int idVideojuego = Integer.parseInt(idParam);

            EditarVideojuegoDTO dto = gson.fromJson(request.getReader(), EditarVideojuegoDTO.class);
            VideojuegoEmpresaService.Resultado r = service.editar(idEmpresa, idVideojuego, dto);

            if (r.exito) {
                response.setStatus(HttpServletResponse.SC_OK);
                salida.put("exito", true);
                salida.put("mensaje", r.mensaje);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                salida.put("exito", false);
                salida.put("mensaje", r.mensaje);
            }

            out.print(gson.toJson(salida));
            out.flush();

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
