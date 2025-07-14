package com.biblioteca.gui;

import com.biblioteca.data.Prestamo;
import com.biblioteca.data.PrestamoDAO;
import com.biblioteca.multimedia.Multimedia;
import com.biblioteca.servicios.MultimediaService;
import com.biblioteca.servicios.PrestamoService;
import com.biblioteca.servicios.UsuarioService;
import com.biblioteca.usuarios.Estudiante;
import com.biblioteca.usuarios.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GestionPrestamosFrame extends JFrame {
    private final PrestamoService prestamoService;
    private final UsuarioService usuarioService;
    private final MultimediaService multimediaService;

    private JComboBox<Usuario> comboUsuarios;
    private JComboBox<Multimedia> comboRecursosDisponibles;
    private JComboBox<Multimedia> comboRecursosPrestados;

    public GestionPrestamosFrame() {
        this.usuarioService = new UsuarioService();
        this.multimediaService = new MultimediaService();
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        this.prestamoService = new PrestamoService(prestamoDAO, multimediaService, usuarioService);
        initUI(true, null);
    }

    public GestionPrestamosFrame(Usuario estudiante, PrestamoService prestamoService) {
        this.usuarioService = null;
        this.multimediaService = prestamoService.getMultimediaService();
        this.prestamoService = prestamoService;
        initUI(false, estudiante);
    }

    private void initUI(boolean mostrarUsuarios, Usuario estudiante) {
        Color rojoUTP = new Color(183, 28, 28);
        Color grisClaro = new Color(245, 245, 245);
        getContentPane().setBackground(grisClaro);

        setTitle("Gestión de Préstamos");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel encabezado = new JLabel("📦 Gestión de Préstamos", SwingConstants.CENTER);
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 22));
        encabezado.setForeground(rojoUTP);
        encabezado.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(encabezado, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelFormulario.setBackground(grisClaro);

        comboUsuarios = new JComboBox<>();
        comboRecursosDisponibles = new JComboBox<>();
        comboRecursosPrestados = new JComboBox<>();

        if (mostrarUsuarios) {
            cargarUsuarios();
            comboUsuarios.addActionListener(e -> {
                Usuario seleccionado = (Usuario) comboUsuarios.getSelectedItem();
                if (seleccionado != null) {
                    cargarRecursosPrestados(seleccionado);
                }
            });
        } else {
            comboUsuarios.addItem(estudiante);
            comboUsuarios.setEnabled(false);
            cargarRecursosPrestados(estudiante);
        }

        cargarRecursosDisponibles();

        panelFormulario.add(new JLabel("Estudiante:"));
        panelFormulario.add(comboUsuarios);
        panelFormulario.add(new JLabel("Recurso disponible:"));
        panelFormulario.add(comboRecursosDisponibles);
        panelFormulario.add(new JLabel("Recurso prestado:"));
        panelFormulario.add(comboRecursosPrestados);

        JButton btnPrestar = new JButton("Registrar préstamo");
        btnPrestar.setBackground(rojoUTP);
        btnPrestar.setForeground(Color.WHITE);
        btnPrestar.setFocusPainted(false);
        btnPrestar.addActionListener(e -> registrarPrestamo());

        JButton btnDevolver = new JButton("Registrar devolución");
        btnDevolver.setBackground(rojoUTP);
        btnDevolver.setForeground(Color.WHITE);
        btnDevolver.setFocusPainted(false);
        btnDevolver.addActionListener(e -> registrarDevolucion());

        panelFormulario.add(btnPrestar);
        panelFormulario.add(btnDevolver);

        add(panelFormulario, BorderLayout.CENTER);
        setVisible(true);
    }

    private void cargarUsuarios() {
        comboUsuarios.removeAllItems();
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        for (Usuario u : usuarios) {
            if (u instanceof Estudiante) {
                comboUsuarios.addItem(u);
            }
        }
        if (comboUsuarios.getItemCount() > 0) {
            comboUsuarios.setSelectedIndex(0);
            cargarRecursosPrestados((Usuario) comboUsuarios.getSelectedItem());
        }
    }

    private void cargarRecursosDisponibles() {
        comboRecursosDisponibles.removeAllItems();
        List<Multimedia> recursos = multimediaService.listarRecursos();
        for (Multimedia recurso : recursos) {
            if (recurso.isDisponible()) {
                comboRecursosDisponibles.addItem(recurso);
            }
        }
    }

    private void cargarRecursosPrestados(Usuario usuario) {
        comboRecursosPrestados.removeAllItems();
        List<Prestamo> prestamos = prestamoService.prestamosPorUsuario(usuario.getId());
        for (Prestamo p : prestamos) {
            if (!p.isDevuelto()) {
                Multimedia recurso = multimediaService.obtenerPorId(p.getIdRecurso());
                if (recurso != null) {
                    comboRecursosPrestados.addItem(recurso);
                }
            }
        }
    }

    private void registrarPrestamo() {
        Usuario usuario = (Usuario) comboUsuarios.getSelectedItem();
        Multimedia recurso = (Multimedia) comboRecursosDisponibles.getSelectedItem();

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante.");
            return;
        }
        if (recurso == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un recurso disponible.");
            return;
        }

        boolean ok = prestamoService.registrarPrestamo(usuario.getId(), recurso.getId());
        if (ok) {
            JOptionPane.showMessageDialog(this, "Préstamo registrado exitosamente.");
            cargarRecursosDisponibles();
            cargarRecursosPrestados(usuario);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar el préstamo.");
        }
    }

    private void registrarDevolucion() {
        Usuario usuario = (Usuario) comboUsuarios.getSelectedItem();
        Multimedia recurso = (Multimedia) comboRecursosPrestados.getSelectedItem();

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante.");
            return;
        }
        if (recurso == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un recurso prestado.");
            return;
        }

        boolean ok = prestamoService.registrarDevolucion(usuario.getId(), recurso.getId());
        if (ok) {
            JOptionPane.showMessageDialog(this, "Devolución registrada exitosamente.");
            cargarRecursosDisponibles();
            cargarRecursosPrestados(usuario);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar la devolución.");
        }
    }
}
