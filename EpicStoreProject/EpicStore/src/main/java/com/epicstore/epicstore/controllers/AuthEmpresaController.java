package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.dtos.LoginEmpresaDTO;
import com.epicstore.epicstore.models.AuthModel;
import com.epicstore.epicstore.services.AuthService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/auth/login-empresa")
public class AuthEmpresaController extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> salida = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {

            LoginEmpresaDTO dto = gson.fromJson(request.getReader(), LoginEmpresaDTO.class);

            AuthModel.EmpresaLoginData emp = authService.loginEmpresa(
                    dto != null ? dto.getCorreo() : null,
                    dto != null ? dto.getPassword() : null
            );

            if (emp == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                salida.put("exito", false);
                salida.put("mensaje", "Credenciales inválidas");
                out.print(gson.toJson(salida));
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("tipoSesion", "EMPRESA");
            session.setAttribute("idUsuarioEmpresa", emp.idUsuarioEmpresa);
            session.setAttribute("idEmpresa", emp.idEmpresa);
            session.setAttribute("nombre", emp.nombre);
            session.setAttribute("correo", emp.correo);

            response.setStatus(HttpServletResponse.SC_OK);
            salida.put("exito", true);
            salida.put("mensaje", "Inicio de sesión (empresa) exitoso");
            salida.put("tipoSesion", "EMPRESA");
            salida.put("idUsuarioEmpresa", emp.idUsuarioEmpresa);
            salida.put("idEmpresa", emp.idEmpresa);
            salida.put("nombre", emp.nombre);
            salida.put("correo", emp.correo);

            out.print(gson.toJson(salida));
            out.flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
