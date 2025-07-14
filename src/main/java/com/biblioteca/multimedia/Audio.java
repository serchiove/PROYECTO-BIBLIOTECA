package com.biblioteca.multimedia;

import com.biblioteca.interfaces.Reproducible;

public class Audio extends Multimedia implements Reproducible {

    private int duracionMinutos;

    public Audio(String id, String titulo, String autor, int duracionMinutos, boolean disponible) {
        super(id, titulo, autor, disponible);
        this.duracionMinutos = duracionMinutos;
    }

    @Override
    public void reproducir() {
        System.out.println("Reproduciendo audio: " + titulo);
    }

    @Override
    public void mostrarInformacion() {
        System.out.printf("Audio - ID: %s, Título: %s, Autor: %s, Duración: %d minutos%n",
                id, titulo, autor, duracionMinutos);
    }

    @Override
    public String toString() {
        return String.format("Audio - ID: %s, Título: %s, Autor: %s, Duración: %d minutos",
                id, titulo, autor, duracionMinutos);
    }

    @Override
    public void ver() {

    }
}
