package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.SesionDTO;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/auth/me")
public class AuthMeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            HttpSession session = request.getSession(false);

            if (session == null || session.getAttribute("tipoSesion") == null) {
                SesionDTO s = new SesionDTO();
                s.setAutenticado(false);

                salida.put("exito", true);
                salida.put("datos", s);
                out.print(gson.toJson(salida));
                return;
            }

            String tipoSesion = (String) session.getAttribute("tipoSesion");
            SesionDTO s = new SesionDTO();
            s.setAutenticado(true);
            s.setTipoSesion(tipoSesion);

            if ("USUARIO".equals(tipoSesion)) {
                s.setIdUsuario((Integer) session.getAttribute("idUsuario"));
                s.setNickname((String) session.getAttribute("nickname"));
                s.setTipoUsuario((String) session.getAttribute("tipoUsuario"));
            } else if ("EMPRESA".equals(tipoSesion)) {
                s.setIdUsuarioEmpresa((Integer) session.getAttribute("idUsuarioEmpresa"));
                s.setIdEmpresa((Integer) session.getAttribute("idEmpresa"));
                s.setNombre((String) session.getAttribute("nombre"));
                s.setCorreo((String) session.getAttribute("correo"));
            }

            salida.put("exito", true);
            salida.put("datos", s);
            out.print(gson.toJson(salida));
            out.flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
