package com.biblioteca.gui;

import com.biblioteca.data.RecursoTecnologicoDAO;
import com.biblioteca.servicios.MultimediaService;
import com.biblioteca.servicios.PrestamoService;
import com.biblioteca.servicios.RecursoTecnologicoService; // Importa el servicio
import com.biblioteca.servicios.UsuarioService;
import com.biblioteca.usuarios.Usuario;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

public class MenuFrame extends JFrame {
    private final Usuario usuario;
    private final PrestamoService prestamoService;
    private final MultimediaService multimediaService;
    private final UsuarioService usuarioService;
    private final Connection connection;

    // Añadimos servicio de recursos tecnológicos
    private final RecursoTecnologicoService recursoTecnologicoService;

    public MenuFrame(Usuario usuario, PrestamoService prestamoService,
                     MultimediaService multimediaService, UsuarioService usuarioService,
                     Connection connection) {
        this.usuario = usuario;
        this.prestamoService = prestamoService;
        this.multimediaService = multimediaService;
        this.usuarioService = usuarioService;
        this.connection = connection;

        // Crear DAO con conexión y pasar al Service
        RecursoTecnologicoDAO recursoTecnologicoDAO = new RecursoTecnologicoDAO(connection);
        this.recursoTecnologicoService = new RecursoTecnologicoService(recursoTecnologicoDAO);

        setTitle("Sistema de Biblioteca Virtual - UTP");
        setSize(750, 580);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        Color fondo = new Color(30, 30, 30);
        Color acento = new Color(80, 150, 255);
        Color textoClaro = new Color(230, 230, 230);

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(fondo);
        principal.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("📚 Biblioteca Virtual - UTP (" + usuario.getRol().toUpperCase() + ")");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setForeground(acento);
        titulo.setBorder(new EmptyBorder(10, 10, 20, 10));
        principal.add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBackground(fondo);
        panelBotones.setBorder(new EmptyBorder(20, 100, 20, 100));

        String rol = usuario.getRol().toUpperCase();

        if (rol.equals("ADMIN") || rol.equals("BIBLIOTECARIO")) {
            panelBotones.add(crearBoton("Gestión de Recursos Multimedia", "icons/recursos.png",
                    () -> new GestionMultimediaFrame(usuario, multimediaService).setVisible(true)));

            panelBotones.add(crearBoton("Gestionar Préstamos", "icons/prestamo.png",
                    () -> new GestionPrestamosFrame(connection, usuario).setVisible(true)));

            panelBotones.add(crearBoton("Ver Todos los Préstamos", "icons/todosprestamos.png",
                    () -> new VerTodosLosPrestamosFrame(
                            prestamoService,
                            usuarioService.getUsuarioDAO(),
                            multimediaService.getMultimediaDAO()
                    ).setVisible(true)));

            panelBotones.add(crearBoton("Gestión de Usuarios", "icons/usuarios.png",
                    () -> new GestionUsuariosFrame(usuarioService).setVisible(true)));

            // --- NUEVO BOTÓN PARA RECURSOS TECNOLÓGICOS ---
            panelBotones.add(crearBoton("Gestión de Recursos Tecnológicos", "icons/recursos_tecnologicos.png",
                    () -> new GestionRecursosTecnologicosFrame(connection).setVisible(true)));
        }

        if (rol.equals("ESTUDIANTE")) {
            panelBotones.add(crearBoton("Ver Recursos Disponibles", "icons/verrecursos.png",
                    () -> new VerRecursosFrame(usuario, prestamoService).setVisible(true)));

            panelBotones.add(crearBoton("Ver Mis Préstamos", "icons/misprestamos.png",
                    () -> new VerMisPrestamosFrame(usuario, prestamoService).setVisible(true)));

            panelBotones.add(crearBoton("Mis Recursos Interactivos", "icons/interactivos.png",
                    () -> new MisRecursosInteractivosFrame(usuario, prestamoService).setVisible(true)));

            // NUEVO: botón para que el estudiante gestione sus préstamos
            panelBotones.add(crearBoton("Gestionar mis Préstamos", "icons/prestamo.png",
                    () -> new GestionPrestamosFrame(connection, usuario).setVisible(true)));
        }

        panelBotones.add(Box.createVerticalStrut(10));

        panelBotones.add(crearBoton("Cerrar Sesión", "icons/logout.png", () -> {
            dispose();
            new LoginFrame(connection).setVisible(true);
        }));

        principal.add(panelBotones, BorderLayout.CENTER);
        add(principal);
    }

    private JButton crearBoton(String texto, String iconoRuta, Runnable accion) {
        Color fondoBoton = new Color(240, 240, 240);
        Color textoOscuro = new Color(30, 30, 30);
        Color acento = new Color(80, 150, 255);

        JButton btn = new JButton(texto, cargarIcono(iconoRuta));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(15);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(fondoBoton);
        btn.setForeground(textoOscuro);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(acento, 2),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        btn.setMaximumSize(new Dimension(400, 50));
        btn.addActionListener(e -> accion.run());

        // Efecto hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(acento);
                btn.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(fondoBoton);
                btn.setForeground(textoOscuro);
            }
        });

        return btn;
    }

    private ImageIcon cargarIcono(String ruta) {
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream(ruta);
            if (input == null) {
                System.err.println("❌ Icono no encontrado: " + ruta);
                return null;
            }
            BufferedImage imagenOriginal = ImageIO.read(input);
            Image imagenEscalada = imagenOriginal.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            return new ImageIcon(imagenEscalada);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
