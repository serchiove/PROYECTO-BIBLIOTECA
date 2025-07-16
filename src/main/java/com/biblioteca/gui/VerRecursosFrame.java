package com.biblioteca.gui;

import com.biblioteca.multimedia.Multimedia;
import com.biblioteca.servicios.MultimediaService;
import com.biblioteca.servicios.PrestamoService;
import com.biblioteca.usuarios.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VerRecursosFrame extends JFrame {

    private final JList<Multimedia> listaRecursos;
    private final DefaultListModel<Multimedia> modeloLista;
    private final JTextArea areaMensajes;
    private final PrestamoService prestamoService;
    private final MultimediaService multimediaService;
    private final Usuario usuario;

    public VerRecursosFrame(Usuario usuario, PrestamoService prestamoService) {
        this.usuario = usuario;
        this.prestamoService = prestamoService;
        this.multimediaService = prestamoService != null ? prestamoService.getMultimediaService() : null;

        setTitle("📂 Recursos Disponibles");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        modeloLista = new DefaultListModel<>();
        listaRecursos = new JList<>(modeloLista);
        listaRecursos.setFont(new Font("SansSerif", Font.PLAIN, 14));

        areaMensajes = new JTextArea(4, 50);
        areaMensajes.setEditable(false);
        areaMensajes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaMensajes.setBackground(new Color(245, 245, 245));
        areaMensajes.setLineWrap(true);
        areaMensajes.setWrapStyleWord(true);

        initUI();
        cargarRecursosDisponibles();

        // Mostrar info del recurso seleccionado
        listaRecursos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Multimedia seleccionado = listaRecursos.getSelectedValue();
                if (seleccionado != null) {
                    areaMensajes.setText(detalleRecurso(seleccionado));
                } else {
                    areaMensajes.setText("");
                }
            }
        });

        setVisible(true);
    }

    private void initUI() {
        JPanel contenido = new JPanel(new BorderLayout(10, 10));
        setContentPane(contenido);

        JPanel panelLista = new JPanel(new BorderLayout());
        panelLista.setBorder(BorderFactory.createTitledBorder("📚 Lista de recursos disponibles"));
        JScrollPane scrollRecursos = new JScrollPane(listaRecursos);
        panelLista.add(scrollRecursos, BorderLayout.CENTER);

        JPanel panelMensajes = new JPanel(new BorderLayout());
        panelMensajes.setBorder(BorderFactory.createTitledBorder("📋 Información"));
        JScrollPane scrollMensajes = new JScrollPane(areaMensajes);
        panelMensajes.add(scrollMensajes, BorderLayout.CENTER);

        contenido.add(panelLista, BorderLayout.CENTER);
        contenido.add(panelMensajes, BorderLayout.SOUTH);
    }

    private void cargarRecursosDisponibles() {
        modeloLista.clear();

        if (multimediaService == null) {
            areaMensajes.setText("Error: Servicio de multimedia no disponible.");
            JOptionPane.showMessageDialog(this, "Servicio de multimedia no está disponible.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Multimedia> disponibles = multimediaService.listarRecursos();

        if (disponibles == null || disponibles.isEmpty()) {
            areaMensajes.setText("No hay recursos disponibles en este momento.");
            JOptionPane.showMessageDialog(this, "No hay recursos disponibles actualmente.");
        } else {
            for (Multimedia m : disponibles) {
                modeloLista.addElement(m);
            }
            areaMensajes.setText("Se encontraron " + modeloLista.size() + " recurso(s) disponible(s).");
        }
    }

    private String detalleRecurso(Multimedia m) {
        if (m == null) return "";

        return "Título: " + m.getTitulo() + "\n"
                + "Autor: " + (m.getAutor() != null ? m.getAutor() : "Desconocido") + "\n"
                + "Tipo: " + m.getClass().getSimpleName() + "\n"
                + "Disponible: " + (m.isDisponible() ? "Sí" : "No") + "\n"
                + "Descripción: " + (m.getDescripcion() != null ? m.getDescripcion() : "No disponible");
    }
}
