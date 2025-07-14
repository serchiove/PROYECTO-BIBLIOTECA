package com.biblioteca.data;

import com.biblioteca.multimedia.*;
import com.google.gson.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultimediaDAO {
    private static final String RUTA = "multimedia.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<Multimedia> cargarRecursos() {
        List<Multimedia> recursos = new ArrayList<>();
        try {
            String json = ArchivoUtil.leerArchivo(RUTA);
            if (json == null || json.isBlank()) return recursos;

            JsonArray array = JsonParser.parseString(json).getAsJsonArray();

            for (JsonElement e : array) {
                JsonObject obj = e.getAsJsonObject();
                String id = obj.get("id").getAsString();
                String titulo = obj.get("titulo").getAsString();
                String autor = obj.get("autor").getAsString();
                boolean disponible = obj.has("disponible") && obj.get("disponible").getAsBoolean();

                // Detección de tipo según campos presentes
                if (obj.has("isbn")) {
                    String isbn = obj.get("isbn").getAsString();
                    int tamanoMB = obj.get("tamanoMB").getAsInt();
                    recursos.add(new LibroDigital(id, titulo, autor, isbn, tamanoMB, disponible));
                } else if (obj.has("duracionSegundos")) {
                    int duracion = obj.get("duracionSegundos").getAsInt();
                    recursos.add(new Video(id, titulo, autor, duracion, disponible));
                } else if (obj.has("duracionMinutos")) {
                    int duracion = obj.get("duracionMinutos").getAsInt();
                    recursos.add(new Audio(id, titulo, autor, duracion, disponible));
                } else if (obj.has("region")) {
                    String region = obj.get("region").getAsString();
                    recursos.add(new Mapa(id, titulo, autor, region, disponible));
                } else if (obj.has("tema")) {
                    String tema = obj.get("tema").getAsString();
                    recursos.add(new Infografia(id, titulo, autor, tema, disponible));
                } else if (obj.has("numeroDiapositivas")) {
                    int slides = obj.get("numeroDiapositivas").getAsInt();
                    recursos.add(new Presentacion(id, titulo, autor, slides, disponible));
                } else if (obj.has("numero")) {
                    int numero = obj.get("numero").getAsInt();
                    recursos.add(new Revista(id, titulo, autor, numero, disponible));
                } else if (obj.has("universidad")) {
                    String universidad = obj.get("universidad").getAsString();
                    recursos.add(new Tesis(id, titulo, autor, universidad, disponible));
                }
            }

        } catch (IOException | IllegalStateException ex) {
            System.err.println("Error leyendo multimedia.json: " + ex.getMessage());
        }

        return recursos;
    }

    public static void guardarRecursos(List<Multimedia> recursos) {
        JsonArray array = new JsonArray();
        for (Multimedia m : recursos) {
            JsonObject obj = (JsonObject) gson.toJsonTree(m);
            array.add(obj);
        }

        try {
            ArchivoUtil.guardarArchivo(RUTA, gson.toJson(array));
        } catch (IOException e) {
            System.err.println("Error guardando multimedia.json: " + e.getMessage());
        }
    }

    public static Multimedia obtenerPorId(String id) {
        List<Multimedia> recursos = cargarRecursos();
        for (Multimedia m : recursos) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }
}
