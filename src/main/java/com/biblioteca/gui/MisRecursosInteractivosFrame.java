package com.biblioteca.gui;

import com.biblioteca.interfaces.Descargable;
import com.biblioteca.interfaces.Reproducible;
import com.biblioteca.interfaces.Visualizable;
import com.biblioteca.multimedia.Multimedia;
import com.biblioteca.data.Prestamo;
import com.biblioteca.servicios.PrestamoService;
import com.biblioteca.usuarios.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MisRecursosInteractivosFrame extends JFrame {
    private Usuario usuario;
    private PrestamoService prestamoService;
    private JPanel panelRecursos;
    private JTextArea areaMensajes;

    public MisRecursosInteractivosFrame(Usuario usuario, PrestamoService prestamoService) {
        this.usuario = usuario;
        this.prestamoService = prestamoService;

        setTitle("🎧 Mis Recursos Interactivos");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        setVisible(true);
    }

    private void initUI() {
        Color rojoUTP = new Color(183, 28, 28);
        Color fondoClaro = new Color(250, 250, 250);

        panelRecursos = new JPanel();
        panelRecursos.setLayout(new BoxLayout(panelRecursos, BoxLayout.Y_AXIS));
        panelRecursos.setBackground(fondoClaro);
        JScrollPane scrollLista = new JScrollPane(panelRecursos);

        areaMensajes = new JTextArea(4, 60);
        areaMensajes.setEditable(false);
        areaMensajes.setBackground(new Color(245, 245, 245));
        areaMensajes.setBorder(BorderFactory.createTitledBorder("💬 Mensajes del sistema"));
        JScrollPane scrollMensajes = new JScrollPane(areaMensajes);

        JLabel titulo = new JLabel("Recursos Interactivos Reservados", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(rojoUTP);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(fondoClaro);
        getContentPane().add(titulo, BorderLayout.NORTH);
        getContentPane().add(scrollMensajes, BorderLayout.SOUTH);
        getContentPane().add(scrollLista, BorderLayout.CENTER);

        cargarRecursosReservadosInteractivos();
    }

    private void cargarRecursosReservadosInteractivos() {
        panelRecursos.removeAll();
        areaMensajes.setText("");

        List<Prestamo> prestamos = prestamoService.listarPrestamosPorUsuario(usuario.getNombreUsuario());
        boolean hayRecursos = false;

        for (Prestamo p : prestamos) {
            if (!p.isDevuelto()) {
                Multimedia recurso = prestamoService.getMultimediaService().obtenerPorId(p.getIdRecurso());
                if (recurso instanceof Descargable || recurso instanceof Visualizable || recurso instanceof Reproducible) {
                    hayRecursos = true;
                    JPanel panelItem = crearPanelRecurso(recurso);
                    panelRecursos.add(panelItem);
                    panelRecursos.add(Box.createVerticalStrut(10));
                }
            }
        }

        if (!hayRecursos) {
            areaMensajes.append("❌ No tienes recursos interactivos reservados.\n");
        } else {
            areaMensajes.append("✅ Recursos interactivos cargados correctamente.\n");
        }

        panelRecursos.revalidate();
        panelRecursos.repaint();
    }

    private JPanel crearPanelRecurso(Multimedia recurso) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("🎓 " + recurso.getTitulo()));
        panel.setBackground(Color.WHITE);

        if (recurso instanceof Descargable) {
            JButton btnDescargar = new JButton("⬇️ Descargar");
            btnDescargar.setBackground(new Color(200, 230, 201));
            btnDescargar.addActionListener(e -> {
                ((Descargable) recurso).descargar();
                areaMensajes.append("⬇️ Recurso descargado: " + recurso.getTitulo() + "\n");
            });
            panel.add(btnDescargar);
        }

        if (recurso instanceof Visualizable) {
            JButton btnVisualizar = new JButton("👁️ Visualizar");
            btnVisualizar.setBackground(new Color(197, 202, 233));
            btnVisualizar.addActionListener(e -> {
                ((Visualizable) recurso).visualizar();
                areaMensajes.append("👁️ Recurso visualizado: " + recurso.getTitulo() + "\n");
            });
            panel.add(btnVisualizar);
        }

        if (recurso instanceof Reproducible) {
            JButton btnReproducir = new JButton("🔊 Reproducir");
            btnReproducir.setBackground(new Color(255, 224, 178));
            btnReproducir.addActionListener(e -> {
                ((Reproducible) recurso).reproducir();
                areaMensajes.append("🔊 Recurso reproducido: " + recurso.getTitulo() + "\n");
            });
            panel.add(btnReproducir);
        }

        return panel;
    }
}
