package com.biblioteca.gui;

import com.biblioteca.multimedia.Multimedia;
import com.biblioteca.recurso.RecursoTecnologico;
import com.biblioteca.servicios.MultimediaService;
import com.biblioteca.servicios.RecursoTecnologicoService;
import com.biblioteca.servicios.PrestamoService;
import com.biblioteca.usuarios.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VerRecursosFrame extends JFrame {

    private final JList<Object> listaRecursos; // Cambiado a Object para mezclar tipos
    private final DefaultListModel<Object> modeloLista;
    private final JTextArea areaMensajes;
    private final PrestamoService prestamoService;
    private final MultimediaService multimediaService;
    private final RecursoTecnologicoService recursoTecnologicoService; // Nuevo servicio
    private final Usuario usuario;

    public VerRecursosFrame(Usuario usuario,
                            PrestamoService prestamoService,
                            MultimediaService multimediaService,
                            RecursoTecnologicoService recursoTecnologicoService) {
        this.usuario = usuario;
        this.prestamoService = prestamoService;
        this.multimediaService = multimediaService;
        this.recursoTecnologicoService = recursoTecnologicoService;

        setTitle("📂 Recursos Disponibles");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        modeloLista = new DefaultListModel<>();
        listaRecursos = new JList<>(modeloLista);
        listaRecursos.setFont(new Font("SansSerif", Font.PLAIN, 14));

        areaMensajes = new JTextArea(5, 50);
        areaMensajes.setEditable(false);
        areaMensajes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaMensajes.setBackground(new Color(245, 245, 245));
        areaMensajes.setLineWrap(true);
        areaMensajes.setWrapStyleWord(true);

        initUI();
        cargarRecursosDisponibles();

        listaRecursos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Object seleccionado = listaRecursos.getSelectedValue();
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

        if (multimediaService == null || recursoTecnologicoService == null) {
            areaMensajes.setText("Error: Servicios no disponibles.");
            JOptionPane.showMessageDialog(this, "Servicios de recursos no están disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Multimedia> multimediaList = multimediaService.listarRecursos();
        List<RecursoTecnologico> recursosTecList = recursoTecnologicoService.listarTodos();

        if ((multimediaList == null || multimediaList.isEmpty()) && (recursosTecList == null || recursosTecList.isEmpty())) {
            areaMensajes.setText("No hay recursos disponibles en este momento.");
            JOptionPane.showMessageDialog(this, "No hay recursos disponibles actualmente.");
            return;
        }

        if (multimediaList != null) {
            for (Multimedia m : multimediaList) {
                modeloLista.addElement(m);
            }
        }

        if (recursosTecList != null) {
            for (RecursoTecnologico r : recursosTecList) {
                modeloLista.addElement(r);
            }
        }

        areaMensajes.setText("Se encontraron " + modeloLista.size() + " recurso(s) disponible(s).");
    }

    private String detalleRecurso(Object recurso) {
        if (recurso == null) return "";

        if (recurso instanceof Multimedia m) {
            return "📖 Recurso Multimedia\n" +
                    "Título: " + m.getTitulo() + "\n" +
                    "Autor: " + (m.getAutor() != null ? m.getAutor() : "Desconocido") + "\n" +
                    "Tipo: " + m.getClass().getSimpleName() + "\n" +
                    "Disponible: " + (m.isDisponible() ? "Sí" : "No") + "\n" +
                    "Descripción: " + (m.getDescripcion() != null ? m.getDescripcion() : "No disponible");
        } else if (recurso instanceof RecursoTecnologico r) {
            return "💻 Recurso Tecnológico\n" +
                    "ID: " + r.getId() + "\n" +
                    "Tipo: " + r.getTipo() + "\n" +
                    "Estado: " + r.getEstado();
        } else {
            return "Tipo de recurso desconocido.";
        }
    }
}
