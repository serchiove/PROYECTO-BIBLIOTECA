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
import java.util.List;

public class VerTodosLosPrestamosFrame extends JFrame {

    private final PrestamoService prestamoService;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel etiquetaTotal;

    public VerTodosLosPrestamosFrame(PrestamoService prestamoService) {
        super("Listado de Préstamos Activos");
        this.prestamoService = prestamoService;

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
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabla.setRowHeight(22);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("📑 Préstamos Activos"));

        etiquetaTotal = new JLabel(" ");
        etiquetaTotal.setFont(new Font("SansSerif", Font.BOLD, 12));
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInferior.add(etiquetaTotal);

        add(scroll, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void cargarPrestamos() {
        modelo.setRowCount(0);
        List<Prestamo> prestamos = prestamoService.listarPrestamosActivos();

        for (Prestamo p : prestamos) {
            Usuario usuario = UsuarioDAO.obtenerPorId(p.getIdUsuario());
            Multimedia recurso = MultimediaDAO.obtenerPorId(p.getIdRecurso());

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

        etiquetaTotal.setText("otal de préstamos activos: " + modelo.getRowCount());
    }
}
