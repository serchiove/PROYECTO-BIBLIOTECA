package com.biblioteca.servicios;

import com.biblioteca.data.Prestamo;
import com.biblioteca.data.PrestamoDAO;
import com.biblioteca.data.PrestamoRecursoTecnologicoDAO;
import com.biblioteca.data.RecursoPrestado;
import com.biblioteca.multimedia.Multimedia;
import com.biblioteca.recurso.RecursoTecnologico;
import com.biblioteca.usuarios.Usuario;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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

    public MultimediaService getMultimediaService() {
        return multimediaService;
    }


    public boolean registrarPrestamo(String idUsuario, String idRecurso, String tipoRecurso) throws SQLException {
        if (idUsuario == null || idUsuario.isBlank() || idRecurso == null || idRecurso.isBlank()) {
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
    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    public boolean registrarDevolucion(String idPrestamo, String tipoRecurso) throws SQLException {
        LocalDate fechaDevolucion = LocalDate.now();

        if ("Multimedia".equalsIgnoreCase(tipoRecurso)) {
            boolean ok = prestamoDAO.registrarDevolucion(idPrestamo, fechaDevolucion);
            if (!ok) return false;

            Prestamo p = prestamoDAO.cargarPrestamos().stream()
                    .filter(prest -> prest.getId().equals(idPrestamo))
                    .findFirst()
                    .orElse(null);

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
                    .findFirst()
                    .orElse(null);

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
    public List<RecursoPrestado> listarRecursosPrestadosPorUsuarioComoObjetos(String idUsuario) {
        List<RecursoPrestado> lista = new ArrayList<>();

        try {
            // Prestamos Multimedia
            List<Prestamo> prestamosMultimedia = prestamoDAO.obtenerPrestamosPorUsuario(idUsuario);
            for (Prestamo p : prestamosMultimedia) {
                Multimedia m = multimediaService.buscarPorId(p.getIdRecurso());
                if (m != null) {
                    lista.add(new RecursoPrestado(p, m));
                }
            }

            // Prestamos Tecnológicos
            List<Prestamo> prestamosTec = prestamoRecTecDAO.obtenerPrestamosPorUsuario(idUsuario);
            for (Prestamo p : prestamosTec) {
                RecursoTecnologico rt = recursoTecnologicoService.buscarPorId(p.getIdRecurso());
                if (rt != null) {
                    lista.add(new RecursoPrestado(p, rt));
                }
            }

        } catch (Exception e) {
            System.err.println("Error al listar recursos prestados como objetos: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }
    public List<Prestamo> listarPrestamos() {
        return listarPrestamos("Multimedia");
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
    public RecursoTecnologicoService getRecursoTecnologicoService() {
        return recursoTecnologicoService;
    }
    public List<RecursoPrestado> listarRecursosPrestadosActivosComoObjetos() {
        List<Prestamo> prestamosActivos = this.listarPrestamosActivos();
        List<RecursoPrestado> recursos = new ArrayList<>();

        for (Prestamo p : prestamosActivos) {
            Multimedia m = multimediaService.buscarPorId(p.getIdRecurso());
            if (m != null) {
                recursos.add(new RecursoPrestado(p, m)); // usa constructor Prestamo + Multimedia
            } else {
                RecursoTecnologico t = recursoTecnologicoService.buscarPorId(p.getIdRecurso());
                if (t != null) {
                    recursos.add(new RecursoPrestado(p, t)); // usa constructor Prestamo + Tecnologico
                }
            }
        }

        return recursos;
    }


    public List<Prestamo> listarPrestamosPorUsuario(String idUsuario) {
        return listarPrestamosPorUsuario(idUsuario, "Multimedia");
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

    public List<Prestamo> listarPrestamosPorUsuarioTodosTipos(String idUsuario) {
        try {
            List<Prestamo> lista = new ArrayList<>();
            lista.addAll(prestamoDAO.obtenerPrestamosPorUsuario(idUsuario));
            lista.addAll(prestamoRecTecDAO.obtenerPrestamosPorUsuario(idUsuario));
            return lista;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Prestamo> listarPrestamosActivos() {
        List<Prestamo> todos = new ArrayList<>();
        try {
            todos.addAll(prestamoDAO.cargarPrestamos().stream().filter(p -> !p.isDevuelto()).toList());
            todos.addAll(prestamoRecTecDAO.cargarPrestamos().stream().filter(p -> !p.isDevuelto()).toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return todos;
    }

    public List<Prestamo> listarPrestamosActivosPorUsuario(String idUsuario) {
        List<Prestamo> todos = listarPrestamosPorUsuarioTodosTipos(idUsuario);
        List<Prestamo> activos = new ArrayList<>();
        for (Prestamo p : todos) {
            if (!p.isDevuelto()) {
                activos.add(p);
            }
        }
        return activos;
    }

    public Object buscarRecursoPorId(String idRecurso) {
        Multimedia multimedia = multimediaService.buscarPorId(idRecurso);
        if (multimedia != null) return multimedia;

        return recursoTecnologicoService.buscarPorId(idRecurso);
    }

    public List<Multimedia> listarRecursosDisponibles() {
        try {
            return multimediaService.listarRecursosDisponibles();
        } catch (Exception e) {
            System.err.println("Error al listar recursos multimedia disponibles: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Multimedia> listarRecursosPrestadosPorUsuario(String idUsuario) {
        try {
            List<Prestamo> prestamos = prestamoDAO.obtenerPrestamosPorUsuario(idUsuario);
            return prestamos.stream()
                    .map(prestamo -> multimediaService.buscarPorId(prestamo.getIdRecurso()))
                    .filter(recurso -> recurso != null)
                    .toList();
        } catch (Exception e) {
            System.err.println("Error al listar recursos prestados por usuario: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    public boolean devolverRecurso(String idPrestamo, LocalDate fechaDevolucion, String tipoRecurso) {
        if ("Multimedia".equalsIgnoreCase(tipoRecurso)) {
            return prestamoDAO.registrarDevolucion(idPrestamo, fechaDevolucion);
        } else if ("Tecnologico".equalsIgnoreCase(tipoRecurso)) {
            return prestamoRecTecDAO.registrarDevolucion(idPrestamo, fechaDevolucion);
        } else {
            System.err.println("Tipo de recurso desconocido para devolución: " + tipoRecurso);
            return false;
        }
    }



}
