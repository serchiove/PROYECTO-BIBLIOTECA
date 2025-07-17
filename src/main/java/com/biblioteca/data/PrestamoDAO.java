package com.biblioteca.data;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    private final Connection conexion;

    public PrestamoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public List<Prestamo> cargarPrestamos() throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LocalDate fechaFin = rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toLocalDate() : null;

                Prestamo p = new Prestamo(
                        rs.getString("id"),
                        rs.getString("id_usuario"),
                        rs.getString("id_recurso"),
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

    public void guardarPrestamos(List<Prestamo> prestamos) throws SQLException {
        String deleteSql = "DELETE FROM prestamos";
        String insertSql = "INSERT INTO prestamos (id, id_usuario, id_recurso, fecha_prestamo, fecha_devolucion, fecha_fin, tipo_recurso) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            conexion.setAutoCommit(false);

            try (Statement stmt = conexion.createStatement()) {
                stmt.executeUpdate(deleteSql);
            }

            try (PreparedStatement ps = conexion.prepareStatement(insertSql)) {
                for (Prestamo p : prestamos) {
                    ps.setString(1, p.getId());
                    ps.setString(2, p.getIdUsuario());
                    ps.setString(3, p.getIdRecurso());
                    ps.setDate(4, Date.valueOf(p.getFechaInicio()));

                    if (p.getFechaDevolucion() != null) {
                        ps.setDate(5, Date.valueOf(p.getFechaDevolucion()));
                    } else {
                        ps.setNull(5, Types.DATE);
                    }

                    if (p.getFechaFin() != null) {
                        ps.setDate(6, Date.valueOf(p.getFechaFin()));
                    } else {
                        ps.setNull(6, Types.DATE);
                    }

                    ps.setString(7, p.getTipoRecurso());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conexion.commit();

        } catch (SQLException e) {
            conexion.rollback();
            throw e;

        } finally {
            conexion.setAutoCommit(true);
        }
    }

    public boolean existenPrestamosParaRecurso(String idRecurso) {
        String sql = "SELECT COUNT(*) FROM prestamos WHERE id_recurso = ? AND fecha_devolucion IS NULL";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, idRecurso);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar préstamos: " + e.getMessage());
            return false;
        }
    }

    public List<Prestamo> obtenerPrestamosPorUsuario(String idUsuario) throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE id_usuario = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prestamo p = new Prestamo(
                            rs.getString("id"),
                            rs.getString("id_usuario"),
                            rs.getString("id_recurso"),
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

    public boolean registrarPrestamo(String idPrestamo, String idUsuario, String idRecurso, LocalDate fechaPrestamo, String tipoRecurso) {
        String sql = "INSERT INTO prestamos (id, id_usuario, id_recurso, fecha_prestamo, tipo_recurso) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idPrestamo);
            ps.setString(2, idUsuario);
            ps.setString(3, idRecurso);
            ps.setDate(4, Date.valueOf(fechaPrestamo));
            ps.setString(5, tipoRecurso);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar préstamo: " + e.getMessage());
            return false;
        }
    }

    public boolean registrarDevolucion(String idPrestamo, LocalDate fechaDevolucion) {
        String sql = "UPDATE prestamos SET fecha_devolucion = ? WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fechaDevolucion));
            ps.setString(2, idPrestamo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar devolución: " + e.getMessage());
            return false;
        }
    }
}
