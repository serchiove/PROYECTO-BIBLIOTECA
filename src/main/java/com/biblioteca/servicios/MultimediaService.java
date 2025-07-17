package com.biblioteca.servicios;

import com.biblioteca.data.MultimediaDAO;
import com.biblioteca.multimedia.Multimedia;

import java.sql.Connection;
import java.util.List;

public class MultimediaService {
    private final MultimediaDAO multimediaDAO;

    public MultimediaService(Connection conexion) {
        this.multimediaDAO = new MultimediaDAO(conexion);
    }

    public List<Multimedia> listarRecursos() {
        return multimediaDAO.obtenerTodosLosRecursos();
    }

    public List<Multimedia> listarRecursosDisponibles() {
        return multimediaDAO.obtenerRecursosDisponibles();
    }

    public Multimedia buscarPorId(String id) {
        return multimediaDAO.obtenerPorId(id);
    }

    public void actualizarRecurso(Multimedia recurso) {
        multimediaDAO.actualizar(recurso);
    }

    public void marcarComoDisponible(String id) {
        multimediaDAO.actualizarDisponibilidad(id, true);
    }

    public boolean eliminarRecursoPorId(String id) {
        Multimedia existente = multimediaDAO.obtenerPorId(id);
        if (existente == null) {
            return false;
        }
        multimediaDAO.eliminarRecurso(id);
        return true;
    }

    public void agregarRecurso(Multimedia recurso) {
        multimediaDAO.insertarRecurso(recurso);
    }

    // Método para marcar como NO disponible
    public void marcarComoNoDisponible(String id) {
        multimediaDAO.actualizarDisponibilidad(id, false);
    }
}
