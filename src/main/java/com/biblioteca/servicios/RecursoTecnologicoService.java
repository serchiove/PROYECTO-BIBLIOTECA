package com.biblioteca.servicios;

import com.biblioteca.data.RecursoTecnologicoDAO;
import com.biblioteca.recurso.RecursoTecnologico;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class RecursoTecnologicoService {

    private final RecursoTecnologicoDAO dao;

    public RecursoTecnologicoService(RecursoTecnologicoDAO dao) {
        this.dao = dao;
    }

    public RecursoTecnologico buscarPorId(String id) {
        try {
            return dao.buscarPorId(id);
        } catch (SQLException e) {
            System.err.println("Error al buscar recurso tecnológico por ID: " + e.getMessage());
            return null;
        }
    }

    public boolean actualizarRecursoTecnologico(RecursoTecnologico recurso) {
        try {
            return dao.actualizar(recurso);
        } catch (SQLException e) {
            System.err.println("Error al actualizar recurso tecnológico: " + e.getMessage());
            return false;
        }
    }

    public boolean marcarComoDisponible(String id) {
        try {
            RecursoTecnologico recurso = dao.buscarPorId(id);
            if (recurso != null && !"Disponible".equalsIgnoreCase(recurso.getEstado())) {
                return dao.actualizarEstado(id, "Disponible");
            }
        } catch (SQLException e) {
            System.err.println("Error al marcar recurso tecnológico como disponible: " + e.getMessage());
        }
        return false;
    }

    public List<RecursoTecnologico> getTodos() {
        try {
            return dao.listarTodos();
        } catch (SQLException e) {
            System.err.println("Error al listar todos los recursos tecnológicos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<RecursoTecnologico> listarDisponibles() {
        try {
            return dao.listarDisponibles();
        } catch (SQLException e) {
            System.err.println("Error al listar recursos tecnológicos disponibles: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean reservarRecurso(String id) {
        try {
            RecursoTecnologico recurso = dao.buscarPorId(id);
            if (recurso != null && "Disponible".equalsIgnoreCase(recurso.getEstado())) {
                return dao.actualizarEstado(id, "Reservado");
            }
        } catch (SQLException e) {
            System.err.println("Error al reservar recurso tecnológico: " + e.getMessage());
        }
        return false;
    }

    public boolean reservarPorTipo(String tipo) {
        try {
            List<RecursoTecnologico> disponibles = dao.listarDisponibles();
            for (RecursoTecnologico recurso : disponibles) {
                if (tipo.equalsIgnoreCase(recurso.getTipo())) {
                    return dao.actualizarEstado(recurso.getId(), "Reservado");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al reservar recurso tecnológico por tipo: " + e.getMessage());
        }
        return false;
    }

    public boolean agregarRecurso(RecursoTecnologico recurso) {
        try {
            return dao.agregar(recurso);
        } catch (SQLException e) {
            System.err.println("Error al agregar recurso tecnológico: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarRecurso(String id) {
        try {
            return dao.eliminar(id);
        } catch (SQLException e) {
            System.err.println("Error al eliminar recurso tecnológico: " + e.getMessage());
            return false;
        }
    }

    /** Método para liberar recurso (marcar como disponible) */
    public boolean liberarRecurso(String id) {
        try {
            RecursoTecnologico recurso = dao.buscarPorId(id);
            if (recurso != null && "Reservado".equalsIgnoreCase(recurso.getEstado())) {
                return dao.actualizarEstado(id, "Disponible");
            }
        } catch (SQLException e) {
            System.err.println("Error al liberar recurso tecnológico: " + e.getMessage());
        }
        return false;
    }
}
