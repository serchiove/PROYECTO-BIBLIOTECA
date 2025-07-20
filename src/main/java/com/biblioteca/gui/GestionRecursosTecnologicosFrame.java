package com.biblioteca.gui;

import com.biblioteca.recurso.RecursoTecnologico;
import com.biblioteca.servicios.RecursoTecnologicoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionRecursosTecnologicosFrame extends JFrame {

    private JTable tablaRecursos;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboTipo;
    private JButton btnAgregar, btnEliminar, btnActualizar, btnReservar, btnDevolver;
    private RecursoTecnologicoService recursoService;

    public GestionRecursosTecnologicosFrame(RecursoTecnologicoService recursoService) {
        this.recursoService = recursoService;
        setTitle("Gestión de Recursos Tecnológicos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior con título
        JLabel titulo = new JLabel("Gestión de Recursos Tecnológicos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        // Panel central con tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Tipo", "Estado"}, 0);
        tablaRecursos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaRecursos);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con controles
        JPanel panelControles = new JPanel(new GridLayout(2, 1, 10, 5));

        // Panel para selección de tipo
        JPanel panelTipo = new JPanel();
        comboTipo = new JComboBox<>(new String[]{"Tablet", "Computadora"});
        panelTipo.add(new JLabel("Tipo de Recurso:"));
        panelTipo.add(comboTipo);
        panelControles.add(panelTipo);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        btnAgregar = new JButton("Agregar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar Lista");
        btnReservar = new JButton("Reservar");
        btnDevolver = new JButton("Devolver");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnReservar);
        panelBotones.add(btnDevolver);
        panelBotones.add(btnActualizar);

        panelControles.add(panelBotones);

        add(panelControles, BorderLayout.SOUTH);

        // Eventos
        btnAgregar.addActionListener(e -> agregarRecurso());
        btnEliminar.addActionListener(e -> eliminarRecurso());
        btnActualizar.addActionListener(e -> cargarRecursos());
        btnReservar.addActionListener(e -> reservarRecurso());
        btnDevolver.addActionListener(e -> devolverRecurso());

        cargarRecursos();
        setVisible(true);
    }

    private void cargarRecursos() {
        modeloTabla.setRowCount(0);
        List<RecursoTecnologico> recursos = recursoService.listarTodos(); // Cambia a listarTodos para mostrar todos recursos
        for (RecursoTecnologico r : recursos) {
            modeloTabla.addRow(new Object[]{r.getId(), r.getTipo(), r.getEstado()});
        }
    }

    private void agregarRecurso() {
        String tipo = (String) comboTipo.getSelectedItem();
        if (tipo != null) {
            recursoService.agregarRecurso(new RecursoTecnologico(tipo));
            cargarRecursos();
            JOptionPane.showMessageDialog(this, "Recurso agregado correctamente.");
        }
    }

    private void eliminarRecurso() {
        int filaSeleccionada = tablaRecursos.getSelectedRow();
        if (filaSeleccionada >= 0) {
            String id = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
            boolean eliminado = recursoService.eliminarRecurso(id);
            if (eliminado) {
                cargarRecursos();
                JOptionPane.showMessageDialog(this, "Recurso eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el recurso.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un recurso para eliminar.");
        }
    }

    private void reservarRecurso() {
        int filaSeleccionada = tablaRecursos.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un recurso para reservar.");
            return;
        }

        String id = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        String estado = (String) modeloTabla.getValueAt(filaSeleccionada, 2);

        if ("No disponible".equalsIgnoreCase(estado) || "Reservado".equalsIgnoreCase(estado)) {
            JOptionPane.showMessageDialog(this, "El recurso ya está reservado o no disponible.");
            return;
        }

        boolean reservado = recursoService.reservarPorId(id);
        if (reservado) {
            JOptionPane.showMessageDialog(this, "Recurso reservado con éxito.");
            cargarRecursos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo reservar el recurso.");
        }
    }

    private void devolverRecurso() {
        int filaSeleccionada = tablaRecursos.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un recurso para devolver.");
            return;
        }

        String id = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        String estado = (String) modeloTabla.getValueAt(filaSeleccionada, 2);

        if ("Disponible".equalsIgnoreCase(estado)) {
            JOptionPane.showMessageDialog(this, "El recurso ya está disponible, no se puede devolver.");
            return;
        }

        boolean devuelto = recursoService.devolverPorId(id);
        if (devuelto) {
            JOptionPane.showMessageDialog(this, "Recurso devuelto con éxito.");
            cargarRecursos();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo devolver el recurso.");
        }
    }
}
