package com.biblioteca.gui;

import com.biblioteca.recurso.RecursoTecnologico;
import com.biblioteca.data.RecursoTecnologicoDAO;
import com.biblioteca.servicios.RecursoTecnologicoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GestionRecursosTecnologicosFrame extends JFrame {

    private JTextArea areaRecursos;
    private RecursoTecnologicoService recursoService;

    public GestionRecursosTecnologicosFrame(Connection conexion) {
        setTitle("Gestión de Recursos Tecnológicos");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        RecursoTecnologicoDAO dao = new RecursoTecnologicoDAO(conexion);
        recursoService = new RecursoTecnologicoService(dao);

        JPanel panelBotones = new JPanel();
        JButton btnVerDisponibles = new JButton("Ver disponibles");
        JButton btnReservarTablet = new JButton("Reservar Tablet");
        JButton btnReservarPC = new JButton("Reservar Computadora");

        btnVerDisponibles.addActionListener(this::verDisponibles);
        btnReservarTablet.addActionListener(this::reservarTablet);
        btnReservarPC.addActionListener(this::reservarComputadora);

        panelBotones.add(btnVerDisponibles);
        panelBotones.add(btnReservarTablet);
        panelBotones.add(btnReservarPC);

        areaRecursos = new JTextArea();
        areaRecursos.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaRecursos);

        setLayout(new BorderLayout());
        add(panelBotones, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    private void verDisponibles(ActionEvent e) {
        try {
            List<RecursoTecnologico> disponibles = recursoService.listarDisponibles();
            areaRecursos.setText("Recursos disponibles:\n");
            if (disponibles.isEmpty()) {
                areaRecursos.append("No hay recursos disponibles.\n");
            } else {
                for (RecursoTecnologico r : disponibles) {
                    areaRecursos.append(r.toString() + "\n");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al listar recursos: " + ex.getMessage());
        }
    }

    private void reservarTablet(ActionEvent e) {
        try {
            boolean reservado = recursoService.reservarPorTipo("Tablet");
            mostrarResultadoReserva(reservado, "Tablet");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al reservar tablet: " + ex.getMessage());
        }
    }

    private void reservarComputadora(ActionEvent e) {
        try {
            boolean reservado = recursoService.reservarPorTipo("Computadora");
            mostrarResultadoReserva(reservado, "Computadora");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al reservar computadora: " + ex.getMessage());
        }
    }

    private void mostrarResultadoReserva(boolean exito, String tipo) {
        if (exito) {
            JOptionPane.showMessageDialog(this, tipo + " reservada con éxito.");
        } else {
            JOptionPane.showMessageDialog(this, "No hay " + tipo.toLowerCase() + "s disponibles.");
        }
        verDisponibles(null); // refrescar lista
    }
}
