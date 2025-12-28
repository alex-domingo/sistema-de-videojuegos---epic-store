package com.epicstore.epicstore.filters;

import com.google.gson.Gson;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

@WebFilter("/api/*")
public class AuthFilter implements Filter {

    // Rutas 100% públicas (cualquier método)
    private static final Set<String> RUTAS_PUBLICAS = Set.of(
            "/api/health",
            "/api/auth/login",
            "/api/auth/login-empresa",
            "/api/auth/logout",
            "/api/auth/me",
            "/api/categorias",
            "/api/videojuegos",
            "/api/usuarios/registro",
            "/api/usuarios/buscar",
            "/api/perfiles/usuario"
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();          // /EpicStore-1.0-SNAPSHOT/api/compras
        String ctx = request.getContextPath();          // /EpicStore-1.0-SNAPSHOT
        if (path.startsWith(ctx)) {
            path = path.substring(ctx.length()); // /api/compras
        }
        String method = request.getMethod(); // GET/POST/PUT/DELETE

        // 1) Rutas públicas directas
        if (RUTAS_PUBLICAS.contains(path)) {
            chain.doFilter(req, res);
            return;
        }

        // 2) Caso especial: /api/comentarios
        //    - GET público (listar)
        //    - POST requiere sesión USUARIO
        if ("/api/comentarios".equals(path) && "GET".equalsIgnoreCase(method)) {
            chain.doFilter(req, res);
            return;
        }

        // 3) Todo lo demás requiere sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("tipoSesion") == null) {
            denegar(response, HttpServletResponse.SC_UNAUTHORIZED, "Debes iniciar sesión");
            return;
        }

        String tipoSesion = String.valueOf(session.getAttribute("tipoSesion")); // "USUARIO" o "EMPRESA"

        // 4) Rutas de empresa: SOLO EMPRESA
        //    incluye /api/empresas/perfil (plural)
        if (path.startsWith("/api/empresa") || path.startsWith("/api/empresas/")) {
            if (!"EMPRESA".equalsIgnoreCase(tipoSesion)) {
                denegar(response, HttpServletResponse.SC_FORBIDDEN, "Acceso solo para usuario empresa");
                return;
            }
            chain.doFilter(req, res);
            return;
        }

        // 5) El resto: SOLO USUARIO (común o admin, según la implementación)
        if (!"USUARIO".equalsIgnoreCase(tipoSesion)) {
            denegar(response, HttpServletResponse.SC_FORBIDDEN, "Acceso solo para usuarios");
            return;
        }

        chain.doFilter(req, res);
    }

    private void denegar(HttpServletResponse response, int status, String mensaje) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HashMap<String, Object> salida = new HashMap<>();
        salida.put("exito", false);
        salida.put("mensaje", mensaje);

        response.getWriter().print(new Gson().toJson(salida));
    }
}
