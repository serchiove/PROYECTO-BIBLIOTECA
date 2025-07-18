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

    // 🔄 Carga todos los préstamos existentes desde la base de datos
    public List<Prestamo> cargarPrestamos() throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                prestamos.add(mapearPrestamo(rs));
            }
        }
        return prestamos;
    }

    // 💾 Guarda la lista completa de préstamos, borrando los anteriores
    public void guardarPrestamos(List<Prestamo> prestamos) throws SQLException {
        String deleteSql = "DELETE FROM prestamos";
        String insertSql = """
            INSERT INTO prestamos 
            (id, id_usuario, id_recurso, fecha_prestamo, fecha_devolucion, fecha_fin, tipo_recurso) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

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
                    ps.setDate(4, p.getFechaInicio() != null ? Date.valueOf(p.getFechaInicio()) : null);
                    ps.setDate(5, p.getFechaDevolucion() != null ? Date.valueOf(p.getFechaDevolucion()) : null);
                    ps.setDate(6, p.getFechaFin() != null ? Date.valueOf(p.getFechaFin()) : null);
                    ps.setString(7, p.getTipoRecurso());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conexion.commit();

        } catch (SQLException e) {
            conexion.rollback();
            System.err.println("❌ Error al guardar préstamos: " + e.getMessage());
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    // ❓ Verifica si un recurso tiene un préstamo pendiente (no devuelto)
    public boolean existenPrestamosParaRecurso(String idRecurso) {
        String sql = "SELECT COUNT(*) FROM prestamos WHERE id_recurso = ? AND fecha_devolucion IS NULL";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, idRecurso);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al verificar préstamos: " + e.getMessage());
            return false;
        }
    }

    // 📋 Obtiene todos los préstamos de un usuario
    public List<Prestamo> obtenerPrestamosPorUsuario(String idUsuario) throws SQLException {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE id_usuario = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prestamos.add(mapearPrestamo(rs));
                }
            }
        }

        return prestamos;
    }

    // ➕ Registra un nuevo préstamo
    public boolean registrarPrestamo(String idPrestamo, String idUsuario, String idRecurso, LocalDate fechaPrestamo, String tipoRecurso) {
        String sql = """
            INSERT INTO prestamos (id, id_usuario, id_recurso, fecha_prestamo, tipo_recurso)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, idPrestamo);
            ps.setString(2, idUsuario);
            ps.setString(3, idRecurso);
            ps.setDate(4, Date.valueOf(fechaPrestamo));
            ps.setString(5, tipoRecurso);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar préstamo: " + e.getMessage());
            return false;
        }
    }

    // ✔️ Marca un préstamo como devuelto
    public boolean registrarDevolucion(String idPrestamo, LocalDate fechaDevolucion) {
        String sql = "UPDATE prestamos SET fecha_devolucion = ? WHERE id = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fechaDevolucion));
            ps.setString(2, idPrestamo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar devolución: " + e.getMessage());
            return false;
        }
    }

    // 🧱 Método auxiliar para mapear un ResultSet a un objeto Prestamo
    private Prestamo mapearPrestamo(ResultSet rs) throws SQLException {
        Date fechaPrestamoSql = rs.getDate("fecha_prestamo");
        LocalDate fechaPrestamo = (fechaPrestamoSql != null) ? fechaPrestamoSql.toLocalDate() : null;

        Date fechaDevolucionSql = rs.getDate("fecha_devolucion");
        LocalDate fechaDevolucion = (fechaDevolucionSql != null) ? fechaDevolucionSql.toLocalDate() : null;

        Date fechaFinSql = rs.getDate("fecha_fin");
        LocalDate fechaFin = (fechaFinSql != null) ? fechaFinSql.toLocalDate() : null;

        return new Prestamo(
                rs.getString("id"),
                rs.getString("id_usuario"),
                rs.getString("id_recurso"),
                fechaPrestamo,
                fechaDevolucion,
                fechaFin,
                rs.getString("tipo_recurso")
        );
    }
}
