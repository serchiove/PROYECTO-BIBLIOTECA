package com.biblioteca.data;

import com.biblioteca.multimedia.Multimedia;
import com.biblioteca.usuarios.Usuario;

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
                Prestamo p = new Prestamo(
                        rs.getString("id"),
                        rs.getString("id_usuario"),
                        rs.getString("id_multimedia"),  // corregido aquí
                        rs.getDate("fecha_prestamo").toLocalDate(),  // corregido aquí
                        rs.getDate("fecha_devolucion") != null
                                ? rs.getDate("fecha_devolucion").toLocalDate()
                                : null
                );
                prestamos.add(p);
            }
        }
        return prestamos;
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
                            rs.getString("id_multimedia"),  // corregido aquí
                            rs.getDate("fecha_prestamo").toLocalDate(),
                            rs.getDate("fecha_devolucion") != null
                                    ? rs.getDate("fecha_devolucion").toLocalDate()
                                    : null
                    );
                    prestamos.add(p);
                }
            }
        }
        return prestamos;
    }

    public void guardarPrestamos(List<Prestamo> prestamos) throws SQLException {
        // Aquí puedes hacer un borrado e inserción o actualizaciones
        // Para ejemplo sencillo, se borran todos y luego se insertan

        conexion.setAutoCommit(false);
        try (Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate("DELETE FROM prestamos");
        }

        String sqlInsert = "INSERT INTO prestamos (id, id_usuario, id_multimedia, fecha_prestamo, fecha_devolucion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sqlInsert)) {
            for (Prestamo p : prestamos) {
                ps.setString(1, p.getId());
                ps.setString(2, p.getIdUsuario());
                ps.setString(3, p.getIdMultimedia());  // corregido aquí
                ps.setDate(4, Date.valueOf(p.getFechaInicio()));  // asumiendo fechaInicio es fecha_prestamo
                if (p.getFechaDevolucion() != null) {
                    ps.setDate(5, Date.valueOf(p.getFechaDevolucion()));
                } else {
                    ps.setNull(5, Types.DATE);
                }
                ps.addBatch();
            }
            ps.executeBatch();
        }

        conexion.commit();
        conexion.setAutoCommit(true);
    }
}
