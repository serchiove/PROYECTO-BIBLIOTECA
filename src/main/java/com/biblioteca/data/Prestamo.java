package com.biblioteca.data;

import java.time.LocalDate;

public class Prestamo {
    private String id;
    private String idUsuario;
    private String idRecurso;
    private LocalDate fechaInicio;
    private LocalDate fechaDevolucion;
    private LocalDate fechaFin;
    private String tipoRecurso;

    public Prestamo(String id, String idUsuario, String idRecurso,
                    LocalDate fechaInicio, LocalDate fechaDevolucion,
                    LocalDate fechaFin, String tipoRecurso) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idRecurso = idRecurso;
        this.fechaInicio = fechaInicio;
        this.fechaDevolucion = fechaDevolucion;
        this.fechaFin = fechaFin;
        this.tipoRecurso = tipoRecurso;
    }

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

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getTipoRecurso() {
        return tipoRecurso;
    }

    public void setTipoRecurso(String tipoRecurso) {
        this.tipoRecurso = tipoRecurso;
    }

    public boolean isDevuelto() {
        return fechaDevolucion != null;
    }

    public void marcarComoDevuelto() {
        this.fechaDevolucion = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "id='" + id + '\'' +
                ", idUsuario='" + idUsuario + '\'' +
                ", idRecurso='" + idRecurso + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaDevolucion=" + fechaDevolucion +
                ", fechaFin=" + fechaFin +
                ", tipoRecurso='" + tipoRecurso + '\'' +
                '}';
    }
}
