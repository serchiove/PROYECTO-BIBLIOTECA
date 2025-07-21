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

    public boolean reservarPorId(String id) {
        try {
            RecursoTecnologico recurso = dao.buscarPorId(id);
            if (recurso == null || "Reservado".equalsIgnoreCase(recurso.getEstado())) {
                return false; // No existe o ya reservado
            }
            recurso.setEstado("Reservado");
            return dao.actualizar(recurso);
        } catch (SQLException e) {
            System.err.println("Error al reservar recurso tecnológico: " + e.getMessage());
            return false;
        }
    }

    public boolean devolverPorId(String id) {
        try {
            RecursoTecnologico recurso = dao.buscarPorId(id);
            if (recurso == null || "Disponible".equalsIgnoreCase(recurso.getEstado())) {
                return false; // No existe o ya disponible
            }
            recurso.setEstado("Disponible");
            return dao.actualizar(recurso);
        } catch (SQLException e) {
            System.err.println("Error al devolver recurso tecnológico: " + e.getMessage());
            return false;
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
    public boolean reservarPorIdYUsuario(String idRecurso, String idUsuario) {
        try {
            return dao.reservarRecursoParaUsuario(idRecurso, idUsuario);
        } catch (Exception e) {
            System.err.println("Error al reservar recurso para usuario: " + e.getMessage());
            return false;
        }
    }

    public List<RecursoTecnologico> listarTodos() {
        try {
            return dao.listarTodos();
        } catch (SQLException e) {
            System.err.println("Error al obtener recursos tecnológicos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<RecursoTecnologico> listarDisponibles() {
        try {
            return dao.listarDisponibles();
        } catch (SQLException e) {
            System.err.println("Error al listar recursos disponibles: " + e.getMessage());
            return Collections.emptyList();
        }
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
}
