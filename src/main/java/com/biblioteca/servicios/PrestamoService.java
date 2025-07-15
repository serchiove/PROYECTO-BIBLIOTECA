package com.biblioteca.servicios;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.biblioteca.data.Prestamo;
import com.biblioteca.data.PrestamoDAO;
import com.biblioteca.multimedia.Multimedia;
import com.biblioteca.usuarios.Usuario;

public class PrestamoService {
    private final PrestamoDAO prestamoDAO;
    private final MultimediaService multimediaService;
    private final UsuarioService usuarioService;

    public PrestamoService(PrestamoDAO prestamoDAO, MultimediaService multimediaService, UsuarioService usuarioService) {
        this.prestamoDAO = prestamoDAO;
        this.multimediaService = multimediaService;
        this.usuarioService = usuarioService;
    }

    public boolean registrarPrestamo(String idUsuario, String idRecurso) {
        try {
            Usuario usuario = usuarioService.buscarUsuarioPorId(idUsuario);
            Multimedia recurso = multimediaService.buscarPorId(idRecurso);

            if (usuario == null || recurso == null || !recurso.isDisponible()) {
                return false;
            }

            List<Prestamo> prestamos = prestamoDAO.cargarPrestamos();
            for (Prestamo p : prestamos) {
                if (p.getIdUsuario().equals(idUsuario) &&
                        p.getIdRecurso().equals(idRecurso) &&
                        !p.isDevuelto()) {
                    return false; // Ya tiene este recurso sin devolver
                }
            }

            String idPrestamo = UUID.randomUUID().toString();
            LocalDate fechaInicio = LocalDate.now();
            LocalDate fechaFin = fechaInicio.plusDays(7); // Préstamo por 7 días

            Prestamo nuevo = new Prestamo(idPrestamo, idUsuario, idRecurso, fechaInicio, fechaFin);
            prestamos.add(nuevo);

            prestamoDAO.guardarPrestamos(prestamos);

            recurso.setDisponible(false);
            multimediaService.actualizarRecurso(recurso);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Multimedia> listarRecursosPrestadosPorUsuario(String usuarioId) {
        List<Multimedia> recursosPrestados = new ArrayList<>();

        try {
            List<Prestamo> prestamos = prestamoDAO.cargarPrestamos();
            for (Prestamo prestamo : prestamos) {
                if (prestamo.getIdUsuario().equals(usuarioId) && prestamo.getFechaDevolucion() == null) {
                    Multimedia recurso = multimediaService.buscarPorId(prestamo.getIdRecurso());
                    if (recurso != null) {
                        recursosPrestados.add(recurso);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recursosPrestados;
    }

    public boolean registrarDevolucion(String idUsuario, String idRecurso) {
        try {
            List<Prestamo> prestamos = prestamoDAO.cargarPrestamos();
            boolean encontrado = false;

            for (Prestamo p : prestamos) {
                if (p.getIdUsuario().equals(idUsuario) &&
                        p.getIdRecurso().equals(idRecurso) &&
                        !p.isDevuelto()) {

                    p.marcarComoDevuelto(); // Marca como devuelto y asigna fecha
                    multimediaService.marcarComoDisponible(idRecurso);
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                prestamoDAO.guardarPrestamos(prestamos);
            }

            return encontrado;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Prestamo> listarPrestamos() {
        try {
            return prestamoDAO.cargarPrestamos();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Prestamo> prestamosPorUsuario(String idUsuario) {
        try {
            List<Prestamo> todos = prestamoDAO.cargarPrestamos();
            List<Prestamo> resultado = new ArrayList<>();

            for (Prestamo p : todos) {
                if (p.getIdUsuario().equals(idUsuario)) {
                    resultado.add(p);
                }
            }

            return resultado;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Prestamo> listarPrestamosActivos() {
        return listarPrestamos().stream()
                .filter(p -> p.getFechaDevolucion() == null)
                .collect(Collectors.toList());
    }

    public List<Multimedia> obtenerRecursosReservadosPorUsuario(String idUsuario) {
        List<Multimedia> reservados = new ArrayList<>();

        try {
            for (Prestamo p : prestamoDAO.cargarPrestamos()) {
                if (p.getIdUsuario().equals(idUsuario) && !p.isDevuelto()) {
                    Multimedia recurso = multimediaService.buscarPorId(p.getIdRecurso());
                    if (recurso != null) {
                        reservados.add(recurso);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservados;
    }

    public List<Prestamo> listarPrestamosPorUsuario(String nombreUsuario) {
        List<Prestamo> prestamosUsuario = new ArrayList<>();
        try {
            for (Prestamo p : prestamoDAO.cargarPrestamos()) {
                Usuario usuario = usuarioService.buscarUsuarioPorId(p.getIdUsuario());
                if (usuario != null && usuario.getUsuario().equals(nombreUsuario)) {
                    prestamosUsuario.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prestamosUsuario;
    }
    // PrestamoService.java
    public List<Prestamo> obtenerPrestamosDeUsuario(String idUsuario) {
        try {
            return prestamoDAO.obtenerPrestamosPorUsuario(idUsuario);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public MultimediaService getMultimediaService() {
        return multimediaService;
    }

    public Multimedia buscarRecursoPorId(String id) {
        return multimediaService != null ? multimediaService.buscarPorId(id) : null;
    }

}
