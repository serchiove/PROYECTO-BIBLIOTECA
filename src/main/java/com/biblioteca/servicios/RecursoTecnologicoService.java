package com.biblioteca.servicios;

import com.biblioteca.data.RecursoTecnologicoDAO;
import com.biblioteca.recurso.RecursoTecnologico;

import java.sql.SQLException;
import java.util.List;

public class RecursoTecnologicoService {

    private final RecursoTecnologicoDAO dao;

    public RecursoTecnologicoService(RecursoTecnologicoDAO dao) {
        this.dao = dao;
    }
    public RecursoTecnologico buscarPorId(String id) throws SQLException {
        return dao.buscarPorId(id);
    }

    public boolean actualizarRecursoTecnologico(RecursoTecnologico recurso) throws SQLException {
        return dao.actualizar(recurso);  // Asegúrate de tener este método en el DAO
    }

    public boolean marcarComoDisponible(String id) throws SQLException {
        RecursoTecnologico recurso = dao.buscarPorId(id);
        if (recurso != null && !"Disponible".equalsIgnoreCase(recurso.getEstado())) {
            return dao.actualizarEstado(id, "Disponible");
        }
        return false;
    }

    public List<RecursoTecnologico> getTodos() throws SQLException {
        return dao.listarTodos();
    }

    public List<RecursoTecnologico> listarDisponibles() throws SQLException {
        return dao.listarDisponibles();
    }

    public boolean reservarRecurso(String id) throws SQLException {
        RecursoTecnologico recurso = dao.buscarPorId(id);
        if (recurso != null && "Disponible".equalsIgnoreCase(recurso.getEstado())) {
            return dao.actualizarEstado(id, "Reservado");
        }
        return false;
    }

    public boolean reservarPorTipo(String tipo) throws SQLException {
        List<RecursoTecnologico> disponibles = dao.listarDisponibles();
        for (RecursoTecnologico recurso : disponibles) {
            if (tipo.equalsIgnoreCase(recurso.getTipo())) {
                return dao.actualizarEstado(recurso.getId(), "Reservado");
            }
        }
        return false;
    }

    public boolean agregarRecurso(RecursoTecnologico recurso) throws SQLException {
        return dao.agregar(recurso);
    }

    public boolean eliminarRecurso(String id) throws SQLException {
        return dao.eliminar(id);
    }

    // Método para liberar recurso (marcar como disponible)
    public boolean liberarRecurso(String id) throws SQLException {
        RecursoTecnologico recurso = dao.buscarPorId(id);
        if (recurso != null && "Reservado".equalsIgnoreCase(recurso.getEstado())) {
            return dao.actualizarEstado(id, "Disponible");
        }
        return false;
    }
}
