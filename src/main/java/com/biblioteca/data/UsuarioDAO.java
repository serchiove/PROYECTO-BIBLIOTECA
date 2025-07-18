package com.biblioteca.data;

import com.biblioteca.usuarios.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private final Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    public Usuario obtenerPorId(String id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construirUsuario(rs);
                }
            }
        }
        return null;
    }

    public List<Usuario> listar() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(construirUsuario(rs));
            }
        }
        return usuarios;
    }

    public boolean agregar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (id, nombre, usuario, contrasena, rol) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, usuario.getId());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getUsuario());
            ps.setString(4, usuario.getPassword()); // getPassword() devuelve contrasena
            ps.setString(5, usuario.getRol());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(String id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Usuario construirUsuario(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String nombre = rs.getString("nombre");
        String usuario = rs.getString("usuario");
        String password = rs.getString("contrasena");
        String rol = rs.getString("rol");

        switch (rol.toUpperCase()) {
            case "ESTUDIANTE":
                return new Estudiante(id, nombre, usuario, password);
            case "PROFESOR":
                return new Profesor(id, nombre, usuario, password);
            case "ADMIN":
            case "ADMINISTRADOR":
                return new Administrador(id, nombre, usuario, password);
            default:
                throw new SQLException("Rol desconocido: " + rol);
        }
    }
}
