package com.biblioteca.usuarios;

public class Profesor extends Usuario {
    private String departamento;

    public Profesor(String id, String nombre, String usuarioLogin, String contrasena) {
        super();
    }

    public Profesor(String id, String nombre, String usuario, String password, String departamento) {
        super(id, nombre, usuario, password);
    }


    public String getRol() {
        return "PROFESOR";
    }

}
