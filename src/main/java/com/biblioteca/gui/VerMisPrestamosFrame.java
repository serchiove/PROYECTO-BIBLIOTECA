package com.biblioteca.gui;

import com.biblioteca.data.Prestamo;
import com.biblioteca.multimedia.Multimedia;
import com.biblioteca.servicios.PrestamoService;
import com.biblioteca.usuarios.Usuario;

import com.biblioteca.recurso.RecursoTecnologico;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class VerMisPrestamosFrame extends JFrame {

    private final PrestamoService prestamoService;
    private final Usuario estudiante;
    private final JTable tabla;
    private final DefaultTableModel modelo;

    public VerMisPrestamosFrame(Usuario estudiante, PrestamoService prestamoService) {
        super("📚 Mis Préstamos Activos");
        this.estudiante = estudiante;
        this.prestamoService = prestamoService;

        modelo = new DefaultTableModel(new Object[]{
                "ID Préstamo", "Título del Recurso", "Fecha de Préstamo", "Fecha Límite", "Días Restantes"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // evita que el usuario edite directamente
            }
        };

        tabla = new JTable(modelo);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabla.setRowHeight(25);
        tabla.setDefaultRenderer(Object.class, new VencimientoRenderer());

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        cargarMisPrestamos();
        setVisible(true);
    }

    private void cargarMisPrestamos() {
        modelo.setRowCount(0); // Limpia la tabla antes de cargar

        List<Prestamo> prestamos = prestamoService.listarPrestamosActivos();

        for (Prestamo p : prestamos) {
            if (!p.getIdUsuario().equals(estudiante.getId())) continue;

            LocalDate hoy = LocalDate.now();
            LocalDate fechaInicio = p.getFechaInicio();
            LocalDate fechaFin = p.getFechaFin() != null ? p.getFechaFin() : fechaInicio.plusDays(7);
            long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaFin);

            String estadoDias = diasRestantes >= 0 ? diasRestantes + " días" : "VENCIDO";

            Object recurso = prestamoService.buscarRecursoPorId(p.getIdRecurso());
            String titulo = "Recurso no encontrado";

            if (recurso instanceof Multimedia) {
                titulo = ((Multimedia) recurso).getTitulo();
            } else if (recurso instanceof RecursoTecnologico) {
                titulo = ((RecursoTecnologico) recurso).getTipo();  // o cualquier campo que identifique el recurso
            }

            modelo.addRow(new Object[]{
                    p.getId(),
                    titulo,
                    fechaInicio,
                    fechaFin,
                    estadoDias
            });
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

            if (!isSelected) {
                Object val = table.getModel().getValueAt(row, 4);
                if (val != null && val.toString().contains("VENCIDO")) {
                    c.setForeground(Color.RED);
                } else {
                    c.setForeground(Color.BLACK);
                }
            }

            return c;
        }
    }
}
