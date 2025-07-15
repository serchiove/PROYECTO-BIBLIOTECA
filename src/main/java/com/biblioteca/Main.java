package com.biblioteca;

import com.biblioteca.config.Conexion;
import com.biblioteca.gui.LoginFrame;

import javax.swing.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        // Establecer el look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Obtener conexión a la base de datos
        Connection connection = Conexion.getConexion();

        if (connection == null) {
            JOptionPane.showMessageDialog(null, "❌ Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Iniciar el LoginFrame (controla si se abre el menú o no)
        SwingUtilities.invokeLater(() -> new LoginFrame(connection));
    }
}
