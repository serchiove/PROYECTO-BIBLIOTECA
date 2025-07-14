package com.biblioteca.usuarios;

public class Bibliotecario extends Usuario {

    public Bibliotecario(String id, String nombre, String usuario, String password) {
        super(id, nombre, usuario, password);
    }

    @Override
    public String getRol() {
        return "BIBLIOTECARIO";
    }
}
