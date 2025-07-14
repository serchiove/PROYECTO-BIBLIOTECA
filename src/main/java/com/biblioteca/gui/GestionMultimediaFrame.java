package com.biblioteca.gui;

import com.biblioteca.multimedia.*;
import com.biblioteca.servicios.MultimediaService;
import com.biblioteca.usuarios.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GestionMultimediaFrame extends JFrame {

    private final MultimediaService multimediaService;
    private final JTextArea areaResultado;
    private final Usuario usuario;

    public GestionMultimediaFrame(Usuario usuario) {
        super("Gestión de Recursos Multimedia");
        this.usuario = usuario;
        this.multimediaService = new MultimediaService();

        setSize(600, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Color rojoUTP = new Color(191, 22, 22);
        Color grisClaro = new Color(245, 245, 245);

        // Panel superior con botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(grisClaro);

        JButton btnAgregar = crearBoton("➕ Agregar Recurso", rojoUTP);
        JButton btnBuscar = crearBoton("🔍 Buscar por ID", rojoUTP);
        JButton btnListar = crearBoton("📄 Listar Todo", rojoUTP);

        btnAgregar.addActionListener(e -> agregarRecurso());
        btnBuscar.addActionListener(e -> buscarPorId());
        btnListar.addActionListener(e -> listarRecursos());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnListar);
        add(panelBotones, BorderLayout.NORTH);

        // Área de resultados
        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaResultado.setBackground(Color.WHITE);
        areaResultado.setForeground(Color.DARK_GRAY);
        JScrollPane scrollPane = new JScrollPane(areaResultado);
        add(scrollPane, BorderLayout.CENTER);

        // Encabezado
        JLabel encabezado = new JLabel("📁 Gestión de Recursos Multimedia", SwingConstants.CENTER);
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 22));
        encabezado.setForeground(rojoUTP);
        encabezado.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(encabezado, BorderLayout.SOUTH);

        getContentPane().setBackground(grisClaro);
        setVisible(true);
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return boton;
    }

    private void agregarRecurso() {
        String[] opciones = {
                "Libro Digital", "Video", "Audio", "Presentación", "Mapa", "Infografía", "Revista", "Tesis"
        };
        String tipo = (String) JOptionPane.showInputDialog(
                this, "Seleccione tipo:", "Nuevo Recurso",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]
        );

        if (tipo == null) return;

        String id = JOptionPane.showInputDialog("ID:");
        String titulo = JOptionPane.showInputDialog("Título:");
        String autor = JOptionPane.showInputDialog("Autor:");

        Multimedia recurso = null;

        try {
            switch (tipo) {
                case "Libro Digital":
                    String isbn = JOptionPane.showInputDialog("ISBN:");
                    int tamanoMB = Integer.parseInt(JOptionPane.showInputDialog("Tamaño (MB):"));
                    recurso = new LibroDigital(id, titulo, autor, isbn, tamanoMB, true);
                    break;
                case "Video":
                    int durVideo = Integer.parseInt(JOptionPane.showInputDialog("Duración (min):"));
                    recurso = new Video(id, titulo, autor, durVideo, true);
                    break;
                case "Audio":
                    int durAudio = Integer.parseInt(JOptionPane.showInputDialog("Duración (min):"));
                    recurso = new Audio(id, titulo, autor, durAudio, true);
                    break;
                case "Presentación":
                    int slides = Integer.parseInt(JOptionPane.showInputDialog("N° de diapositivas:"));
                    recurso = new Presentacion(id, titulo, autor, slides, true);
                    break;
                case "Mapa":
                    String region = JOptionPane.showInputDialog("Región:");
                    recurso = new Mapa(id, titulo, autor, region, true);
                    break;
                case "Infografía":
                    String tema = JOptionPane.showInputDialog("Tema:");
                    recurso = new Infografia(id, titulo, autor, tema, true);
                    break;
                case "Revista":
                    int nroRevista = Integer.parseInt(JOptionPane.showInputDialog("Número de revista:"));
                    recurso = new Revista(id, titulo, autor, nroRevista, true);
                    break;
                case "Tesis":
                    String universidad = JOptionPane.showInputDialog("Universidad:");
                    recurso = new Tesis(id, titulo, autor, universidad, true);
                    break;
            }

            if (recurso != null) {
                multimediaService.agregarRecurso(recurso);
                JOptionPane.showMessageDialog(this, "✅ Recurso agregado exitosamente.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "❌ Entrada numérica inválida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPorId() {
        String id = JOptionPane.showInputDialog("Ingrese ID:");
        Multimedia recurso = multimediaService.obtenerPorId(id);
        if (recurso != null) {
            areaResultado.setText("📌 Resultado:\n\n" + recurso);
        } else {
            areaResultado.setText("⚠️ Recurso no encontrado.");
        }
    }

    private void listarRecursos() {
        List<Multimedia> recursos = multimediaService.getRecursos();
        StringBuilder sb = new StringBuilder("📚 Todos los recursos:\n\n");
        for (Multimedia r : recursos) {
            sb.append(r).append("\n");
        }
        areaResultado.setText(sb.toString());
    }
}
