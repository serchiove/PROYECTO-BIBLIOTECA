package com.biblioteca.gui;

import com.biblioteca.data.PrestamoDAO;
import com.biblioteca.servicios.AutenticacionService;
import com.biblioteca.servicios.MultimediaService;
import com.biblioteca.servicios.PrestamoService;
import com.biblioteca.servicios.UsuarioService;
import com.biblioteca.usuarios.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoPassword;
    private final AutenticacionService authService;

    public LoginFrame() {
        super("Inicio de sesión - Biblioteca");
        authService = new AutenticacionService();
        initUI();
    }

    private void initUI() {
        Color rojoUTP = new Color(183, 28, 28);
        Color grisClaro = new Color(245, 245, 245);
        Color grisIntermedio = new Color(220, 220, 220);

        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(grisClaro);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("INICIO DE SESIÓN - BIBLIOTECA", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(rojoUTP);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new GridLayout(3, 2, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        panelCentral.setBackground(grisClaro);

        panelCentral.add(new JLabel("Usuario:"));
        campoUsuario = new JTextField();
        panelCentral.add(campoUsuario);

        panelCentral.add(new JLabel("Contraseña:"));
        campoPassword = new JPasswordField();
        panelCentral.add(campoPassword);

        panelCentral.add(new JLabel());
        JButton botonLogin = new JButton("Ingresar");
        botonLogin.setBackground(rojoUTP);
        botonLogin.setForeground(Color.WHITE);
        botonLogin.setFocusPainted(false);
        botonLogin.addActionListener(e -> autenticar());
        panelCentral.add(botonLogin);

        add(panelCentral, BorderLayout.CENTER);
        setVisible(true);
    }

    private void autenticar() {
        String usuario = campoUsuario.getText();
        String password = new String(campoPassword.getPassword());

        Usuario user = authService.autenticar(usuario, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Bienvenido, " + user.getNombre());

            UsuarioService usuarioService = new UsuarioService();
            MultimediaService multimediaService = new MultimediaService();
            PrestamoService prestamoService = new PrestamoService(new PrestamoDAO(), multimediaService, usuarioService);

            dispose(); // Cerrar login
            new MenuFrame(user, prestamoService, multimediaService, usuarioService).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas");
        }
    }
}
