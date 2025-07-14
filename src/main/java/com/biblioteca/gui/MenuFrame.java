package com.biblioteca.gui;

import com.biblioteca.servicios.MultimediaService;
import com.biblioteca.servicios.PrestamoService;
import com.biblioteca.servicios.UsuarioService;
import com.biblioteca.usuarios.Usuario;

import javax.swing.*;
import java.awt.*;

public class MenuFrame extends JFrame {
    private Usuario usuario;
    private PrestamoService prestamoService;
    private MultimediaService multimediaService;
    private UsuarioService usuarioService;

    public MenuFrame(Usuario usuario, PrestamoService prestamoService,
                     MultimediaService multimediaService, UsuarioService usuarioService) {
        this.usuario = usuario;
        this.prestamoService = prestamoService;
        this.multimediaService = multimediaService;
        this.usuarioService = usuarioService;

        setTitle("Sistema de Biblioteca Virtual - UTP");
        setSize(750, 580);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        // Colores institucionales UTP
        Color rojoUTP = new Color(200, 16, 46); // #C8102E
        Color fondoClaro = new Color(245, 245, 245);
        Color textoOscuro = new Color(40, 40, 40);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(fondoClaro);
        principal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("📚 Biblioteca Virtual - UTP (" + usuario.getRol().toUpperCase() + ")");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setForeground(rojoUTP);
        principal.add(titulo, BorderLayout.NORTH);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 15, 15));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        panelBotones.setBackground(Color.WHITE);

        String rol = usuario.getRol().toUpperCase();

        if (rol.equals("ADMIN") || rol.equals("BIBLIOTECARIO")) {
            panelBotones.add(crearBoton("🎞 Gestión de Recursos Multimedia", rojoUTP, () -> {
                new GestionMultimediaFrame(usuario).setVisible(true);
            }));

            panelBotones.add(crearBoton("📖 Gestionar Préstamos", rojoUTP, () -> {
                new GestionPrestamosFrame().setVisible(true);
            }));

            panelBotones.add(crearBoton("📋 Ver Todos los Préstamos", rojoUTP, () -> {
                new VerTodosLosPrestamosFrame(prestamoService).setVisible(true);
            }));

            panelBotones.add(crearBoton("👤 Gestión de Usuarios", rojoUTP, () -> {
                new GestionUsuariosFrame().setVisible(true);
            }));
        }

        if (rol.equals("ESTUDIANTE")) {
            panelBotones.add(crearBoton("📚 Ver Recursos Disponibles", rojoUTP, () -> {
                new VerRecursosFrame(usuario, prestamoService).setVisible(true);
            }));

            panelBotones.add(crearBoton("📋 Ver Mis Préstamos", rojoUTP, () -> {
                new VerMisPrestamosFrame(usuario, prestamoService).setVisible(true);
            }));

            panelBotones.add(crearBoton("💻 Mis Recursos Interactivos", rojoUTP, () -> {
                new MisRecursosInteractivosFrame(usuario, prestamoService).setVisible(true);
            }));

            panelBotones.add(crearBoton("📖 Gestionar Préstamos", rojoUTP, () -> {
                new GestionPrestamosFrame(usuario, prestamoService).setVisible(true);
            }));
        }

        // Botón cerrar sesión
        panelBotones.add(crearBoton("Cerrar Sesión", new Color(120, 120, 120), () -> {
            dispose();
            new LoginFrame();
        }));

        principal.add(panelBotones, BorderLayout.CENTER);
        add(principal);
        setVisible(true);
    }

    private JButton crearBoton(String texto, Color color, Runnable accion) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        btn.addActionListener(e -> accion.run());
        return btn;
    }
}
