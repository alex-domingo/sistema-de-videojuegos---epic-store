package com.epicstore.epicstore.controllers;

import com.epicstore.epicstore.services.ReportesEmpresaService;
import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/api/empresa/reportes/feedback/mejores-comentarios")
public class EmpresaReporteFeedbackMejoresComentariosController extends HttpServlet {

    private final ReportesEmpresaService service = new ReportesEmpresaService();

    private Integer idEmpresa(HttpServletRequest request) {
        HttpSession s = request.getSession(false);
        if (s == null) {
            return null;
        }
        Object tipo = s.getAttribute("tipoSesion");
        if (tipo == null || !"EMPRESA".equals(tipo.toString())) {
            return null;
        }
        return (Integer) s.getAttribute("idEmpresa");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        HashMap<String, Object> outJson = new HashMap<>();

        try (PrintWriter out = response.getWriter()) {
            Integer idEmpresa = idEmpresa(request);
            if (idEmpresa == null) {
                response.setStatus(401);
                outJson.put("exito", false);
                outJson.put("mensaje", "No autenticado como empresa");
                out.print(gson.toJson(outJson));
                return;
            }

            String desde = request.getParameter("desde");
            String hasta = request.getParameter("hasta");
            String limit = request.getParameter("limit");

            var r = service.mejoresComentarios(idEmpresa, desde, hasta, limit);

            response.setStatus(200);
            outJson.put("exito", r.exito);
            outJson.put("mensaje", r.mensaje);
            outJson.put("desde", desde);
            outJson.put("hasta", hasta);
            outJson.put("datos", r.datos);

            out.print(gson.toJson(outJson));
        }
    }
}
