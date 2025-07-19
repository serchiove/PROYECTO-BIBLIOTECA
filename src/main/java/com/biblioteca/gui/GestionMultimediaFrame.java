package com.biblioteca.gui;

import com.biblioteca.multimedia.*;
import com.biblioteca.servicios.MultimediaService;
import com.biblioteca.usuarios.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class GestionMultimediaFrame extends JFrame {

    private final MultimediaService multimediaService;
    private final JTextArea areaResultado;
    private final Usuario usuario;

    public GestionMultimediaFrame(Usuario usuario, MultimediaService multimediaService) {
        super("Gestión de Recursos Multimedia");
        this.usuario = usuario;
        this.multimediaService = multimediaService;

        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout(15, 15));

        Color azulOscuro = new Color(30, 30, 60);
        Color grisClaro = new Color(245, 245, 245);
        Color grisTexto = new Color(40, 40, 40);

        // Encabezado
        JLabel encabezado = new JLabel("📁 Gestión de Recursos Multimedia", SwingConstants.CENTER);
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 24));
        encabezado.setForeground(grisTexto);
        encabezado.setBorder(new EmptyBorder(20, 10, 10, 10));
        add(encabezado, BorderLayout.NORTH);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(grisClaro);
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnAgregar = crearBoton("➕ Agregar Recurso", azulOscuro);
        JButton btnBuscar = crearBoton("🔍 Buscar por ID", azulOscuro);
        JButton btnListar = crearBoton("📄 Listar Todo", azulOscuro);
        JButton btnEliminar = crearBoton("🗑️ Eliminar Recurso", azulOscuro);

        btnAgregar.addActionListener(e -> agregarRecurso());
        btnBuscar.addActionListener(e -> buscarPorId());
        btnListar.addActionListener(e -> listarRecursos());
        btnEliminar.addActionListener(e -> eliminarRecurso());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnListar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.CENTER);

        // Área de resultados
        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        areaResultado.setBackground(Color.WHITE);
        areaResultado.setForeground(grisTexto);
        areaResultado.setBorder(new EmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(areaResultado);
        scrollPane.setBorder(BorderFactory.createTitledBorder("📝 Resultados"));
        scrollPane.setPreferredSize(new Dimension(600, 200));
        add(scrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton crearBoton(String texto, Color colorTexto) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(240, 240, 240));
        boton.setForeground(colorTexto);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(10, 20, 10, 20)
        ));
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
        if (id == null || id.trim().isEmpty()) return;
        String titulo = JOptionPane.showInputDialog("Título:");
        if (titulo == null || titulo.trim().isEmpty()) return;
        String autor = JOptionPane.showInputDialog("Autor:");
        if (autor == null || autor.trim().isEmpty()) return;

        Multimedia recurso = null;

        try {
            switch (tipo) {
                case "Libro Digital" -> {
                    String isbn = JOptionPane.showInputDialog("ISBN:");
                    int tamanoMB = Integer.parseInt(JOptionPane.showInputDialog("Tamaño (MB):"));
                    recurso = new LibroDigital(id, titulo, autor, isbn, tamanoMB, true);
                }
                case "Video" -> {
                    int durVideoMin = Integer.parseInt(JOptionPane.showInputDialog("Duración (minutos):"));
                    recurso = new Video(id, titulo, autor, durVideoMin * 60, true);
                }
                case "Audio" -> {
                    int durAudioMin = Integer.parseInt(JOptionPane.showInputDialog("Duración (minutos):"));
                    recurso = new Audio(id, titulo, autor, durAudioMin * 60, true);
                }
                case "Presentación" -> {
                    int slides = Integer.parseInt(JOptionPane.showInputDialog("N° de diapositivas:"));
                    recurso = new Presentacion(id, titulo, autor, slides, true);
                }
                case "Mapa" -> {
                    String region = JOptionPane.showInputDialog("Región:");
                    recurso = new Mapa(id, titulo, autor, region, true);
                }
                case "Infografía" -> {
                    String tema = JOptionPane.showInputDialog("Tema:");
                    recurso = new Infografia(id, titulo, autor, tema, true);
                }
                case "Revista" -> {
                    int nroRevista = Integer.parseInt(JOptionPane.showInputDialog("Número de revista:"));
                    recurso = new Revista(id, titulo, autor, nroRevista, true);
                }
                case "Tesis" -> {
                    String universidad = JOptionPane.showInputDialog("Universidad:");
                    recurso = new Tesis(id, titulo, autor, universidad, true);
                }
            }

            if (recurso != null) {
                multimediaService.agregarRecurso(recurso);
                JOptionPane.showMessageDialog(this, "✅ Recurso agregado exitosamente.");
                listarRecursos();
            }

        } catch (NumberFormatException e) {
            mostrarError("❌ Entrada numérica inválida.");
        } catch (Exception e) {
            mostrarError("Error al agregar recurso: " + e.getMessage());
        }
    }

    private void buscarPorId() {
        String id = JOptionPane.showInputDialog("Ingrese ID:");
        if (id == null || id.trim().isEmpty()) return;
        try {
            Multimedia recurso = multimediaService.buscarPorId(id);
            if (recurso != null) {
                areaResultado.setText("📌 Resultado:\n\n" + recurso);
            } else {
                areaResultado.setText("⚠️ Recurso no encontrado.");
            }
        } catch (Exception e) {
            mostrarError("Error al buscar recurso: " + e.getMessage());
        }
    }

    private void listarRecursos() {
        try {
            List<Multimedia> recursos = multimediaService.listarRecursosDisponibles();
            StringBuilder sb = new StringBuilder("📚 Todos los recursos:\n\n");
            for (Multimedia r : recursos) {
                sb.append(r).append("\n\n");
            }
            areaResultado.setText(sb.toString());
        } catch (Exception e) {
            mostrarError("Error al listar recursos: " + e.getMessage());
        }
    }

    private void eliminarRecurso() {
        String id = JOptionPane.showInputDialog(this, "Ingrese el ID del recurso a eliminar:");
        if (id == null || id.trim().isEmpty()) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el recurso con ID: " + id + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = multimediaService.eliminarRecursoPorId(id);
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "✅ Recurso eliminado correctamente.");
                    listarRecursos();
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ No se encontró recurso con ese ID.");
                }
            } catch (Exception e) {
                mostrarError("Error al eliminar recurso: " + e.getMessage());
            }
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
