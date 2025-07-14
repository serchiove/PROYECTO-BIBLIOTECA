package com.biblioteca.data;

import com.biblioteca.usuarios.Bibliotecario;
import com.biblioteca.usuarios.Estudiante;
import com.biblioteca.usuarios.Usuario;
import com.google.gson.*;

import java.io.IOException;
import java.util.*;

public class UsuarioDAO {

    private static final String RUTA = "usuarios.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<Usuario> cargarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            String json = ArchivoUtil.leerArchivo(RUTA);
            JsonArray array = JsonParser.parseString(json).getAsJsonArray();

            for (JsonElement elem : array) {
                JsonObject obj = elem.getAsJsonObject();
                String tipo = obj.get("tipo").getAsString();

                switch (tipo) {
                    case "Estudiante":
                        usuarios.add(gson.fromJson(obj, Estudiante.class));
                        break;
                    case "Bibliotecario":
                        usuarios.add(gson.fromJson(obj, Bibliotecario.class));
                        break;
                    default:
                        System.err.println("Tipo desconocido de usuario: " + tipo);
                        break;
                }
            }

        } catch (IOException | IllegalStateException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    public static void guardarUsuarios(List<Usuario> usuarios) {
        JsonArray array = new JsonArray();

        for (Usuario u : usuarios) {
            JsonObject obj = (JsonObject) gson.toJsonTree(u);
            obj.addProperty("tipo", u.getClass().getSimpleName());  // Esto asegura Estudiante o Bibliotecario
            array.add(obj);
        }

        try {
            ArchivoUtil.guardarArchivo(RUTA, gson.toJson(array));
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    public static Usuario obtenerPorId(String id) {
        List<Usuario> usuarios = cargarUsuarios();
        for (Usuario u : usuarios) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }
}
