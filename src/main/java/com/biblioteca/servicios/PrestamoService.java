package com.biblioteca.servicios;

import com.biblioteca.data.Prestamo;
import com.biblioteca.data.PrestamoDAO;
import com.biblioteca.data.PrestamoRecursoTecnologicoDAO;
import com.biblioteca.multimedia.Multimedia;
import com.biblioteca.recurso.RecursoTecnologico;
import com.biblioteca.usuarios.Usuario;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PrestamoService {

    private final PrestamoDAO prestamoDAO;
    private final PrestamoRecursoTecnologicoDAO prestamoRecTecDAO;
    private final MultimediaService multimediaService;
    private final RecursoTecnologicoService recursoTecnologicoService;
    private final UsuarioService usuarioService;

    public PrestamoService(PrestamoDAO prestamoDAO,
                           PrestamoRecursoTecnologicoDAO prestamoRecTecDAO,
                           MultimediaService multimediaService,
                           RecursoTecnologicoService recursoTecnologicoService,
                           UsuarioService usuarioService) {
        this.prestamoDAO = prestamoDAO;
        this.prestamoRecTecDAO = prestamoRecTecDAO;
        this.multimediaService = multimediaService;
        this.recursoTecnologicoService = recursoTecnologicoService;
        this.usuarioService = usuarioService;
    }

    public boolean registrarPrestamo(String idUsuario, String idRecurso, String tipoRecurso) throws SQLException {
        if (idUsuario == null || idUsuario.isEmpty() || idRecurso == null || idRecurso.isEmpty()) {
            System.err.println("ID Usuario o Recurso inválido para registrar préstamo");
            return false;
        }

        Usuario usuario = usuarioService.buscarUsuarioPorId(idUsuario);
        if (usuario == null) {
            System.err.println("Usuario no encontrado");
            return false;
        }

        String idPrestamo = UUID.randomUUID().toString();
        LocalDate fechaInicio = LocalDate.now();

        if ("Multimedia".equalsIgnoreCase(tipoRecurso)) {
            Multimedia recurso = multimediaService.buscarPorId(idRecurso);
            if (recurso == null || !recurso.isDisponible()) {
                System.err.println("Recurso multimedia no válido o no disponible");
                return false;
            }
            if (prestamoDAO.existenPrestamosParaRecurso(idRecurso)) {
                System.err.println("El recurso multimedia ya está prestado");
                return false;
            }

            boolean registrado = prestamoDAO.registrarPrestamo(idPrestamo, idUsuario, idRecurso, fechaInicio, tipoRecurso);
            if (!registrado) return false;

            recurso.setDisponible(false);
            multimediaService.actualizarRecurso(recurso);

            System.out.println("Préstamo multimedia registrado con id: " + idPrestamo);
            return true;

        } else if ("Tecnologico".equalsIgnoreCase(tipoRecurso)) {
            RecursoTecnologico recursoTec = recursoTecnologicoService.buscarPorId(idRecurso);
            if (recursoTec == null || !"Disponible".equalsIgnoreCase(recursoTec.getEstado())) {
                System.err.println("Recurso tecnológico no válido o no disponible");
                return false;
            }
            if (prestamoRecTecDAO.existenPrestamosParaRecurso(idRecurso)) {
                System.err.println("El recurso tecnológico ya está prestado");
                return false;
            }

            boolean registrado = prestamoRecTecDAO.registrarPrestamo(idPrestamo, idUsuario, idRecurso, fechaInicio, tipoRecurso);
            if (!registrado) return false;

            recursoTec.setEstado("Reservado");
            recursoTecnologicoService.actualizarRecursoTecnologico(recursoTec);

            System.out.println("Préstamo tecnológico registrado con id: " + idPrestamo);
            return true;

        } else {
            System.err.println("Tipo de recurso desconocido: " + tipoRecurso);
            return false;
        }
    }

    public boolean registrarDevolucion(String idPrestamo, String tipoRecurso) throws SQLException {
        LocalDate fechaDevolucion = LocalDate.now();

        if ("Multimedia".equalsIgnoreCase(tipoRecurso)) {
            boolean ok = prestamoDAO.registrarDevolucion(idPrestamo, fechaDevolucion);
            if (!ok) return false;

            Prestamo p = prestamoDAO.cargarPrestamos().stream()
                    .filter(prest -> prest.getId().equals(idPrestamo))
                    .findFirst().orElse(null);

            if (p != null) {
                Multimedia recurso = multimediaService.buscarPorId(p.getIdRecurso());
                if (recurso != null) {
                    recurso.setDisponible(true);
                    multimediaService.actualizarRecurso(recurso);
                }
            }
            System.out.println("Devolución multimedia registrada para préstamo: " + idPrestamo);
            return true;

        } else if ("Tecnologico".equalsIgnoreCase(tipoRecurso)) {
            boolean ok = prestamoRecTecDAO.registrarDevolucion(idPrestamo, fechaDevolucion);
            if (!ok) return false;

            Prestamo p = prestamoRecTecDAO.cargarPrestamos().stream()
                    .filter(prest -> prest.getId().equals(idPrestamo))
                    .findFirst().orElse(null);

            if (p != null) {
                RecursoTecnologico recursoTec = recursoTecnologicoService.buscarPorId(p.getIdRecurso());
                if (recursoTec != null) {
                    recursoTec.setEstado("Disponible");
                    recursoTecnologicoService.actualizarRecursoTecnologico(recursoTec);
                }
            }
            System.out.println("Devolución tecnológica registrada para préstamo: " + idPrestamo);
            return true;

        } else {
            System.err.println("Tipo de recurso desconocido para devolución: " + tipoRecurso);
            return false;
        }
    }

    public List<Prestamo> listarPrestamos(String tipoRecurso) {
        try {
            if ("Multimedia".equalsIgnoreCase(tipoRecurso)) {
                return prestamoDAO.cargarPrestamos();
            } else if ("Tecnologico".equalsIgnoreCase(tipoRecurso)) {
                return prestamoRecTecDAO.cargarPrestamos();
            } else {
                System.err.println("Tipo de recurso desconocido para listar préstamos: " + tipoRecurso);
                return List.of();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Prestamo> listarPrestamosPorUsuario(String idUsuario, String tipoRecurso) {
        try {
            if ("Multimedia".equalsIgnoreCase(tipoRecurso)) {
                return prestamoDAO.obtenerPrestamosPorUsuario(idUsuario);
            } else if ("Tecnologico".equalsIgnoreCase(tipoRecurso)) {
                return prestamoRecTecDAO.obtenerPrestamosPorUsuario(idUsuario);
            } else {
                System.err.println("Tipo de recurso desconocido para listar préstamos por usuario: " + tipoRecurso);
                return List.of();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
