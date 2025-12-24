package com.epicstore.epicstore.services;

import com.epicstore.epicstore.dtos.CrearUsuarioEmpresaDTO;
import com.epicstore.epicstore.dtos.UsuarioEmpresaDTO;
import com.epicstore.epicstore.models.UsuarioEmpresaModel;

import java.util.ArrayList;

public class UsuarioEmpresaService {

    public static final int OK = 0;
    public static final int DATOS_INVALIDOS = 1;
    public static final int CORREO_YA_EXISTE = 2;
    public static final int NO_SE_PUEDE_ELIMINAR_ULTIMO = 3;
    public static final int NO_ENCONTRADO = 4;
    public static final int ERROR = 9;

    public static class Resultado {

        public int codigo;
        public String mensaje;
        public Object datos;

        public Resultado(int c, String m) {
            codigo = c;
            mensaje = m;
        }

        public Resultado(int c, String m, Object d) {
            codigo = c;
            mensaje = m;
            datos = d;
        }
    }

    private final UsuarioEmpresaModel model = new UsuarioEmpresaModel();

    public Resultado listar(int idEmpresa) {
        ArrayList<UsuarioEmpresaDTO> lista = model.listarUsuariosEmpresa(idEmpresa);
        return new Resultado(OK, "Listado de usuarios de la empresa", lista);
    }

    public Resultado crear(int idEmpresa, CrearUsuarioEmpresaDTO dto) {
        if (dto == null) {
            return new Resultado(DATOS_INVALIDOS, "JSON inválido");
        }
        if (dto.getNombre() == null || dto.getCorreo() == null || dto.getFechaNacimiento() == null || dto.getPassword() == null) {
            return new Resultado(DATOS_INVALIDOS, "Datos incompletos");
        }
        if (dto.getNombre().trim().isEmpty() || dto.getCorreo().trim().isEmpty() || dto.getPassword().trim().isEmpty()) {
            return new Resultado(DATOS_INVALIDOS, "Datos incompletos");
        }

        String correo = dto.getCorreo().trim().toLowerCase();
        dto.setCorreo(correo);

        if (model.existeCorreo(correo)) {
            return new Resultado(CORREO_YA_EXISTE, "Ya existe un usuario empresa con ese correo");
        }

        Integer idNuevo = model.crearUsuarioEmpresa(idEmpresa, dto);
        if (idNuevo == null) {
            return new Resultado(ERROR, "No se pudo crear el usuario empresa");
        }

        return new Resultado(OK, "Usuario empresa creado correctamente", idNuevo);
    }

    public Resultado eliminar(int idEmpresa, int idUsuarioEmpresa) {
        // Regla: la empresa debe tener al menos 1 usuario
        int total = model.contarUsuariosEmpresa(idEmpresa);
        if (total <= 1) {
            return new Resultado(NO_SE_PUEDE_ELIMINAR_ULTIMO, "No se puede eliminar el último usuario de la empresa");
        }

        boolean ok = model.eliminarUsuarioEmpresa(idEmpresa, idUsuarioEmpresa);
        if (!ok) {
            return new Resultado(NO_ENCONTRADO, "Usuario empresa no encontrado o no pertenece a tu empresa");
        }

        return new Resultado(OK, "Usuario empresa eliminado correctamente");
    }
}
