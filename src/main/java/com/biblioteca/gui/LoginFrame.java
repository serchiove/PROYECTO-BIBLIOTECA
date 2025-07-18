package com.biblioteca.gui;

import com.biblioteca.data.PrestamoDAO;
import com.biblioteca.data.PrestamoRecursoTecnologicoDAO;
import com.biblioteca.data.RecursoTecnologicoDAO;
import com.biblioteca.servicios.AutenticacionService;
import com.biblioteca.servicios.MultimediaService;
import com.biblioteca.servicios.PrestamoService;
import com.biblioteca.servicios.RecursoTecnologicoService;
import com.biblioteca.servicios.UsuarioService;
import com.biblioteca.usuarios.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;

public class LoginFrame extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoPassword;

    private final Connection connection;
    private UsuarioService usuarioService;
    private MultimediaService multimediaService;
    private PrestamoService prestamoService;
    private AutenticacionService authService;

    public LoginFrame(Connection connection) {
        super("Inicio de sesión");
        this.connection = connection;
        inicializarServicios();
        initUI();
    }

    private void inicializarServicios() {
        this.usuarioService = new UsuarioService(connection);
        this.multimediaService = new MultimediaService(connection);
        // Crear DAOs y servicios necesarios para el constructor de PrestamoService:
        PrestamoDAO prestamoDAO = new PrestamoDAO(connection);
        PrestamoRecursoTecnologicoDAO prestamoRecTecDAO = new PrestamoRecursoTecnologicoDAO(connection);
        RecursoTecnologicoService recursoTecnologicoService = new RecursoTecnologicoService(new RecursoTecnologicoDAO(connection));

        this.prestamoService = new PrestamoService(
                prestamoDAO,
                prestamoRecTecDAO,
                multimediaService,
                recursoTecnologicoService,
                usuarioService
        );

        this.authService = new AutenticacionService(connection);
    }

    private void initUI() {
        Color fondo = new Color(30, 30, 30);
        Color acento = new Color(80, 150, 255);
        Color textoClaro = new Color(220, 220, 220);

        setSize(450, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(fondo);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(acento);
        titulo.setBorder(new EmptyBorder(20, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(new EmptyBorder(20, 50, 20, 50));
        panelCentral.setBackground(fondo);

        campoUsuario = crearCampoConIcono("👤", "Usuario", panelCentral);
        campoPassword = (JPasswordField) crearCampoConIcono("🔒", "Contraseña", panelCentral);

        panelCentral.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton botonLogin = new JButton("Ingresar");
        botonLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonLogin.setBackground(acento);
        botonLogin.setForeground(Color.BLACK);
        botonLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        botonLogin.setFocusPainted(false);
        botonLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonLogin.setPreferredSize(new Dimension(300, 40));
        botonLogin.setMaximumSize(new Dimension(300, 40));
        botonLogin.addActionListener(e -> autenticar());

        panelCentral.add(botonLogin);
        add(panelCentral, BorderLayout.CENTER);

        setVisible(true);
    }

    private JTextField crearCampoConIcono(String icono, String nombreCampo, JPanel panelCentral) {
        Color fondo = new Color(40, 40, 40);
        Color texto = new Color(230, 230, 230);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setMaximumSize(new Dimension(300, 50));
        contenedor.setBackground(fondo);
        contenedor.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Panel izquierdo con icono + nombre
        JPanel panelEtiqueta = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelEtiqueta.setBackground(fondo);

        JLabel lblIcono = new JLabel(icono);
        lblIcono.setForeground(texto);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        panelEtiqueta.add(lblIcono);

        JLabel lblTexto = new JLabel(nombreCampo);
        lblTexto.setForeground(texto);
        lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelEtiqueta.add(lblTexto);

        contenedor.add(panelEtiqueta, BorderLayout.WEST);

        JTextField campo = nombreCampo.equals("Contraseña") ? new JPasswordField() : new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campo.setForeground(texto);
        campo.setBackground(fondo);
        campo.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        campo.setCaretColor(texto);
        campo.setPreferredSize(new Dimension(250, 40));
        campo.setToolTipText(nombreCampo);
        contenedor.add(campo, BorderLayout.CENTER);

        panelCentral.add(contenedor);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 15)));

        return campo;
    }

    private void autenticar() {
        String usuario = campoUsuario.getText().trim();
        String password = new String(campoPassword.getPassword()).trim();

        Usuario user = authService.autenticar(usuario, password);
        if (user != null) {
            UsuarioService.setUsuarioActivo(user);
            JOptionPane.showMessageDialog(this, "Bienvenido, " + user.getNombre());
            dispose();
            new MenuFrame(user, prestamoService, multimediaService, usuarioService, connection).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
