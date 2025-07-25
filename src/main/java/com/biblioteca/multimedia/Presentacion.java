package com.biblioteca.multimedia;

import com.biblioteca.interfaces.Visualizable;

public class Presentacion extends Multimedia implements Visualizable {

    private int numeroDiapositivas;

    // Constructor completo
    public Presentacion(String id, String titulo, String autor, int numeroDiapositivas, boolean disponible) {
        super(id, titulo, autor, disponible);
        this.numeroDiapositivas = numeroDiapositivas;
    }

    // Getter y Setter
    public int getNumeroDiapositivas() {
        return numeroDiapositivas;
    }

    public void setNumeroDiapositivas(int numeroDiapositivas) {
        this.numeroDiapositivas = numeroDiapositivas;
    }

    @Override
    public void ver() {
        System.out.println("Mostrando presentación: " + titulo);
    }

    @Override
    public void mostrarInformacion() {
        System.out.printf("Presentación - ID: %s, Título: %s, Autor: %s, Diapositivas: %d%n",
                id, titulo, autor, numeroDiapositivas);
    }

    @Override
    public String toString() {
        return String.format("Presentación - ID: %s, Título: %s, Autor: %s, Diapositivas: %d",
                id, titulo, autor, numeroDiapositivas);
    }
}
