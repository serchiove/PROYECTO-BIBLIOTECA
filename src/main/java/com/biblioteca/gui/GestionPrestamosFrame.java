package com.biblioteca.gui;

import com.biblioteca.data.Prestamo;
import com.biblioteca.data.PrestamoDAO;
import com.biblioteca.multimedia.Multimedia;
import com.biblioteca.servicios.MultimediaService;
import com.biblioteca.servicios.PrestamoService;
import com.biblioteca.servicios.UsuarioService;
import com.biblioteca.usuarios.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class GestionPrestamosFrame extends JFrame {

    private final PrestamoService prestamoService;
    private final UsuarioService usuarioService;
    private final MultimediaService multimediaService;
    private final Usuario usuarioActual;

    private JComboBox<Usuario> comboUsuarios;
    private JComboBox<Multimedia> comboRecursosDisponibles;
    private JComboBox<Multimedia> comboRecursosPrestados;

    public GestionPrestamosFrame(Connection connection, Usuario usuarioActual) {
        this.usuarioService = new UsuarioService(connection);
        this.multimediaService = new MultimediaService(connection);
        PrestamoDAO prestamoDAO = new PrestamoDAO(connection);
        this.prestamoService = new PrestamoService(prestamoDAO, multimediaService, usuarioService);
        this.usuarioActual = usuarioActual;

        // Verifica si el usuario es estudiante para definir el acceso
        boolean esEstudiante = usuarioActual.getRol().equalsIgnoreCase("Estudiante");
        initUI(!esEstudiante, esEstudiante ? usuarioActual : null);
    }

    private void initUI(boolean mostrarUsuarios, Usuario estudiante) {
        Color grisClaro = new Color(245, 245, 245);
        Color grisOscuro = new Color(60, 63, 65);
        Color textoOscuro = new Color(33, 33, 33);

        getContentPane().setBackground(grisClaro);

        setTitle("Gestión de Préstamos");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel encabezado = new JLabel("📦 Gestión de Préstamos", SwingConstants.CENTER);
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 22));
        encabezado.setForeground(textoOscuro);
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
            comboUsuarios.setEnabled(true);
            comboUsuarios.addActionListener(e -> {
                Usuario seleccionado = (Usuario) comboUsuarios.getSelectedItem();
                cargarRecursosPrestados(seleccionado);
            });
        } else {
            comboUsuarios.addItem(estudiante);
            comboUsuarios.setEnabled(false); // estudiante no puede cambiar
            cargarRecursosPrestados(estudiante);
        }

        cargarRecursosDisponibles();

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setForeground(textoOscuro);
        panelFormulario.add(lblUsuario);
        panelFormulario.add(comboUsuarios);

        JLabel lblDisponible = new JLabel("Recurso disponible:");
        lblDisponible.setForeground(textoOscuro);
        panelFormulario.add(lblDisponible);
        panelFormulario.add(comboRecursosDisponibles);

        JLabel lblPrestado = new JLabel("Recurso prestado:");
        lblPrestado.setForeground(textoOscuro);
        panelFormulario.add(lblPrestado);
        panelFormulario.add(comboRecursosPrestados);

        JButton btnPrestar = new JButton("Registrar préstamo");
        btnPrestar.setBackground(grisOscuro);
        btnPrestar.setForeground(Color.WHITE);
        btnPrestar.setFocusPainted(false);
        btnPrestar.addActionListener(e -> registrarPrestamo());

        JButton btnDevolver = new JButton("Registrar devolución");
        btnDevolver.setBackground(grisOscuro);
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

        if (usuarioService == null) {
            System.out.println("⚠️ usuarioService es null, no se puede cargar usuarios.");
            return;
        }

        try {
            List<Usuario> usuarios = usuarioService.listarUsuarios();
            System.out.println("Usuarios obtenidos: " + usuarios.size());

            if (usuarios.isEmpty()) {
                System.out.println("⚠️ No hay usuarios en la base de datos.");
                return;
            }

            for (Usuario u : usuarios) {
                System.out.println("Agregando usuario: " + u.getNombre() + " - Rol: " + u.getRol());
                comboUsuarios.addItem(u);
            }

            if (comboUsuarios.getItemCount() > 0) {
                comboUsuarios.setSelectedIndex(0);
                Usuario primero = (Usuario) comboUsuarios.getSelectedItem();
                cargarRecursosPrestados(primero);
            }

        } catch (Exception e) {
            System.err.println("❌ Error cargando usuarios: " + e.getMessage());
            e.printStackTrace();
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
        if (usuario == null) return;

        List<Prestamo> prestamos = prestamoService.prestamosPorUsuario(usuario.getId());
        for (Prestamo p : prestamos) {
            if (!p.isDevuelto()) {
                Multimedia recurso = multimediaService.buscarPorId(p.getIdRecurso());
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
            JOptionPane.showMessageDialog(this, "Seleccione un usuario.");
            return;
        }
        if (recurso == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un recurso disponible.");
            return;
        }

        boolean ok = prestamoService.registrarPrestamo(usuario.getId(), recurso.getId());
        if (ok) {
            JOptionPane.showMessageDialog(this, "✅ Préstamo registrado exitosamente.");
            cargarRecursosDisponibles();
            cargarRecursosPrestados(usuario);
        } else {
            JOptionPane.showMessageDialog(this, "❌ No se pudo registrar el préstamo.");
        }
    }

    private void registrarDevolucion() {
        Usuario usuario = (Usuario) comboUsuarios.getSelectedItem();
        Multimedia recurso = (Multimedia) comboRecursosPrestados.getSelectedItem();

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario.");
            return;
        }
        if (recurso == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un recurso prestado.");
            return;
        }

        boolean ok = prestamoService.registrarDevolucion(usuario.getId(), recurso.getId());
        if (ok) {
            JOptionPane.showMessageDialog(this, "✅ Devolución registrada exitosamente.");
            cargarRecursosDisponibles();
            cargarRecursosPrestados(usuario);
        } else {
            JOptionPane.showMessageDialog(this, "❌ No se pudo registrar la devolución.");
        }
    }
}
