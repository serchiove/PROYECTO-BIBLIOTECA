package com.biblioteca.gui;

import com.biblioteca.servicios.UsuarioService;
import com.biblioteca.usuarios.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionUsuariosFrame extends JFrame {

    private final UsuarioService usuarioService;

    private JTable tablaUsuarios;
    private DefaultTableModel modeloUsuarios;
    private JButton btnAgregar;
    private JButton btnEliminar;

    public GestionUsuariosFrame(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;

        setTitle("Gestión de Usuarios");
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        cargarUsuarios();
        setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // Ahora agregamos columna "Usuario" en el modelo
        modeloUsuarios = new DefaultTableModel(new Object[]{"ID", "Usuario", "Nombre Completo", "Rol"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaUsuarios = new JTable(modeloUsuarios);
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);

        JPanel panelBotones = new JPanel();
        btnAgregar = new JButton("Agregar Usuario");
        btnEliminar = new JButton("Eliminar Usuario");

        btnAgregar.addActionListener(e -> agregarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);

        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarUsuarios() {
        modeloUsuarios.setRowCount(0);
        List<Usuario> usuarios = usuarioService.listarUsuarios();

        for (Usuario u : usuarios) {
            modeloUsuarios.addRow(new Object[]{
                    u.getId(),
                    u.getUsuario(),    // Aquí mostramos el usuario (login)
                    u.getNombre(),
                    u.getRol()
            });
        }
    }

    private void agregarUsuario() {
        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtUsuario = new JTextField(); // Campo para login
        JTextField txtRol = new JTextField();
        JTextField txtPassword = new JTextField();

        Object[] campos = {
                "ID:", txtId,
                "Nombre completo:", txtNombre,
                "Usuario (login):", txtUsuario,
                "Rol (ADMIN, PROFESOR, ESTUDIANTE):", txtRol,
                "Password:", txtPassword
        };

        int opcion = JOptionPane.showConfirmDialog(this, campos, "Agregar Usuario", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            String usuarioLogin = txtUsuario.getText().trim();
            String rol = txtRol.getText().trim().toUpperCase();
            String password = txtPassword.getText().trim();

            if (id.isEmpty() || nombre.isEmpty() || usuarioLogin.isEmpty() || rol.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe completar todos los campos.");
                return;
            }

            boolean creado = usuarioService.crearUsuario(id, nombre, usuarioLogin, password, rol);
            if (creado) {
                JOptionPane.showMessageDialog(this, "Usuario creado con éxito.");
                cargarUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear el usuario.");
            }
        }
    }

    private void eliminarUsuario() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.");
            return;
        }

        String idUsuario = (String) modeloUsuarios.getValueAt(filaSeleccionada, 0);
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar al usuario seleccionado?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean eliminado = usuarioService.eliminarUsuario(idUsuario);
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado.");
                cargarUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el usuario.");
            }
        }
    }
}
