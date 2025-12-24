package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.AgregarMiembroGrupoDTO;
import com.epicstore.epicstore.models.MiembroGrupoModel;
import com.epicstore.epicstore.services.MiembroGrupoService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/api/grupos/miembros")
public class MiembroGrupoController extends HttpServlet {

    private final MiembroGrupoService service = new MiembroGrupoService();
    private final MiembroGrupoModel model = new MiembroGrupoModel();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        String idParam = request.getParameter("idGrupo");

        try (PrintWriter out = response.getWriter()) {

            if (idParam == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                salida.put("exito", false);
                salida.put("mensaje", "Falta el parámetro idGrupo");
                out.print(gson.toJson(salida));
                return;
            }

            int idGrupo = Integer.parseInt(idParam);

            // Validamos que el grupo exista
            try (var conn = model.abrirConexion()) {
                if (!model.grupoExiste(conn, idGrupo)) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    salida.put("exito", false);
                    salida.put("mensaje", "El grupo no existe");
                    out.print(gson.toJson(salida));
                    return;
                }
            } catch (Exception ex) {
                Logger.getLogger(MiembroGrupoController.class.getName()).log(Level.SEVERE, null, ex);
            }

            var lista = service.listarMiembros(idGrupo);

            response.setStatus(HttpServletResponse.SC_OK);
            salida.put("exito", true);
            salida.put("mensaje", "Miembros del grupo");
            salida.put("total", lista.size());
            salida.put("miembros", lista);

            out.print(gson.toJson(salida));
            out.flush();

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            salida.put("exito", false);
            salida.put("mensaje", "El parámetro idGrupo debe ser numérico");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            AgregarMiembroGrupoDTO dto = gson.fromJson(request.getReader(), AgregarMiembroGrupoDTO.class);
            MiembroGrupoService.Resultado r = service.agregarMiembro(dto);

            if (r.codigo == MiembroGrupoService.OK) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                salida.put("exito", true);
                salida.put("mensaje", r.mensaje);

            } else if (r.codigo == MiembroGrupoService.GRUPO_NO_EXISTE
                    || r.codigo == MiembroGrupoService.USUARIO_NO_EXISTE) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                salida.put("exito", false);
                salida.put("mensaje", r.mensaje);

            } else if (r.codigo == MiembroGrupoService.DUENO_NO_VALIDO
                    || r.codigo == MiembroGrupoService.LIMITE_MIEMBROS) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                salida.put("exito", false);
                salida.put("mensaje", r.mensaje);

            } else if (r.codigo == MiembroGrupoService.YA_ES_MIEMBRO) {
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
            salida.put("exito", false);
            salida.put("mensaje", "JSON inválido o datos incompletos");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        com.google.gson.Gson gson = new com.google.gson.Gson();
        java.util.HashMap<String, Object> salida = new java.util.HashMap<>();

        try (java.io.PrintWriter out = response.getWriter()) {

            com.epicstore.epicstore.dtos.EliminarMiembroDTO dto
                    = gson.fromJson(request.getReader(), com.epicstore.epicstore.dtos.EliminarMiembroDTO.class);

            com.epicstore.epicstore.services.GrupoAdminService adminService
                    = new com.epicstore.epicstore.services.GrupoAdminService();

            var r = adminService.eliminarMiembro(dto);

            if (r.codigo == com.epicstore.epicstore.services.GrupoAdminService.OK) {
                response.setStatus(HttpServletResponse.SC_OK);
                salida.put("exito", true);
                salida.put("mensaje", r.mensaje);

            } else if (r.codigo == com.epicstore.epicstore.services.GrupoAdminService.GRUPO_NO_EXISTE
                    || r.codigo == com.epicstore.epicstore.services.GrupoAdminService.MIEMBRO_NO_EXISTE) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                salida.put("exito", false);
                salida.put("mensaje", r.mensaje);

            } else if (r.codigo == com.epicstore.epicstore.services.GrupoAdminService.NO_ES_DUENO
                    || r.codigo == com.epicstore.epicstore.services.GrupoAdminService.NO_SE_PUEDE_ELIMINAR_DUENO) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
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
