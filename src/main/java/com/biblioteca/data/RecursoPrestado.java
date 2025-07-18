package com.biblioteca.data;

import com.biblioteca.multimedia.Multimedia;
import com.biblioteca.recurso.RecursoTecnologico;

public class RecursoPrestado {
    private final Prestamo prestamo;
    private final Multimedia multimedia;
    private final RecursoTecnologico recursoTecnologico;

    public RecursoPrestado(Prestamo prestamo, Multimedia multimedia) {
        this.prestamo = prestamo;
        this.multimedia = multimedia;
        this.recursoTecnologico = null;
    }

    public RecursoPrestado(Prestamo prestamo, RecursoTecnologico recursoTecnologico) {
        this.prestamo = prestamo;
        this.multimedia = null;
        this.recursoTecnologico = recursoTecnologico;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public Multimedia getMultimedia() {
        return multimedia;
    }

    public RecursoTecnologico getRecursoTecnologico() {
        return recursoTecnologico;
    }

    @Override
    public String toString() {
        if (multimedia != null) {
            return multimedia.getTitulo();
        } else if (recursoTecnologico != null) {
            return recursoTecnologico.getNombre();
        } else {
            return "Recurso desconocido";
        }
    }
}
