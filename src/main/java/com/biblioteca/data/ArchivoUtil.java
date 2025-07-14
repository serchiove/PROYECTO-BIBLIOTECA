package com.biblioteca.data;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ArchivoUtil {

    public static void guardarArchivo(String ruta, String contenido) throws IOException {
        try (FileWriter writer = new FileWriter(ruta)) {
            writer.write(contenido);
        }
    }

    public static String leerArchivo(String ruta) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (FileReader reader = new FileReader(ruta)) {
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
        }
        return sb.toString();
    }
}
