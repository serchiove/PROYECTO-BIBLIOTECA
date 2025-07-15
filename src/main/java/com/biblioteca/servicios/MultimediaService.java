package com.biblioteca.servicios;

import com.biblioteca.data.MultimediaDAO;
import com.biblioteca.multimedia.Multimedia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class MultimediaService {

    private final MultimediaDAO multimediaDAO;

    public MultimediaService(Connection conexion) {
        this.multimediaDAO = new MultimediaDAO(conexion);
    }

    public List<Multimedia> listarRecursos() {
        return multimediaDAO.obtenerTodosLosRecursos();
    }

    public Multimedia buscarPorId(String id) {
        return multimediaDAO.obtenerPorId(id);
    }

    public List<Multimedia> listarRecursosDisponibles() {
        return multimediaDAO.obtenerRecursosDisponibles();
    }

    // NO expongas directamente el DAO, mejor encapsula aquí los métodos necesarios

    public void agregarRecurso(Multimedia recurso) throws SQLException {
        multimediaDAO.insertarRecurso(recurso);
    }

    public void actualizarRecurso(Multimedia recurso) {
        multimediaDAO.actualizar(recurso);
    }

    public void marcarComoDisponible(String id) {
        multimediaDAO.actualizarDisponibilidad(id, true);
    }

    // Método para eliminar recurso que retorna boolean para saber si eliminó o no
    public boolean eliminarRecursoPorId(String id) {
        Multimedia existente = multimediaDAO.obtenerPorId(id);
        if (existente == null) {
            return false; // No existe recurso con ese ID
        }
        multimediaDAO.eliminarRecurso(id);
        return true;
    }

    public MultimediaDAO getMultimediaDAO() {
        return null;
    }
}
