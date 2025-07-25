package com.biblioteca.multimedia;

import com.biblioteca.interfaces.Visualizable;

public class Infografia extends Multimedia implements Visualizable {

    private String tema;

    public Infografia(String id, String titulo, String autor, String tema, boolean disponible) {
        super(id, titulo, autor, disponible);
        this.tema = tema;
    }

    @Override
    public void ver() {
        System.out.println("Visualizando infografía sobre: " + tema);
    }

    @Override
    public void mostrarInformacion() {
        System.out.printf("Infografía - ID: %s, Título: %s, Autor: %s, Tema: %s%n",
                getId(), getTitulo(), getAutor(), tema);
    }

    @Override
    public String toString() {
        return String.format("Infografía - ID: %s, Título: %s, Autor: %s, Tema: %s",
                getId(), getTitulo(), getAutor(), tema);
    }

    // getter y setter para tema (si los necesitas)
    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }
}
