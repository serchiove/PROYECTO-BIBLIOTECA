package com.biblioteca.servicios;

import com.biblioteca.multimedia.LibroDigital;
import com.biblioteca.multimedia.Multimedia;

import java.util.ArrayList;
import java.util.List;

public class LibroService {

    private List<LibroDigital> libros;

    public LibroService() {
        this.libros = new ArrayList<>();
        // Cargar libros si es necesario desde JSON aquí
    }

    public void agregarLibro(LibroDigital libro) {
        libros.add(libro);
    }

    public List<Multimedia> listarLibros() {
        return new ArrayList<>(libros); // Retorna como List<Multimedia>
    }

    public LibroDigital buscarLibroPorId(String id) {
        for (LibroDigital libro : libros) {
            if (libro.getId().equalsIgnoreCase(id)) {
                return libro;
            }
        }
        return null;
    }

    public void eliminarLibro(String id) {
        libros.removeIf(libro -> libro.getId().equalsIgnoreCase(id));
    }
}
