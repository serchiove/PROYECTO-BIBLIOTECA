package com.biblioteca.servicios;

import com.biblioteca.data.MultimediaDAO;
import com.biblioteca.multimedia.Multimedia;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

public class MultimediaService {
    private final MultimediaDAO multimediaDAO;

    public MultimediaService(Connection conexion) {
        this.multimediaDAO = new MultimediaDAO(conexion);
    }

    /** Retorna todos los recursos multimedia */
    public List<Multimedia> listarRecursos() {
        try {
            return multimediaDAO.obtenerTodosLosRecursos();
        } catch (Exception e) {
            System.err.println("Error al listar recursos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /** Retorna los recursos multimedia disponibles */
    public List<Multimedia> listarRecursosDisponibles() {
        try {
            return multimediaDAO.obtenerRecursosDisponibles();
        } catch (Exception e) {
            System.err.println("Error al listar recursos disponibles: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /** Busca un recurso por su ID */
    public Multimedia buscarPorId(String id) {
        try {
            return multimediaDAO.obtenerPorId(id);
        } catch (Exception e) {
            System.err.println("Error al buscar recurso por ID: " + e.getMessage());
            return null;
        }
    }

    /** Actualiza un recurso multimedia */
    public void actualizarRecurso(Multimedia recurso) {
        try {
            multimediaDAO.actualizar(recurso);
        } catch (Exception e) {
            System.err.println("Error al actualizar recurso: " + e.getMessage());
        }
    }

    /** Marca un recurso como disponible */
    public void marcarComoDisponible(String id) {
        try {
            multimediaDAO.actualizarDisponibilidad(id, true);
        } catch (Exception e) {
            System.err.println("Error al marcar recurso como disponible: " + e.getMessage());
        }
    }

    /** Marca un recurso como no disponible */
    public void marcarComoNoDisponible(String id) {
        try {
            multimediaDAO.actualizarDisponibilidad(id, false);
        } catch (Exception e) {
            System.err.println("Error al marcar recurso como no disponible: " + e.getMessage());
        }
    }

    /** Elimina un recurso por su ID, retorna true si fue eliminado */
    public boolean eliminarRecursoPorId(String id) {
        try {
            Multimedia existente = multimediaDAO.obtenerPorId(id);
            if (existente == null) {
                return false;
            }
            multimediaDAO.eliminarRecurso(id);
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar recurso: " + e.getMessage());
            return false;
        }
    }

    /** Agrega un nuevo recurso multimedia */
    public void agregarRecurso(Multimedia recurso) {
        try {
            multimediaDAO.insertarRecurso(recurso);
        } catch (Exception e) {
            System.err.println("Error al agregar recurso: " + e.getMessage());
        }
    }

    // Método getter para acceder al DAO desde fuera
    public MultimediaDAO getMultimediaDAO() {
        return this.multimediaDAO;
    }
}
