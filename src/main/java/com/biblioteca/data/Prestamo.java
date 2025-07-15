package com.biblioteca.data;

import java.time.LocalDate;

public class Prestamo {
    private String id;
    private String idUsuario;
    private String idRecurso;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaDevolucion;
    private boolean devuelto;

    // Constructor
    public Prestamo(String id, String idUsuario, String idRecurso, LocalDate fechaInicio, LocalDate fechaFin) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idRecurso = idRecurso;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.devuelto = false;
        this.fechaDevolucion = null;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getIdRecurso() {
        return idRecurso;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdRecurso(String idRecurso) {
        this.idRecurso = idRecurso;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
        if (devuelto && this.fechaDevolucion == null) {
            this.fechaDevolucion = LocalDate.now();
        } else if (!devuelto) {
            this.fechaDevolucion = null;
        }
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
        this.devuelto = fechaDevolucion != null;
    }

    // Método lógico para marcar como devuelto
    public void marcarComoDevuelto() {
        this.devuelto = true;
        this.fechaDevolucion = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Préstamo [ID: " + id +
                ", Usuario: " + idUsuario +
                ", Recurso: " + idRecurso +
                ", Inicio: " + fechaInicio +
                ", Fin: " + fechaFin +
                (devuelto ? ", Devuelto: " + fechaDevolucion : ", En curso") +
                "]";
    }

    public String getIdMultimedia() {
        String idMultimedia = null;
        return idMultimedia;
    }
}
