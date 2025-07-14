package com.biblioteca.gui;

import com.biblioteca.data.Prestamo;
import com.biblioteca.servicios.PrestamoService;
import com.biblioteca.usuarios.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RegistrarDevolucionFrame extends JFrame {
    private final PrestamoService prestamoService;
    private final Usuario usuario;
    private final JList<Prestamo> listaPrestamos;
    private final VerMisPrestamosFrame verMisPrestamosFrame;

    public RegistrarDevolucionFrame(Usuario usuario, PrestamoService prestamoService, VerMisPrestamosFrame verMisPrestamosFrame) {
        this.usuario = usuario;
        this.prestamoService = prestamoService;
        this.verMisPrestamosFrame = verMisPrestamosFrame;

        setTitle("↩️ Registrar Devolución");
        setSize(550, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        listaPrestamos = new JList<>();
        listaPrestamos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPrestamos.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cargarPrestamos();

        JScrollPane scrollPane = new JScrollPane(listaPrestamos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("📋 Préstamos activos"));
        add(scrollPane, BorderLayout.CENTER);

        JButton btnDevolver = new JButton("✅ Registrar devolución");
        btnDevolver.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnDevolver.addActionListener(e -> registrarDevolucion());
        add(btnDevolver, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void cargarPrestamos() {
        DefaultListModel<Prestamo> modelo = new DefaultListModel<>();
        List<Prestamo> prestamos;

        if (usuario.getRol().equalsIgnoreCase("ADMIN") || usuario.getRol().equalsIgnoreCase("BIBLIOTECARIO")) {
            prestamos = prestamoService.listarPrestamos();
        } else {
            prestamos = prestamoService.prestamosPorUsuario(usuario.getId());
        }

        for (Prestamo p : prestamos) {
            if (!p.isDevuelto()) {
                modelo.addElement(p);
            }
        }

        listaPrestamos.setModel(modelo);
    }

    private void registrarDevolucion() {
        Prestamo seleccionado = listaPrestamos.getSelectedValue();

        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debes seleccionar un préstamo para registrar la devolución.");
            return;
        }

        boolean exito = prestamoService.registrarDevolucion(
                seleccionado.getIdUsuario(),
                seleccionado.getIdRecurso()
        );

        if (exito) {
            JOptionPane.showMessageDialog(this, "Devolución registrada exitosamente.");
            cargarPrestamos();

            if (verMisPrestamosFrame != null) {
                verMisPrestamosFrame.actualizarTabla();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar la devolución. Verifica los datos.");
        }
    }
}
