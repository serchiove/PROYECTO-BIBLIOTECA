package com.biblioteca.data;

import com.biblioteca.multimedia.LibroDigital;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    private Connection conexion;

    public LibroDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public List<LibroDigital> obtenerTodosLosLibros() {
        List<LibroDigital> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros";

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LibroDigital libro = new LibroDigital(
                        rs.getString("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("isbn"),
                        rs.getInt("tamanoMB"),
                        rs.getBoolean("disponible")
                );
                libros.add(libro);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener los libros: " + e.getMessage());
        }

        return libros;
    }

    public void insertarLibro(LibroDigital libro) {
        String sql = "INSERT INTO libros (id, titulo, autor, isbn, tamanoMB, disponible) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, libro.getId());
            stmt.setString(2, libro.getTitulo());
            stmt.setString(3, libro.getAutor());
            stmt.setString(4, libro.getIsbn());
            stmt.setInt(5, libro.getTamanoMB());
            stmt.setBoolean(6, libro.isDisponible());
            stmt.executeUpdate();
            System.out.println("✅ Libro insertado correctamente.");
        } catch (SQLException e) {
            System.err.println("❌ Error al insertar el libro: " + e.getMessage());
        }
    }

    public void actualizarDisponibilidad(String id, boolean disponible) {
        String sql = "UPDATE libros SET disponible = ? WHERE id = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setBoolean(1, disponible);
            stmt.setString(2, id);
            stmt.executeUpdate();
            System.out.println("✅ Disponibilidad actualizada.");
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar la disponibilidad: " + e.getMessage());
        }
    }

    public void eliminarLibro(String id) {
        String sql = "DELETE FROM libros WHERE id = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Libro eliminado.");
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar el libro: " + e.getMessage());
        }
    }

    // ✅ Método adicional opcional
    public LibroDigital buscarLibroPorId(String id) {
        String sql = "SELECT * FROM libros WHERE id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new LibroDigital(
                            rs.getString("id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getString("isbn"),
                            rs.getInt("tamanoMB"),
                            rs.getBoolean("disponible")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar el libro: " + e.getMessage());
        }
        return null;
    }
}
