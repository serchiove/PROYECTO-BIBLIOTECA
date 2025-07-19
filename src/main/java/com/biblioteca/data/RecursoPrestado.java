package com.biblioteca.data;

import com.biblioteca.multimedia.Multimedia;
import com.biblioteca.recurso.RecursoTecnologico;

public class RecursoPrestado {
    private Prestamo prestamo;
    private Multimedia multimedia;
    private RecursoTecnologico tecnologico;

    public RecursoPrestado(Prestamo prestamo, Multimedia multimedia) {
        this.prestamo = prestamo;
        this.multimedia = multimedia;
        this.tecnologico = null;
    }

    public RecursoPrestado(Prestamo prestamo, RecursoTecnologico tecnologico) {
        this.prestamo = prestamo;
        this.multimedia = null;
        this.tecnologico = tecnologico;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public Multimedia getMultimedia() {
        return multimedia;
    }

    public RecursoTecnologico getTecnologico() {
        return tecnologico;
    }

    // ✅ Este método te permite obtener directamente el ID del préstamo
    public String getIdPrestamo() {
        return prestamo != null ? prestamo.getId() : null;
    }

    @Override
    public String toString() {
        if (multimedia != null) {
            return "[Multimedia] " + multimedia.getTitulo();
        } else if (tecnologico != null) {
            return "[Tecnológico] " + tecnologico.getNombre();
        } else {
            return "[Recurso desconocido]";
        }
    }
}
