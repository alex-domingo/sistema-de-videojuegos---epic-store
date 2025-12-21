package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.AgregarMiembroGrupoDTO;
import com.epicstore.epicstore.services.MiembroGrupoService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/grupos/miembros")
public class MiembroGrupoController extends HttpServlet {

    private final MiembroGrupoService service = new MiembroGrupoService();

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
}
