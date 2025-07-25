package com.biblioteca.multimedia;

import com.biblioteca.interfaces.Descargable;
import com.biblioteca.interfaces.Reproducible;
import com.biblioteca.interfaces.Visualizable;

public abstract class Multimedia implements Descargable, Reproducible, Visualizable {
    protected String id;
    protected String titulo;
    protected String autor;
    protected boolean disponible;
    protected String tipoRecurso;  // nuevo campo para distinguir tipo

    public Multimedia(String id, String titulo, String autor, boolean disponible, String tipoRecurso) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.disponible = disponible;
        this.tipoRecurso = tipoRecurso;
    }

    public Multimedia(String id, String titulo, String autor, boolean disponible) {
        this(id, titulo, autor, disponible, "multimedia"); // Tipo por defecto si no se especifica
    }

    // Getter y setter para tipoRecurso
    public String getTipoRecurso() {
        return tipoRecurso;
    }

    public void setTipoRecurso(String tipoRecurso) {
        this.tipoRecurso = tipoRecurso;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return "Título: " + titulo + ", Autor: " + autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getNombre() {
        return getTitulo(); // 'titulo' representa el "nombre" del recurso
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public abstract void mostrarInformacion();

    @Override
    public void descargar() {
        System.out.println("📥 Descargando recurso: " + titulo);
    }

    @Override
    public void reproducir() {
        System.out.println("▶️ Reproduciendo recurso: " + titulo);
    }

    @Override
    public void visualizar() {
        System.out.println("👁️ Visualizando recurso: " + titulo);
    }

    @Override
    public String toString() {
        return titulo + " (ID: " + id + ", Tipo: " + tipoRecurso + ")";
    }
}
