package com.biblioteca.data;

import com.biblioteca.multimedia.LibroDigital;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    private final Connection conexion;

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

    public boolean insertarLibro(LibroDigital libro) {
        String sql = "INSERT INTO libros (id, titulo, autor, isbn, tamanoMB, disponible) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, libro.getId());
            stmt.setString(2, libro.getTitulo());
            stmt.setString(3, libro.getAutor());
            stmt.setString(4, libro.getIsbn());
            stmt.setInt(5, libro.getTamanoMB());
            stmt.setBoolean(6, libro.isDisponible());
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al insertar el libro: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarDisponibilidad(String id, boolean disponible) {
        String sql = "UPDATE libros SET disponible = ? WHERE id = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setBoolean(1, disponible);
            stmt.setString(2, id);
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar la disponibilidad: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarLibro(String id) {
        String sql = "DELETE FROM libros WHERE id = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, id);
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar el libro: " + e.getMessage());
            return false;
        }
    }

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
