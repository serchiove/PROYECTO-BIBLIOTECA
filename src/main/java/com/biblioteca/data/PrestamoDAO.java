package com.biblioteca.data;

import com.biblioteca.data.Prestamo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    private static final String ARCHIVO = "data/prestamos.json";

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    public static List<Prestamo> cargarPrestamos() {
        try (Reader reader = new FileReader(ARCHIVO)) {
            Type listType = new TypeToken<List<Prestamo>>(){}.getType();
            List<Prestamo> prestamos = gson.fromJson(reader, listType);
            return prestamos != null ? prestamos : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static void guardarPrestamos(List<Prestamo> prestamos) {
        try (Writer writer = new FileWriter(ARCHIVO)) {
            gson.toJson(prestamos, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
