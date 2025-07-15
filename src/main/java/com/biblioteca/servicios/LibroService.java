package com.biblioteca.servicios;

import com.biblioteca.multimedia.LibroDigital;
import com.biblioteca.multimedia.Multimedia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroService {

    private Connection conexion;

    public LibroService(Connection conexion) {
        this.conexion = conexion;
    }

    public void agregarLibro(LibroDigital libro) throws SQLException {
        String sql = "INSERT INTO libros (id, titulo, autor, isbn, tamanoMB, disponible) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, libro.getId());
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getAutor());
            ps.setString(4, libro.getIsbn());
            ps.setInt(5, libro.getTamanoMB());
            ps.setBoolean(6, libro.isDisponible());
            ps.executeUpdate();
        }
    }

    public List<Multimedia> listarLibros() throws SQLException {
        List<Multimedia> lista = new ArrayList<>();
        String sql = "SELECT * FROM libros";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LibroDigital libro = new LibroDigital(
                        rs.getString("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("isbn"),
                        rs.getInt("tamanoMB"),
                        rs.getBoolean("disponible")
                );
                lista.add(libro);
            }
        }
        return lista;
    }

    public LibroDigital buscarLibroPorId(String id) throws SQLException {
        String sql = "SELECT * FROM libros WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
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
        }
        return null;
    }

    public void eliminarLibro(String id) throws SQLException {
        String sql = "DELETE FROM libros WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }
}
