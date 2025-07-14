package com.biblioteca.usuarios;

/**
 * Clase base abstracta para representar un usuario del sistema.
 */
public abstract class Usuario {
    protected String id;
    protected String nombre;
    protected String usuario;
    protected String password;

    public Usuario(String id, String nombre, String usuario, String password) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.password = password;
    }

    @Override
    public String toString() {
        return nombre; // o nombre + " (" + id + ")" si deseas mostrar más detalle
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPassword() {
        return password;
    }

//Devuelve el alias del usuario

    public String getNombreUsuario() {
        return usuario;
    }


//Metodo abstracto que debe devolver el rol del usuario.

    public abstract String getRol();
}
