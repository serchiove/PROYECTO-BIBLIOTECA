package com.biblioteca.usuarios;

public class Administrador extends Usuario {

    public Administrador() {
        super();
    }

    public Administrador(String id, String nombre, String usuario, String password) {
        super(id, nombre, usuario, password);
    }

    @Override
    public String getRol() {
        return "ADMIN";
    }
}