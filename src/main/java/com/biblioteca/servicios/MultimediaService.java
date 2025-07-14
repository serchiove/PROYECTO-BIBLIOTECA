package com.biblioteca.servicios;

import com.biblioteca.data.MultimediaDAO;
import com.biblioteca.multimedia.Multimedia;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultimediaService {

    protected List<Multimedia> recursos;

    public MultimediaService() {
        this.recursos = MultimediaDAO.cargarRecursos(); // Carga inicial
    }

    public void agregarRecurso(Multimedia recurso) {
        recursos.add(recurso);
        guardarRecursos();
    }

    public List<Multimedia> listarRecursos() {
        return new ArrayList<>(recursos); // copia segura
    }

    public List<Multimedia> listarRecursosDisponibles() {
        return recursos.stream()
                .filter(Multimedia::isDisponible)
                .collect(Collectors.toList());
    }

    public List<Multimedia> getRecursos() {
        return recursos;
    }

    public Multimedia obtenerPorId(String id) {
        for (Multimedia r : recursos) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }

    public List<Multimedia> listarTodos() {
        return MultimediaDAO.cargarRecursos(); // recarga completa desde JSON
    }

    public void actualizarRecurso(Multimedia recursoActualizado) {
        for (int i = 0; i < recursos.size(); i++) {
            if (recursos.get(i).getId().equals(recursoActualizado.getId())) {
                recursos.set(i, recursoActualizado);
                guardarRecursos();
                break;
            }
        }
    }

    public void marcarComoDisponible(String idRecurso) {
        Multimedia recurso = obtenerPorId(idRecurso);
        if (recurso != null) {
            recurso.setDisponible(true);
            actualizarRecurso(recurso);
        }
    }


    protected void guardarRecursos() {
        MultimediaDAO.guardarRecursos(recursos);
    }
}
