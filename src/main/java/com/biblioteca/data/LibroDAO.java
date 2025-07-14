package com.biblioteca.data;

import com.biblioteca.multimedia.LibroDigital;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    private static final String ARCHIVO_LIBROS = "libros.json";

    public static List<LibroDigital> cargarLibros() {
        List<LibroDigital> libros = new ArrayList<>();
        try (FileReader reader = new FileReader(ARCHIVO_LIBROS)) {
            Type listType = new TypeToken<ArrayList<LibroDigital>>() {}.getType();
            libros = new Gson().fromJson(reader, listType);
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo de libros. Se usará una lista vacía.");
        }
        return libros;
    }

    public static void guardarLibros(List<LibroDigital> libros) {
        try (FileWriter writer = new FileWriter(ARCHIVO_LIBROS)) {
            new Gson().toJson(libros, writer);
        } catch (IOException e) {
            System.out.println("Error al guardar los libros: " + e.getMessage());
        }
    }
}
