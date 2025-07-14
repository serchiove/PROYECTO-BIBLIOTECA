package com.biblioteca.multimedia;

import com.biblioteca.interfaces.Descargable;

public class LibroDigital extends Multimedia {
    private String isbn;
    private int tamanoMB;

    public LibroDigital(String id, String titulo, String autor, String isbn, int tamanoMB, boolean disponible) {
        super(id, titulo, autor, disponible);
        this.isbn = isbn;
        this.tamanoMB = tamanoMB;
    }

    public int getTamanoMB() {
        return tamanoMB;
    }

    public void setTamanoMB(int tamanoMB) {
        this.tamanoMB = tamanoMB;
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("Libro Digital: " + titulo + " por " + autor + " (" + tamanoMB + "MB)");
    }

    @Override
    public void ver() {

    }
}

