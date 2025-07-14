package com.biblioteca.servicios;

import com.biblioteca.usuarios.*;
import com.google.gson.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AutenticacionService {

    private List<Usuario> usuarios;

    public AutenticacionService() {
        this.usuarios = cargarUsuariosDesdeJSON("Usuarios.json");
    }

    public Usuario autenticar(String usuarioIngresado, String passwordIngresado) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsuario().equals(usuarioIngresado) &&
                    usuario.getPassword().equals(passwordIngresado)) {
                return usuario;
            }
        }
        return null;
    }

    private List<Usuario> cargarUsuariosDesdeJSON(String archivo) {
        List<Usuario> listaUsuarios = new ArrayList<>();
        try (FileReader reader = new FileReader(archivo)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement elem : jsonArray) {
                JsonObject obj = elem.getAsJsonObject();

                if (!obj.has("tipo") || !obj.has("id") || !obj.has("nombre") ||
                        !obj.has("usuario") || !obj.has("password")) {
                    System.out.println("Usuario con campos faltantes: " + obj);
                    continue; // saltar este usuario
                }

                String tipo = obj.get("tipo").getAsString();
                String id = obj.get("id").getAsString();
                String nombre = obj.get("nombre").getAsString();
                String usuario = obj.get("usuario").getAsString();
                String password = obj.get("password").getAsString();

                Usuario u = switch (tipo.toUpperCase()) {
                    case "ADMIN" -> new Administrador(id, nombre, usuario, password);
                    case "BIBLIOTECARIO" -> new Bibliotecario(id, nombre, usuario, password);
                    case "ESTUDIANTE" -> new Estudiante(id, nombre, usuario, password);
                    default -> {
                        System.out.println("tipo desconocido: " + tipo);
                        yield null;
                    }
                };

                if (u != null) listaUsuarios.add(u);
            }

        } catch (IOException e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
        } catch (IllegalStateException | JsonSyntaxException e) {
            System.out.println("Formato JSON inválido en usuarios: " + e.getMessage());
        }

        return listaUsuarios;
    }
}
