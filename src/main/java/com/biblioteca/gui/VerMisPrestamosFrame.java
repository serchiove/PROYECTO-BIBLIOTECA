package com.biblioteca.gui;

import com.biblioteca.data.Prestamo;
import com.biblioteca.multimedia.Multimedia;
import com.biblioteca.servicios.PrestamoService;
import com.biblioteca.usuarios.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class VerMisPrestamosFrame extends JFrame {

    private final PrestamoService prestamoService;
    private final Usuario estudiante;
    private final JTable tabla;
    private final DefaultTableModel modelo;

    public VerMisPrestamosFrame(Usuario estudiante, PrestamoService prestamoService) {
        super("Mis Préstamos");
        this.estudiante = estudiante;
        this.prestamoService = prestamoService;

        modelo = new DefaultTableModel(new Object[]{
                "ID Préstamo", "Título del Recurso", "Fecha de Préstamo", "Fecha Límite", "Días Restantes"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evita edición
            }
        };

        tabla = new JTable(modelo);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabla.setRowHeight(25);
        tabla.setDefaultRenderer(Object.class, new VencimientoRenderer());

        JScrollPane scroll = new JScrollPane(tabla);

        setLayout(new BorderLayout(10, 10));
        add(scroll, BorderLayout.CENTER);

        setSize(750, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cargarMisPrestamos();
        setVisible(true);
    }

    private void cargarMisPrestamos() {
        modelo.setRowCount(0); // Limpia tabla
        List<Prestamo> prestamos = prestamoService.listarPrestamosActivos();

        for (Prestamo p : prestamos) {
            if (p.getIdUsuario().equals(estudiante.getId())) {
                LocalDate hoy = LocalDate.now();
                long diasRestantes = Duration.between(hoy.atStartOfDay(), p.getFechaFin().atStartOfDay()).toDays();

                Multimedia recurso = prestamoService.getMultimediaService().obtenerPorId(p.getIdRecurso());
                String titulo = (recurso != null) ? recurso.getTitulo() : "Recurso no encontrado";

                String estadoDias = (diasRestantes >= 0) ? diasRestantes + " días" : "VENCIDO";

                modelo.addRow(new Object[]{
                        p.getId(),
                        titulo,
                        p.getFechaInicio(),
                        p.getFechaFin(),
                        estadoDias
                });
            }
        }
    }

    public void actualizarTabla() {
        cargarMisPrestamos();
    }

    // Clase para resaltar préstamos vencidos
    private static class VencimientoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column
        ) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String diasRestantes = (String) table.getModel().getValueAt(row, 4);
            if (diasRestantes.contains("VENCIDO")) {
                c.setForeground(Color.RED);
            } else {
                c.setForeground(Color.BLACK);
            }
            return c;
        }
    }
}
