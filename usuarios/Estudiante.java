package com.biblioteca.usuarios;

public class Estudiante extends Usuario {
    private String carrera;

    public Estudiante() {
        super();
    }

    public Estudiante(String id, String nombre, String usuario, String password) {
        super(id, nombre, usuario, password);
    }

    @Override
    public String getRol() {
        return "ESTUDIANTE";
    }
}