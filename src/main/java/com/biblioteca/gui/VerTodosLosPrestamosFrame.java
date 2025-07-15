package com.biblioteca.gui;

import com.biblioteca.data.MultimediaDAO;
import com.biblioteca.data.UsuarioDAO;
import com.biblioteca.data.Prestamo;
import com.biblioteca.multimedia.Multimedia;
import com.biblioteca.servicios.PrestamoService;
import com.biblioteca.usuarios.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class VerTodosLosPrestamosFrame extends JFrame {

    private final PrestamoService prestamoService;
    private final UsuarioDAO usuarioDAO;
    private final MultimediaDAO multimediaDAO;

    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel etiquetaTotal;

    public VerTodosLosPrestamosFrame(PrestamoService prestamoService, UsuarioDAO usuarioDAO, MultimediaDAO multimediaDAO) {
        super("Listado de Préstamos Activos");
        this.prestamoService = prestamoService;
        this.usuarioDAO = usuarioDAO;
        this.multimediaDAO = multimediaDAO;

        initUI();
        cargarPrestamos();
    }

    private void initUI() {
        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        modelo = new DefaultTableModel(
                new Object[]{"ID Préstamo", "ID Usuario", "Nombre Usuario", "ID Recurso", "Título Recurso", "Fecha Préstamo", "Fecha Límite"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hace la tabla no editable
            }
        };

        tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);

        etiquetaTotal = new JLabel("Total de préstamos activos: 0");
        etiquetaTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        etiquetaTotal.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        add(scrollPane, BorderLayout.CENTER);
        add(etiquetaTotal, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void cargarPrestamos() {
        modelo.setRowCount(0);
        List<Prestamo> prestamos = prestamoService.listarPrestamosActivos();

        for (Prestamo p : prestamos) {
            Usuario usuario = null;
            try {
                usuario = usuarioDAO.obtenerPorId(p.getIdUsuario());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Multimedia recurso = multimediaDAO.obtenerPorId(p.getIdRecurso());

            String nombreUsuario = (usuario != null) ? usuario.getNombre() : "Desconocido";
            String tituloRecurso = (recurso != null) ? recurso.getTitulo() : "Desconocido";

            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getIdUsuario(),
                    nombreUsuario,
                    p.getIdRecurso(),
                    tituloRecurso,
                    p.getFechaInicio(),
                    p.getFechaFin()
            });
        }

        etiquetaTotal.setText("Total de préstamos activos: " + prestamos.size());
    }
}
