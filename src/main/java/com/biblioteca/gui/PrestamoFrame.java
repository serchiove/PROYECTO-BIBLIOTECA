package com.biblioteca.gui;

import com.biblioteca.multimedia.Multimedia;
import com.biblioteca.servicios.MultimediaService;
import com.biblioteca.servicios.PrestamoService;
import com.biblioteca.servicios.UsuarioService;
import com.biblioteca.usuarios.Estudiante;
import com.biblioteca.usuarios.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PrestamoFrame extends JFrame {
    private final PrestamoService prestamoService;
    private final UsuarioService usuarioService;
    private final MultimediaService multimediaService;

    private JComboBox<Usuario> comboUsuarios;
    private JComboBox<Multimedia> comboRecursos;

    public PrestamoFrame(Usuario usuarioActual, PrestamoService prestamoService,
                         MultimediaService multimediaService, UsuarioService usuarioService) {
        super("Registrar Préstamo o Devolución");
        this.usuarioService = usuarioService;
        this.multimediaService = multimediaService;
        this.prestamoService = prestamoService;

        initUI(usuarioActual);
    }

    private void initUI(Usuario usuarioActual) {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        comboUsuarios = new JComboBox<>();
        comboRecursos = new JComboBox<>();

        cargarDatos(usuarioActual);

        panelFormulario.add(new JLabel("Estudiante:"));
        panelFormulario.add(comboUsuarios);
        panelFormulario.add(new JLabel("Recurso disponible:"));
        panelFormulario.add(comboRecursos);

        JButton btnPrestar = new JButton("Registrar Préstamo");
        btnPrestar.addActionListener(e -> registrarPrestamo());

        JButton btnDevolver = new JButton("Registrar Devolución");
        btnDevolver.addActionListener(e -> registrarDevolucion());

        panelFormulario.add(btnPrestar);
        panelFormulario.add(btnDevolver);

        add(panelFormulario, BorderLayout.CENTER);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void cargarDatos(Usuario usuarioActual) {
        comboUsuarios.removeAllItems();
        comboRecursos.removeAllItems();

        // Cargar estudiantes
        if (usuarioActual instanceof Estudiante) {
            comboUsuarios.addItem(usuarioActual); // Solo el mismo estudiante
            comboUsuarios.setEnabled(false);
        } else {
            List<Usuario> usuarios = usuarioService.listarUsuarios();
            for (Usuario u : usuarios) {
                if (u instanceof Estudiante) {
                    comboUsuarios.addItem(u);
                }
            }
            comboUsuarios.setEnabled(true);
        }

        // Cargar recursos disponibles (para préstamo)
        // Mostrar solo recursos disponibles para préstamo y recursos que el usuario tiene prestados para devolución
        // Pero aquí solo cargamos los disponibles para préstamo (devolver se gestiona aparte)
        List<Multimedia> recursos = multimediaService.listarRecursos();
        for (Multimedia recurso : recursos) {
            if (recurso.isDisponible()) {
                comboRecursos.addItem(recurso);
            }
        }
    }

    private void registrarPrestamo() {
        Usuario usuario = (Usuario) comboUsuarios.getSelectedItem();
        Multimedia recurso = (Multimedia) comboRecursos.getSelectedItem();

        if (usuario == null || recurso == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un estudiante y un recurso.");
            return;
        }

        if (!recurso.isDisponible()) {
            JOptionPane.showMessageDialog(this, "El recurso seleccionado no está disponible para préstamo.");
            return;
        }

        boolean ok = prestamoService.registrarPrestamo(usuario.getId(), recurso.getId());
        if (ok) {
            JOptionPane.showMessageDialog(this, "Préstamo registrado exitosamente.");
            cargarDatos(usuario); // actualizar recursos disponibles
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar el préstamo. Verifica disponibilidad.");
        }
    }

    private void registrarDevolucion() {
        Usuario usuario = (Usuario) comboUsuarios.getSelectedItem();

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un estudiante.");
            return;
        }

        // Para devolución, mostramos solo los recursos que el usuario tiene actualmente en préstamo (no devueltos)
        List<Multimedia> recursosPrestados = prestamoService.listarRecursosPrestadosPorUsuario(usuario.getId());
        if (recursosPrestados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este usuario no tiene recursos en préstamo para devolver.");
            return;
        }

        // Pedir que seleccione qué recurso devolver
        Multimedia recursoADevolver = (Multimedia) JOptionPane.showInputDialog(
                this,
                "Selecciona el recurso a devolver:",
                "Devolución",
                JOptionPane.PLAIN_MESSAGE,
                null,
                recursosPrestados.toArray(),
                null
        );

        if (recursoADevolver == null) {
            // Canceló selección
            return;
        }

        boolean ok = prestamoService.registrarDevolucion(usuario.getId(), recursoADevolver.getId());
        if (ok) {
            JOptionPane.showMessageDialog(this, "Devolución registrada exitosamente.");
            cargarDatos(usuario);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar la devolución. Verifica que el recurso esté prestado.");
        }
    }
}
