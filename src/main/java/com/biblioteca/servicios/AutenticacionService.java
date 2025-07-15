package com.biblioteca.servicios;

import com.biblioteca.usuarios.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutenticacionService {

    private Connection conexion;

    public AutenticacionService(Connection conexion) {
        this.conexion = conexion;
    }

    public Usuario autenticar(String usuarioIngresado, String passwordIngresado) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuarioIngresado);
            ps.setString(2, passwordIngresado);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String rol = rs.getString("rol");
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                String usuario = rs.getString("usuario");
                String password = rs.getString("contrasena");

                return construirUsuario(id, nombre, usuario, password, rol);
            }

        } catch (SQLException e) {
            System.err.println("Error en autenticación: " + e.getMessage());
        }
        return null;
    }

    public List<Usuario> obtenerTodosUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String rol = rs.getString("rol");
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                String usuario = rs.getString("usuario");
                String password = rs.getString("contrasena");

                Usuario u = construirUsuario(id, nombre, usuario, password, rol);
                if (u != null) lista.add(u);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }

        return lista;
    }

    private Usuario construirUsuario(String id, String nombre, String usuario, String contrasena, String rol) {
        return switch (rol.toUpperCase()) {
            case "ADMIN" -> new Administrador(id, nombre, usuario, contrasena);
            case "PROFESOR" -> new Profesor(id, nombre, usuario, contrasena);
            case "ESTUDIANTE" -> new Estudiante(id, nombre, usuario, contrasena);
            default -> null;
        };
    }
}
