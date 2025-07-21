package com.biblioteca.gui;

import com.biblioteca.data.PrestamoDAO;
import com.biblioteca.data.PrestamoRecursoTecnologicoDAO;
import com.biblioteca.data.RecursoTecnologicoDAO;
import com.biblioteca.data.RecursoPrestado;
import com.biblioteca.multimedia.Multimedia;
import com.biblioteca.recurso.RecursoTecnologico;
import com.biblioteca.servicios.MultimediaService;
import com.biblioteca.servicios.PrestamoService;
import com.biblioteca.servicios.RecursoTecnologicoService;
import com.biblioteca.servicios.UsuarioService;
import com.biblioteca.usuarios.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GestionPrestamosFrame extends JFrame {

    private final PrestamoService prestamoService;
    private final UsuarioService usuarioService;
    private final MultimediaService multimediaService;
    private final RecursoTecnologicoService recursoTecnologicoService;
    private final Usuario usuarioActual;

    private JComboBox<Usuario> comboUsuarios;
    private JComboBox<Object> comboRecursosDisponibles; // Cambiado a Object para multimedia o recurso tecnológico
    private JComboBox<RecursoPrestado> comboRecursosPrestados;

    public GestionPrestamosFrame(Connection connection, Usuario usuarioActual) {
        this.usuarioService = new UsuarioService(connection);
        this.multimediaService = new MultimediaService(connection);

        PrestamoDAO prestamoDAO = new PrestamoDAO(connection);
        PrestamoRecursoTecnologicoDAO prestamoRecTecDAO = new PrestamoRecursoTecnologicoDAO(connection);
        RecursoTecnologicoDAO recursoTecnologicoDAO = new RecursoTecnologicoDAO(connection);
        this.recursoTecnologicoService = new RecursoTecnologicoService(recursoTecnologicoDAO);

        this.prestamoService = new PrestamoService(prestamoDAO, prestamoRecTecDAO, multimediaService, recursoTecnologicoService, usuarioService);
        this.usuarioActual = usuarioActual;

        boolean esEstudiante = usuarioActual.getRol().equalsIgnoreCase("Estudiante");
        initUI(!esEstudiante, esEstudiante ? usuarioActual : null);
    }

    private void initUI(boolean mostrarUsuarios, Usuario estudiante) {
        Color grisClaro = new Color(245, 245, 245);
        Color grisOscuro = new Color(60, 63, 65);
        Color textoOscuro = new Color(33, 33, 33);

        getContentPane().setBackground(grisClaro);

        setTitle("Gestión de Préstamos");
        setSize(600, 320);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel encabezado = new JLabel("📦 Gestión de Préstamos", SwingConstants.CENTER);
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 22));
        encabezado.setForeground(textoOscuro);
        encabezado.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(encabezado, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 10));
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
            comboUsuarios.setEnabled(false);
            cargarRecursosPrestados(estudiante);
        }

        cargarRecursosDisponibles();

        panelFormulario.add(new JLabel("Usuario:"));
        panelFormulario.add(comboUsuarios);

        panelFormulario.add(new JLabel("Recurso disponible:"));
        panelFormulario.add(comboRecursosDisponibles);

        panelFormulario.add(new JLabel("Recurso prestado:"));
        panelFormulario.add(comboRecursosPrestados);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(grisClaro);

        JButton btnPrestar = new JButton("Registrar préstamo");
        btnPrestar.setBackground(grisOscuro); // Fondo claro o el que estés usando
        btnPrestar.setForeground(Color.BLACK); // Letras oscuras
        btnPrestar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnPrestar.addActionListener(e -> {
            try {
                registrarPrestamo();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar préstamo: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnDevolver = new JButton("Registrar devolución");
        btnDevolver.setBackground(new Color(23, 36, 46)); // Mantienes este fondo
        btnDevolver.setForeground(Color.BLACK); // Letras oscuras
        btnDevolver.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnDevolver.addActionListener(e -> {
            try {
                registrarDevolucion();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar devolución: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        panelBotones.add(btnPrestar);
        panelBotones.add(btnDevolver);

        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void cargarUsuarios() {
        comboUsuarios.removeAllItems();
        try {
            List<Usuario> usuarios = usuarioService.listarUsuarios();
            for (Usuario u : usuarios) {
                comboUsuarios.addItem(u);
            }

            if (comboUsuarios.getItemCount() > 0) {
                Usuario primero = (Usuario) comboUsuarios.getSelectedItem();
                cargarRecursosPrestados(primero);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando usuarios: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarRecursosDisponibles() {
        comboRecursosDisponibles.removeAllItems();
        try {
            // Solo cargar recursos multimedia disponibles (sin tecnológicos)
            List<Multimedia> multimediaDisponibles = prestamoService.listarRecursosDisponibles();
            for (Multimedia r : multimediaDisponibles) {
                comboRecursosDisponibles.addItem(r);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando recursos disponibles: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void cargarRecursosPrestados(Usuario usuario) {
        comboRecursosPrestados.removeAllItems(); // Limpia combo antes
        if (usuario == null) return;

        try {
            List<RecursoPrestado> prestados = prestamoService.listarRecursosPrestadosPorUsuarioComoObjetos(usuario.getId());
            for (RecursoPrestado rp : prestados) {
                comboRecursosPrestados.addItem(rp);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando recursos prestados: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void registrarPrestamo() throws SQLException {
        Usuario usuario = (Usuario) comboUsuarios.getSelectedItem();
        Object recurso = comboRecursosDisponibles.getSelectedItem();

        if (usuario == null || recurso == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario y un recurso.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tipoRecurso = null;
        String idRecurso = null;

        if (recurso instanceof Multimedia) {
            tipoRecurso = "Multimedia";
            idRecurso = ((Multimedia) recurso).getId();
        } else if (recurso instanceof RecursoTecnologico) {
            tipoRecurso = "Tecnologico";
            idRecurso = ((RecursoTecnologico) recurso).getId();
        } else {
            JOptionPane.showMessageDialog(this, "Tipo de recurso no soportado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean ok = prestamoService.registrarPrestamo(usuario.getId(), idRecurso, tipoRecurso);
        if (ok) {
            JOptionPane.showMessageDialog(this, "✅ Préstamo registrado correctamente.");
            cargarRecursosDisponibles();
            cargarRecursosPrestados(usuario);
        } else {
            JOptionPane.showMessageDialog(this, "❌ No se pudo registrar el préstamo.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarDevolucion() throws SQLException {
        Usuario usuario = (Usuario) comboUsuarios.getSelectedItem();
        RecursoPrestado recursoPrestado = (RecursoPrestado) comboRecursosPrestados.getSelectedItem();

        if (usuario == null || recursoPrestado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario y un recurso prestado.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idPrestamo = recursoPrestado.getPrestamo().getId();
        String tipoRecurso = recursoPrestado.getMultimedia() != null ? "Multimedia" : "Tecnologico";

        boolean ok = prestamoService.registrarDevolucion(idPrestamo, tipoRecurso);
        if (ok) {
            JOptionPane.showMessageDialog(this, "✅ Devolución registrada correctamente.");
            cargarRecursosDisponibles();
            cargarRecursosPrestados(usuario);
        } else {
            JOptionPane.showMessageDialog(this, "❌ No se pudo registrar la devolución.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
