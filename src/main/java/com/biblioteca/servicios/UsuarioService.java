package com.biblioteca.servicios;

import com.biblioteca.data.UsuarioDAO;
import com.biblioteca.usuarios.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;
    private static Usuario usuarioActivo;

    public UsuarioService(Connection connection) {
        this.usuarioDAO = new UsuarioDAO(connection);
    }

    public UsuarioDAO getUsuarioDAO() {
        return this.usuarioDAO;
    }

    // Crear usuario genérico según rol
    public boolean crearUsuario(String id, String nombre, String usuarioLogin, String contrasena, String rol) {
        try {
            if (usuarioDAO.obtenerPorId(id) != null) {
                return false; // Ya existe un usuario con ese ID
            }

            Usuario nuevoUsuario;
            switch (rol.toUpperCase()) {
                case "ESTUDIANTE":
                    nuevoUsuario = new Estudiante(id, nombre, usuarioLogin, contrasena);
                    break;
                case "PROFESOR":
                    nuevoUsuario = new Profesor(id, nombre, usuarioLogin, contrasena);
                    break;
                case "ADMIN":
                    nuevoUsuario = new Administrador(id, nombre, usuarioLogin, contrasena);
                    break;
                default:
                    return false; // Rol inválido
            }

            return usuarioDAO.agregar(nuevoUsuario);

        } catch (SQLException e) {
            System.err.println("❌ Error al crear usuario: " + e.getMessage());
            return false;
        }
    }

    // Autenticación del usuario
    public Usuario autenticar(String usuarioLogin, String contrasena) {
        try {
            List<Usuario> usuarios = usuarioDAO.listar();
            for (Usuario u : usuarios) {
                if (u.getUsuario().equals(usuarioLogin) && u.getPassword().equals(contrasena)) {
                    usuarioActivo = u;
                    return u;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al autenticar: " + e.getMessage());
        }
        return null;
    }

    public static void setUsuarioActivo(Usuario usuario) {
        usuarioActivo = usuario;
    }

    public static Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    // Registrar estudiante
    public boolean registrarEstudiante(String id, String nombre, String usuario, String contrasena) {
        return crearUsuario(id, nombre, usuario, contrasena, "ESTUDIANTE");
    }

    // Registrar profesor
    public boolean registrarProfesor(String id, String nombre, String usuario, String contrasena) {
        return crearUsuario(id, nombre, usuario, contrasena, "PROFESOR");
    }

    // Listar todos los usuarios (con debug)
    public List<Usuario> listarUsuarios() {
        try {
            return usuarioDAO.listar();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }



    // Buscar por ID
    public Usuario buscarUsuarioPorId(String id) {
        try {
            return usuarioDAO.obtenerPorId(id);
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar usuario: " + e.getMessage());
            return null;
        }
    }

    // Eliminar usuario
    public boolean eliminarUsuario(String id) {
        try {
            return usuarioDAO.eliminar(id);
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}
