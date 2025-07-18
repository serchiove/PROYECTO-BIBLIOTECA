package com.biblioteca.data;

import com.biblioteca.multimedia.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MultimediaDAO {

    private final Connection conexion;

    public MultimediaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public List<Multimedia> obtenerRecursosDisponibles() {
        List<Multimedia> disponibles = new ArrayList<>();
        String sql = "SELECT * FROM multimedia WHERE disponible = true";

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Multimedia recurso = crearMultimediaDesdeResultSet(rs);
                if (recurso != null) {
                    disponibles.add(recurso);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener recursos disponibles: " + e.getMessage());
        }

        return disponibles;
    }

    private Multimedia crearMultimediaDesdeResultSet(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        if (tipo == null) {
            System.err.println("❌ Tipo es null en registro id=" + rs.getString("id"));
            return null;
        }
        tipo = tipo.toLowerCase().trim();

        String id = rs.getString("id");
        String titulo = rs.getString("titulo");
        String autor = rs.getString("autor");
        boolean disponible = rs.getBoolean("disponible");

        return switch (tipo) {
            case "librodigital" -> new LibroDigital(id, titulo, autor,
                    rs.getString("isbn"),
                    rs.getInt("tamanoMB"),
                    disponible);
            case "video" -> new Video(id, titulo, autor,
                    rs.getInt("duracionSegundos"),
                    disponible);
            case "audio" -> new Audio(id, titulo, autor,
                    rs.getInt("duracionMinutos"),
                    disponible);
            case "mapa" -> new Mapa(id, titulo, autor,
                    rs.getString("region"),
                    disponible);
            case "infografia" -> new Infografia(id, titulo, autor,
                    rs.getString("tema"),
                    disponible);
            case "presentacion" -> new Presentacion(id, titulo, autor,
                    rs.getInt("numeroDiapositivas"),
                    disponible);
            case "revista" -> new Revista(id, titulo, autor,
                    rs.getInt("numero"),
                    disponible);
            case "tesis" -> new Tesis(id, titulo, autor,
                    rs.getString("universidad"),
                    disponible);
            default -> {
                System.err.println("❌ Tipo desconocido: " + tipo + " para id=" + id);
                yield null;
            }
        };
    }

    public boolean insertarRecurso(Multimedia recurso) {
        String sql = "INSERT INTO multimedia (id, titulo, autor, disponible, tipo, isbn, tamanoMB, duracionSegundos, " +
                "duracionMinutos, region, tema, numeroDiapositivas, numero, universidad) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, recurso.getId());
            stmt.setString(2, recurso.getTitulo());
            stmt.setString(3, recurso.getAutor());
            stmt.setBoolean(4, recurso.isDisponible());

            stmt.setString(5, recurso.getClass().getSimpleName());

            // Inicializar campos nulos para evitar valores erróneos
            stmt.setNull(6, Types.VARCHAR);
            stmt.setNull(7, Types.INTEGER);
            stmt.setNull(8, Types.INTEGER);
            stmt.setNull(9, Types.INTEGER);
            stmt.setNull(10, Types.VARCHAR);
            stmt.setNull(11, Types.VARCHAR);
            stmt.setNull(12, Types.INTEGER);
            stmt.setNull(13, Types.INTEGER);
            stmt.setNull(14, Types.VARCHAR);

            if (recurso instanceof LibroDigital l) {
                stmt.setString(6, l.getIsbn());
                stmt.setInt(7, l.getTamanoMB());
            } else if (recurso instanceof Video v) {
                stmt.setInt(8, v.getDuracionSegundos());
            } else if (recurso instanceof Audio a) {
                stmt.setInt(9, a.getDuracionMinutos());
            } else if (recurso instanceof Mapa m) {
                stmt.setString(10, m.getRegion());
            } else if (recurso instanceof Infografia i) {
                stmt.setString(11, i.getTema());
            } else if (recurso instanceof Presentacion p) {
                stmt.setInt(12, p.getNumeroDiapositivas());
            } else if (recurso instanceof Revista r) {
                stmt.setInt(13, r.getNumero());
            } else if (recurso instanceof Tesis t) {
                stmt.setString(14, t.getUniversidad());
            }

            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al insertar recurso multimedia: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Multimedia recurso) {
        String sql = "UPDATE multimedia SET titulo = ?, autor = ?, disponible = ? WHERE id = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, recurso.getTitulo());
            stmt.setString(2, recurso.getAutor());
            stmt.setBoolean(3, recurso.isDisponible());
            stmt.setString(4, recurso.getId());
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar recurso multimedia: " + e.getMessage());
            return false;
        }
    }


    public boolean actualizarDisponibilidad(String id, boolean disponible) {
        String sql = "UPDATE multimedia SET disponible = ? WHERE id = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setBoolean(1, disponible);
            stmt.setString(2, id);
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar disponibilidad: " + e.getMessage());
            return false;
        }
    }

    public List<Multimedia> obtenerTodosLosRecursos() {
        List<Multimedia> recursos = new ArrayList<>();
        String sql = "SELECT * FROM multimedia";

        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Multimedia recurso = crearMultimediaDesdeResultSet(rs);
                if (recurso != null) recursos.add(recurso);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener los recursos multimedia: " + e.getMessage());
        }

        return recursos;
    }

    public Multimedia obtenerPorId(String id) {
        String sql = "SELECT * FROM multimedia WHERE id = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return crearMultimediaDesdeResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar recurso multimedia por ID: " + e.getMessage());
        }

        return null;
    }

    public boolean eliminarRecurso(String id) {
        String sql = "DELETE FROM multimedia WHERE id = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, id);
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar recurso: " + e.getMessage());
            return false;
        }
    }
}
