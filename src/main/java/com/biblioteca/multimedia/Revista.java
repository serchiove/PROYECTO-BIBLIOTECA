package com.biblioteca.multimedia;

import com.biblioteca.interfaces.Descargable;

public class Revista extends Multimedia implements Descargable {

    private int numero;

    public Revista(String id, String titulo, String autor, int numero, boolean disponible) {
        super(id, titulo, autor, disponible);
        this.numero = numero;
    }

    // Getters y Setters
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override
    public void descargar() {
        System.out.println("Descargando revista: " + titulo);
    }

    @Override
    public void mostrarInformacion() {
        System.out.printf("Revista - ID: %s, Título: %s, Autor: %s, Número: %d%n",
                id, titulo, autor, numero);
    }

    @Override
    public String toString() {
        return String.format("Revista - ID: %s, Título: %s, Autor: %s, Número: %d",
                id, titulo, autor, numero);
    }

    @Override
    public void ver() {
        // Puedes implementar o dejar vacío si no es relevante
    }
}
