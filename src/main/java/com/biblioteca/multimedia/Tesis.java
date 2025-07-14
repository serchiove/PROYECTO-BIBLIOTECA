package com.biblioteca.multimedia;

import com.biblioteca.interfaces.Descargable;

public class Tesis extends Multimedia implements Descargable {

    private String universidad;

    public Tesis(String id, String titulo, String autor, String universidad, boolean disponible) {
        super(id, titulo, autor, disponible);
        this.universidad = universidad;
    }
    @Override
    public void descargar() {
        System.out.println("Descargando tesis de: " + universidad);
    }

    @Override
    public void mostrarInformacion() {
        System.out.printf("Tesis - ID: %s, Título: %s, Autor: %s, Universidad: %s%n",
                id, titulo, autor, universidad);
    }
    @Override
    public String toString() {
        return String.format("Tesis - ID: %s, Título: %s, Autor: %s, Universidad: %s",
                id, titulo, autor, universidad);
    }

    @Override
    public void ver() {

    }
}
