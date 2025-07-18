package com.biblioteca.data;

import com.biblioteca.recurso.RecursoTecnologico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar recursos tecnológicos en la base de datos.
 */
public class RecursoTecnologicoDAO {

    private final Connection conexion;

    public RecursoTecnologicoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * Lista todos los recursos tecnológicos.
     */
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

    /**
     * Lista únicamente los recursos disponibles.
     */
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

    /**
     * Busca un recurso tecnológico por su ID.
     */
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

    /**
     * Agrega un nuevo recurso tecnológico.
     */
    public boolean agregar(RecursoTecnologico recurso) throws SQLException {
        String sql = "INSERT INTO recursos_tecnologicos (id, tipo, estado) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, recurso.getId());
            ps.setString(2, recurso.getTipo());
            ps.setString(3, recurso.getEstado());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Actualiza un recurso tecnológico existente.
     */
    public boolean actualizar(RecursoTecnologico recurso) throws SQLException {
        String sql = "UPDATE recursos_tecnologicos SET tipo = ?, estado = ? WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, recurso.getTipo());
            ps.setString(2, recurso.getEstado());
            ps.setString(3, recurso.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Actualiza solo el estado de un recurso tecnológico.
     */
    public boolean actualizarEstado(String id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE recursos_tecnologicos SET estado = ? WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setString(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Elimina un recurso tecnológico por ID.
     */
    public boolean eliminar(String id) throws SQLException {
        String sql = "DELETE FROM recursos_tecnologicos WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Mapea un ResultSet a un objeto RecursoTecnologico.
     */
    private RecursoTecnologico mapearRecurso(ResultSet rs) throws SQLException {
        return new RecursoTecnologico(
                rs.getString("id"),
                rs.getString("tipo"),
                rs.getString("estado")
        );
    }
}
