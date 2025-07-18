package com.biblioteca.data;

import java.time.LocalDate;

/**
 * Representa un préstamo de recurso (multimedia o tecnológico).
 */
public class Prestamo {
    private String id;
    private String idUsuario;
    private String idRecurso;
    private LocalDate fechaInicio;
    private LocalDate fechaDevolucion;
    private LocalDate fechaLimiteDevolucion;
    private String tipoRecurso;

    // Constructor completo
    public Prestamo(String id, String idUsuario, String idRecurso,
                    LocalDate fechaInicio, LocalDate fechaDevolucion,
                    LocalDate fechaLimiteDevolucion, String tipoRecurso) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idRecurso = idRecurso;
        this.fechaInicio = fechaInicio;
        this.fechaDevolucion = fechaDevolucion;
        this.fechaLimiteDevolucion = fechaLimiteDevolucion;
        this.tipoRecurso = tipoRecurso;
    }

    // Constructor sin fechas de devolución (cuando se crea un préstamo)
    public Prestamo(String id, String idUsuario, String idRecurso,
                    LocalDate fechaInicio, String tipoRecurso) {
        this(id, idUsuario, idRecurso, fechaInicio, null, null, tipoRecurso);
    }

    // Getters y Setters
    public String getId() { return id; }
    public String getIdUsuario() { return idUsuario; }
    public String getIdRecurso() { return idRecurso; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    public LocalDate getFechaLimiteDevolucion() { return fechaLimiteDevolucion; }
    public void setFechaLimiteDevolucion(LocalDate fechaLimiteDevolucion) { this.fechaLimiteDevolucion = fechaLimiteDevolucion; }
    public String getTipoRecurso() { return tipoRecurso; }
    public void setTipoRecurso(String tipoRecurso) { this.tipoRecurso = tipoRecurso; }

    /**
     * Método adicional para obtener la fecha de finalización del préstamo.
     * Equivale a la fecha de devolución.
     */
    public LocalDate getFechaFin() {
        return fechaDevolucion;
    }

    /**
     * Indica si el préstamo ya fue devuelto.
     * @return true si fechaDevolucion no es null.
     */
    public boolean isDevuelto() {
        return fechaDevolucion != null;
    }

    /**
     * Marca el préstamo como devuelto asignando la fecha actual.
     */
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
                ", fechaLimiteDevolucion=" + fechaLimiteDevolucion +
                ", tipoRecurso='" + tipoRecurso + '\'' +
                '}';
    }
}
