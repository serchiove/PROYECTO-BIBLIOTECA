package com.biblioteca.data;

import com.biblioteca.recurso.RecursoTecnologico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecursoTecnologicoDAO {

    private final Connection conexion;

    public RecursoTecnologicoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public List<RecursoTecnologico> listarTodos() throws SQLException {
        List<RecursoTecnologico> lista = new ArrayList<>();
        String sql = "SELECT * FROM recursos_tecnologicos";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearRecurso(rs));
            }
        }
        return lista;
    }

    public List<RecursoTecnologico> listarDisponibles() throws SQLException {
        List<RecursoTecnologico> lista = new ArrayList<>();
        String sql = "SELECT * FROM recursos_tecnologicos WHERE estado = 'Disponible'";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearRecurso(rs));
            }
        }
        return lista;
    }

    public RecursoTecnologico buscarPorId(String id) throws SQLException {
        String sql = "SELECT * FROM recursos_tecnologicos WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearRecurso(rs);
                }
            }
        }
        return null;
    }

    public boolean agregar(RecursoTecnologico recurso) throws SQLException {
        String sql = "INSERT INTO recursos_tecnologicos (id, tipo, estado) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, recurso.getId());
            ps.setString(2, recurso.getTipo());
            ps.setString(3, recurso.getEstado());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizar(RecursoTecnologico recurso) throws SQLException {
        String sql = "UPDATE recursos_tecnologicos SET tipo = ?, estado = ? WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, recurso.getTipo());
            ps.setString(2, recurso.getEstado());
            ps.setString(3, recurso.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarEstado(String id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE recursos_tecnologicos SET estado = ? WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setString(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(String id) throws SQLException {
        String sql = "DELETE FROM recursos_tecnologicos WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    public boolean estaDisponible(String id) throws SQLException {
        String sql = "SELECT estado FROM recursos_tecnologicos WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "Disponible".equalsIgnoreCase(rs.getString("estado"));
                }
            }
        }
        return false;
    }


    public boolean reservarRecurso(String id) throws SQLException {
        return actualizarEstado(id, "Reservado");
    }

    public boolean devolverRecurso(String id) throws SQLException {
        return actualizarEstado(id, "Disponible");
    }
    public boolean reservarRecursoParaUsuario(String idRecurso, String idUsuario) throws SQLException {
        String sqlInsertReserva = "INSERT INTO reservas_recursos_tecnologicos (id_recurso_tecnologico, id_usuario, fecha_reserva, estado) VALUES (?, ?, NOW(), 'Reservado')";
        String sqlActualizarEstadoRecurso = "UPDATE recursos_tecnologicos SET estado = 'Reservado' WHERE id = ?";

        try (PreparedStatement psInsert = conexion.prepareStatement(sqlInsertReserva);
             PreparedStatement psUpdate = conexion.prepareStatement(sqlActualizarEstadoRecurso)) {

            // Insertar la reserva
            psInsert.setString(1, idRecurso);
            psInsert.setString(2, idUsuario);
            int filasInsertadas = psInsert.executeUpdate();

            if (filasInsertadas == 0) {
                return false; // No se pudo insertar reserva
            }

            // Actualizar estado del recurso a reservado
            psUpdate.setString(1, idRecurso);
            int filasActualizadas = psUpdate.executeUpdate();

            return filasActualizadas > 0;
        }
    }

    private RecursoTecnologico mapearRecurso(ResultSet rs) throws SQLException {
        return new RecursoTecnologico(
                rs.getString("id"),
                rs.getString("tipo"),
                rs.getString("estado")
        );
    }
}
