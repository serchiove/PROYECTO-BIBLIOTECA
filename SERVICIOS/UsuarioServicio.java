
package servicios;

import java.util.*;
import usuarios.Usuario;

public class UsuarioService {
    private Map<Integer, Usuario> usuarios = new HashMap<>();

    public void agregarUsuario(Usuario usuario) {
        usuarios.put(usuario.id_usuario, usuario);
        System.out.println("Usuario agregado: " + usuario.nombre);
    }

    public Usuario buscarUsuario(int id) {
        return usuarios.get(id);
    }

    public void mostrarUsuarios() {
        for (Usuario u : usuarios.values()) {
            u.verPerfil();
        }
    }
}
