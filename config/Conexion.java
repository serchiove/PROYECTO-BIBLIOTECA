package com.biblioteca.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:sqlite:biblioteca.db"; // Archivo de base de datos SQLite
    private static Connection conexion;

    public static Connection getConexion() {
        if (conexion == null) {
            try {
                Class.forName("org.sqlite.JDBC"); // Cargar el driver de SQLite
                conexion = DriverManager.getConnection(URL);
                System.out.println("✅ Conexión establecida con la base de datos SQLite.");
            } catch (ClassNotFoundException e) {
                System.err.println("❌ No se encontró el driver de SQLite: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("❌ Error al conectar con SQLite: " + e.getMessage());
            }
        }
        return conexion;
    }

    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                System.out.println("🔒 Conexión SQLite cerrada.");
            } catch (SQLException e) {
                System.err.println("❌ Error al cerrar la conexión SQLite: " + e.getMessage());
            }
        }
    }
}