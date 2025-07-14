package com.biblioteca.multimedia;

import com.biblioteca.interfaces.Descargable;
import com.biblioteca.interfaces.Reproducible;
import com.biblioteca.interfaces.Visualizable;

/**
 * Clase base abstracta para representar un recurso multimedia.
 */
public abstract class Multimedia implements Descargable, Reproducible, Visualizable {
    protected String id;
    protected String titulo;
    protected String autor;
    protected boolean disponible;

    public Multimedia(String id, String titulo, String autor, boolean disponible) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.disponible = disponible;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /**
     * Muestra la información detallada del recurso.
     */
    public abstract void mostrarInformacion();

    /**
     * Muestra el recurso en una descarga simulada.
     */
    @Override
    public void descargar() {
        System.out.println("📥 Descargando recurso: " + titulo);
    }

    /**
     * Muestra el recurso en una reproducción simulada.
     */
    @Override
    public void reproducir() {
        System.out.println("▶️ Reproduciendo recurso: " + titulo);
    }

    /**
     * Muestra el recurso en una visualización simulada.
     */
    @Override
    public void visualizar() {
        System.out.println("👁️ Visualizando recurso: " + titulo);
    }

    /**
     * Para mostrar correctamente en JComboBox o listas.
     */
    @Override
    public String toString() {
        return titulo + " (" + autor + ")";
    }
}
