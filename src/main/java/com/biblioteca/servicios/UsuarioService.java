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

    /**
     * Crea un usuario nuevo según el rol especificado.
     * Retorna false si el usuario ya existe o si el rol es inválido.
     */
    public boolean crearUsuario(String id, String nombre, String usuarioLogin, String contrasena, String rol) {
        try {
            if (usuarioDAO.obtenerPorId(id) != null) {
                System.err.println("El usuario con id " + id + " ya existe.");
                return false;
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
                    System.err.println("Rol inválido: " + rol);
                    return false;
            }

            return usuarioDAO.agregar(nuevoUsuario);

        } catch (SQLException e) {
            System.err.println("❌ Error al crear usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Autentica un usuario buscando coincidencia por usuario y contraseña.
     * En caso de éxito, guarda el usuario activo y lo retorna.
     */
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

    // Métodos específicos para crear usuarios con roles predefinidos
    public boolean registrarEstudiante(String id, String nombre, String usuario, String contrasena) {
        return crearUsuario(id, nombre, usuario, contrasena, "ESTUDIANTE");
    }

    public boolean registrarProfesor(String id, String nombre, String usuario, String contrasena) {
        return crearUsuario(id, nombre, usuario, contrasena, "PROFESOR");
    }

    /**
     * Lista todos los usuarios de la base de datos.
     * Retorna una lista vacía en caso de error.
     */
    public List<Usuario> listarUsuarios() {
        try {
            return usuarioDAO.listar();
        } catch (SQLException e) {
            System.err.println("❌ Error al listar usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Busca un usuario por su ID.
     * Retorna null si no lo encuentra o hay error.
     */
    public Usuario buscarUsuarioPorId(String id) {
        try {
            return usuarioDAO.obtenerPorId(id);
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar usuario: " + e.getMessage());
            return null;
        }
    }

    /**
     * Elimina un usuario por su ID.
     * Retorna false si hay error.
     */
    public boolean eliminarUsuario(String id) {
        try {
            return usuarioDAO.eliminar(id);
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}
