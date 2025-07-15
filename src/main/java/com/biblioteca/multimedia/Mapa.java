package com.biblioteca.multimedia;

import com.biblioteca.interfaces.Visualizable;

public class Mapa extends Multimedia implements Visualizable {

    private String region;

    public Mapa(String id, String titulo, String autor, String region, boolean disponible) {
        super(id, titulo, autor, disponible);
        this.region = region;
    }

    @Override
    public void ver() {
        System.out.println("Visualizando mapa de la región: " + region);
    }

    @Override
    public void mostrarInformacion() {
        System.out.printf("Mapa - ID: %s, Título: %s, Autor: %s, Región: %s%n",
                getId(), getTitulo(), getAutor(), region);
    }

    @Override
    public String toString() {
        return String.format("Mapa - ID: %s, Título: %s, Autor: %s, Región: %s",
                getId(), getTitulo(), getAutor(), region);
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
