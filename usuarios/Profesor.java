package com.biblioteca.usuarios;

public class Profesor extends Usuario {
    private String departamento;

    // Constructor correcto
    public Profesor(String id, String nombre, String usuarioLogin, String contrasena) {
        super(id, nombre, usuarioLogin, contrasena);
    }

    // Puedes dejar este o eliminarlo si no usas departamento aún
    public Profesor(String id, String nombre, String usuario, String password, String departamento) {
        super(id, nombre, usuario, password);
        this.departamento = departamento;
    }

    public String getRol() {
        return "PROFESOR";
    }

    // Getter y setter para departamento si lo usas
    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}