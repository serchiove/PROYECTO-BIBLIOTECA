package com.biblioteca.multimedia;

import com.biblioteca.interfaces.Reproducible;

public class Video extends Multimedia implements Reproducible {

    private int duracionSegundos;

    public Video(String id, String titulo, String autor, int duracionSegundos, boolean disponible) {
        super(id, titulo, autor, disponible);
        this.duracionSegundos = duracionSegundos;
    }

    // Opcional constructor vacío solo si lo necesitas
    // public Video() {
    //     super(null, null, null, true);
    // }

    @Override
    public void reproducir() {
        System.out.println("Reproduciendo video: " + titulo);
    }

    @Override
    public void mostrarInformacion() {
        System.out.printf("Video - ID: %s, Título: %s, Autor: %s, Duración: %d segundos%n",
                id, titulo, autor, duracionSegundos);
    }

    public int getDuracionSegundos() {
        return duracionSegundos;
    }

    public void setDuracionSegundos(int duracionSegundos) {
        this.duracionSegundos = duracionSegundos;
    }

    @Override
    public String toString() {
        return String.format("Video - ID: %s, Título: %s, Autor: %s, Duración: %d segundos",
                id, titulo, autor, duracionSegundos);
    }

    @Override
    public void ver() {
        reproducir();
    }
}
