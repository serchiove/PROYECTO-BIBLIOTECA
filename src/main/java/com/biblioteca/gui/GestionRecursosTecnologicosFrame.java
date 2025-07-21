package com.biblioteca.gui;

import com.biblioteca.recurso.RecursoTecnologico;
import com.biblioteca.servicios.RecursoTecnologicoService;
import com.biblioteca.usuarios.Usuario;
import com.biblioteca.servicios.UsuarioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionRecursosTecnologicosFrame extends JFrame {

    private JTable tablaRecursos;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboTipo;
    private JButton btnAgregar, btnEliminar, btnActualizar, btnReservar, btnDevolver;

    private final RecursoTecnologicoService recursoService;
    private final UsuarioService usuarioService;

    public GestionRecursosTecnologicosFrame(RecursoTecnologicoService recursoService, UsuarioService usuarioService) {
        this.recursoService = recursoService;
        this.usuarioService = usuarioService;

        setTitle("Gestión de Recursos Tecnológicos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior con título
        JLabel titulo = new JLabel("Gestión de Recursos Tecnológicos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        // Modelo y tabla para recursos
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Tipo", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaRecursos = new JTable(modeloTabla);
        tablaRecursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tablaRecursos);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con controles
        JPanel panelControles = new JPanel(new BorderLayout());

        // Panel para selección de tipo y botones
        JPanel panelTipoYBotones = new JPanel(new BorderLayout());

        // Panel tipo
        JPanel panelTipo = new JPanel();
        comboTipo = new JComboBox<>(new String[]{"Tablet", "Computadora"});
        panelTipo.add(new JLabel("Tipo de Recurso:"));
        panelTipo.add(comboTipo);
        panelTipoYBotones.add(panelTipo, BorderLayout.WEST);

        // Panel botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        btnAgregar = new JButton("Agregar");
        btnEliminar = new JButton("Eliminar");
        btnReservar = new JButton("Reservar");
        btnDevolver = new JButton("Devolver");
        btnActualizar = new JButton("Actualizar Lista");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnReservar);
        panelBotones.add(btnDevolver);
        panelBotones.add(btnActualizar);

        panelTipoYBotones.add(panelBotones, BorderLayout.EAST);

        panelControles.add(panelTipoYBotones, BorderLayout.NORTH);

        add(panelControles, BorderLayout.SOUTH);

        // Asociar eventos con acciones
        btnAgregar.addActionListener(e -> agregarRecurso());
        btnEliminar.addActionListener(e -> eliminarRecurso());
        btnActualizar.addActionListener(e -> cargarRecursos());
        btnReservar.addActionListener(e -> reservarRecurso());
        btnDevolver.addActionListener(e -> devolverRecurso());

        // Cargar recursos inicialmente
        cargarRecursos();

        setVisible(true);
    }

    private void cargarRecursos() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<RecursoTecnologico> recursos = recursoService.listarTodos();
        for (RecursoTecnologico r : recursos) {
            modeloTabla.addRow(new Object[]{r.getId(), r.getTipo(), r.getEstado()});
        }
    }

    private void agregarRecurso() {
        String tipo = (String) comboTipo.getSelectedItem();
        if (tipo == null || tipo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un tipo válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean agregado = recursoService.agregarRecurso(new RecursoTecnologico(tipo));
        if (agregado) {
            cargarRecursos();
            JOptionPane.showMessageDialog(this, "Recurso agregado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar el recurso.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarRecurso() {
        int filaSeleccionada = tablaRecursos.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un recurso para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) modeloTabla.getValueAt(filaSeleccionada, 0);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea eliminar el recurso seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean eliminado = recursoService.eliminarRecurso(id);
            if (eliminado) {
                cargarRecursos();
                JOptionPane.showMessageDialog(this, "Recurso eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el recurso.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void reservarRecurso() {
        int filaSeleccionada = tablaRecursos.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un recurso para reservar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idRecurso = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        String estado = (String) modeloTabla.getValueAt(filaSeleccionada, 2);

        if ("No disponible".equalsIgnoreCase(estado) || "Reservado".equalsIgnoreCase(estado)) {
            JOptionPane.showMessageDialog(this, "El recurso ya está reservado o no disponible.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<Usuario> usuarios;
        try {
            // Filtrar solo estudiantes (opcional, según necesidades)
            usuarios = usuarioService.listarUsuarios().stream()
                    .filter(u -> u.getRol().equalsIgnoreCase("ESTUDIANTE"))
                    .toList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] nombresUsuarios = usuarios.stream()
                .map(u -> u.getNombre() + " (" + u.getRol() + ")")
                .toArray(String[]::new);

        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el usuario para reservar el recurso:",
                "Reservar recurso",
                JOptionPane.QUESTION_MESSAGE,
                null,
                nombresUsuarios,
                nombresUsuarios.length > 0 ? nombresUsuarios[0] : null);

        if (seleccion == null) return; // Canceló selección

        int indiceSeleccionado = -1;
        for (int i = 0; i < nombresUsuarios.length; i++) {
            if (nombresUsuarios[i].equals(seleccion)) {
                indiceSeleccionado = i;
                break;
            }
        }

        if (indiceSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Usuario no válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Usuario usuarioSeleccionado = usuarios.get(indiceSeleccionado);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Desea reservar el recurso para " + usuarioSeleccionado.getNombre() + "?",
                "Confirmar reserva",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion != JOptionPane.YES_OPTION) return;

        boolean reservado = recursoService.reservarPorIdYUsuario(idRecurso, usuarioSeleccionado.getId());

        if (reservado) {
            cargarRecursos();
            JOptionPane.showMessageDialog(this, "Recurso reservado con éxito para " + usuarioSeleccionado.getNombre() + ".");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo reservar el recurso.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void devolverRecurso() {
        int filaSeleccionada = tablaRecursos.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un recurso para devolver.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        String estado = (String) modeloTabla.getValueAt(filaSeleccionada, 2);

        if ("Disponible".equalsIgnoreCase(estado)) {
            JOptionPane.showMessageDialog(this, "El recurso ya está disponible, no se puede devolver.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boolean devuelto = recursoService.devolverPorId(id);
        if (devuelto) {
            cargarRecursos();
            JOptionPane.showMessageDialog(this, "Recurso devuelto con éxito.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo devolver el recurso.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
