package com.biblioteca.recurso;

import java.util.UUID;

public class RecursoTecnologico {
    private String id;
    private String tipo; // Tablet o Computadora
    private String estado; // Disponible o Reservado

    // Constructor para crear nuevo recurso con ID generado automáticamente
    public RecursoTecnologico(String tipo) {
        this.id = UUID.randomUUID().toString();
        this.tipo = tipo;
        this.estado = "Disponible";
    }

    // ✅ Constructor nuevo con boolean disponible
    public RecursoTecnologico(String tipo, boolean disponible) {
        this.id = UUID.randomUUID().toString();
        this.tipo = tipo;
        this.estado = disponible ? "Disponible" : "Reservado";
    }

    // Constructor para crear recurso con ID y estado específicos (ej. desde BD)
    public RecursoTecnologico(String id, String tipo, String estado) {
        this.id = id;
        this.tipo = tipo;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNombre() {
        return tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return tipo + " - " + estado + " (ID: " + id + ")";
    }
}