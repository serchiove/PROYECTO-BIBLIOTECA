package com.biblioteca.data;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoRecursoTecnologicoDAO {

    private final Connection conexion;

    public PrestamoRecursoTecnologicoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public List<Prestamo> cargarPrestamos() throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos_recursos_tecnologicos";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LocalDate fechaFin = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toLocalDate() : null;

                Prestamo p = new Prestamo(
                        rs.getString("id"),
                        rs.getString("id_usuario"),
                        rs.getString("id_recurso_tecnologico"),
                        rs.getDate("fecha_prestamo").toLocalDate(),
                        rs.getDate("fecha_devolucion") != null ? rs.getDate("fecha_devolucion").toLocalDate() : null,
                        fechaFin,
                        rs.getString("tipo_recurso")
                );

                prestamos.add(p);
            }
        }
        return prestamos;
    }

    public boolean registrarPrestamo(String idPrestamo, String idUsuario, String idRecursoTecnologico, LocalDate fechaPrestamo, String tipoRecurso) {
        String sql = "INSERT INTO prestamos_recursos_tecnologicos (id, id_usuario, id_recurso_tecnologico, fecha_prestamo, tipo_recurso) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idPrestamo);
            ps.setString(2, idUsuario);
            ps.setString(3, idRecursoTecnologico);
            ps.setDate(4, Date.valueOf(fechaPrestamo));
            ps.setString(5, tipoRecurso);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar préstamo recurso tecnológico: " + e.getMessage());
            return false;
        }
    }

    public boolean registrarDevolucion(String idPrestamo, LocalDate fechaDevolucion) {
        String sql = "UPDATE prestamos_recursos_tecnologicos SET fecha_devolucion = ? WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fechaDevolucion));
            ps.setString(2, idPrestamo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar devolución recurso tecnológico: " + e.getMessage());
            return false;
        }
    }

    public boolean existenPrestamosParaRecurso(String idRecursoTecnologico) {
        String sql = "SELECT COUNT(*) FROM prestamos_recursos_tecnologicos WHERE id_recurso_tecnologico = ? AND fecha_devolucion IS NULL";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idRecursoTecnologico);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar préstamos recurso tecnológico: " + e.getMessage());
            return false;
        }
    }

    public List<Prestamo> obtenerPrestamosPorUsuario(String idUsuario) throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos_recursos_tecnologicos WHERE id_usuario = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prestamo p = new Prestamo(
                            rs.getString("id"),
                            rs.getString("id_usuario"),
                            rs.getString("id_recurso_tecnologico"),
                            rs.getDate("fecha_prestamo").toLocalDate(),
                            rs.getDate("fecha_devolucion") != null ? rs.getDate("fecha_devolucion").toLocalDate() : null,
                            rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toLocalDate() : null,
                            rs.getString("tipo_recurso")
                    );
                    prestamos.add(p);
                }
            }
        }

        return prestamos;
    }
}
