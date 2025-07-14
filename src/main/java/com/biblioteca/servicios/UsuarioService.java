package com.biblioteca.servicios;

import com.biblioteca.data.UsuarioDAO;
import com.biblioteca.usuarios.*;

import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    private List<Usuario> usuarios;
    private static Usuario usuarioActivo;

    public UsuarioService() {
        usuarios = UsuarioDAO.cargarUsuarios();
    }

    public static void setUsuarioActivo(Usuario usuario) {
        usuarioActivo = usuario;
    }

    public static Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    public void registrarEstudiante(String id, String nombre, String usuario, String password) {
        if (buscarUsuarioPorId(id) == null) {
            Estudiante estudiante = new Estudiante(id, nombre, usuario, password);
            usuarios.add(estudiante);
            UsuarioDAO.guardarUsuarios(usuarios);
        }
    }
    public List<Usuario> listarUsuarios() {
        return usuarios;
    }

    public void registrarBibliotecario(String id, String nombre, String usuario, String password) {
        if (buscarUsuarioPorId(id) == null) {
            Bibliotecario bibliotecario = new Bibliotecario(id, nombre, usuario, password);
            usuarios.add(bibliotecario);
            UsuarioDAO.guardarUsuarios(usuarios);
        }
    }

    public Usuario buscarUsuarioPorId(String id) {
        for (Usuario u : usuarios) {
            if (u.getId().equalsIgnoreCase(id)) {
                return u;
            }
        }
        return null;
    }

    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios);
    }
}
