package com.biblioteca.gui;

import com.biblioteca.usuarios.Usuario;
import com.biblioteca.servicios.UsuarioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionUsuariosFrame extends JFrame {

    private UsuarioService usuarioService;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    public GestionUsuariosFrame() {
        super("Gestión de Usuarios");
        usuarioService = new UsuarioService();
        initUI();
        cargarUsuarios();
    }

    private void initUI() {
        Color rojoUTP = new Color(183, 28, 28);
        Color grisClaro = new Color(245, 245, 245);
        getContentPane().setBackground(grisClaro);
        setLayout(new BorderLayout());

        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel encabezado = new JLabel("👥 Gestión de Usuarios", SwingConstants.CENTER);
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 22));
        encabezado.setForeground(rojoUTP);
        encabezado.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(encabezado, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Usuario", "Rol"}, 0);
        tablaUsuarios = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        tablaUsuarios.setFillsViewportHeight(true);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnAgregar = new JButton("➕ Registrar Estudiante");
        btnAgregar.setBackground(rojoUTP);
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.addActionListener(e -> registrarEstudiante());

        JButton btnActualizar = new JButton("🔄 Actualizar Lista");
        btnActualizar.setBackground(rojoUTP);
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.addActionListener(e -> cargarUsuarios());

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(grisClaro);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);

        add(panelBotones, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void cargarUsuarios() {
        modeloTabla.setRowCount(0);
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        for (Usuario u : usuarios) {
            modeloTabla.addRow(new Object[]{
                    u.getId(), u.getNombre(), u.getUsuario(), u.getRol()
            });
        }
    }

    private void registrarEstudiante() {
        JTextField campoId = new JTextField();
        JTextField campoNombre = new JTextField();
        JTextField campoCorreo = new JTextField();
        JTextField campoCarrera = new JTextField();

        Object[] campos = {
                "ID:", campoId,
                "Nombre:", campoNombre,
                "Correo:", campoCorreo,
                "Carrera:", campoCarrera
        };

        int opcion = JOptionPane.showConfirmDialog(
                this, campos, "Nuevo Estudiante", JOptionPane.OK_CANCEL_OPTION);

        if (opcion == JOptionPane.OK_OPTION) {
            usuarioService.registrarEstudiante(
                    campoId.getText(),
                    campoNombre.getText(),
                    campoCorreo.getText(),
                    campoCarrera.getText()
            );
            cargarUsuarios();
            JOptionPane.showMessageDialog(this, "Estudiante registrado correctamente.");
        }
    }
}
