package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.CrearUsuarioEmpresaDTO;
import com.epicstore.epicstore.services.UsuarioEmpresaService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/empresa/usuarios")
public class EmpresaUsuariosController extends HttpServlet {

    private final UsuarioEmpresaService service = new UsuarioEmpresaService();

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

            UsuarioEmpresaService.Resultado r = service.listar(idEmpresa);

            salida.put("exito", true);
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

            CrearUsuarioEmpresaDTO dto = gson.fromJson(request.getReader(), CrearUsuarioEmpresaDTO.class);
            UsuarioEmpresaService.Resultado r = service.crear(idEmpresa, dto);

            if (r.codigo == UsuarioEmpresaService.OK) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                salida.put("exito", true);
                salida.put("mensaje", r.mensaje);
                salida.put("idUsuarioEmpresa", r.datos);
            } else if (r.codigo == UsuarioEmpresaService.CORREO_YA_EXISTE) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {

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

            int idUsuarioEmpresa = Integer.parseInt(idParam);
            UsuarioEmpresaService.Resultado r = service.eliminar(idEmpresa, idUsuarioEmpresa);

            if (r.codigo == UsuarioEmpresaService.OK) {
                response.setStatus(HttpServletResponse.SC_OK);
                salida.put("exito", true);
                salida.put("mensaje", r.mensaje);
            } else if (r.codigo == UsuarioEmpresaService.NO_SE_PUEDE_ELIMINAR_ULTIMO) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                salida.put("exito", false);
                salida.put("mensaje", r.mensaje);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                salida.put("exito", false);
                salida.put("mensaje", r.mensaje);
            }

            out.print(gson.toJson(salida));
            out.flush();
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
