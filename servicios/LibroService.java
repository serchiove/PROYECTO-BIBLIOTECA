        String sql = "SELECT * FROM libros";
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LibroDigital libro = construirLibroDesdeResultSet(rs);
                lista.add(libro);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar libros: " + e.getMessage());
            throw e;
        }

        return lista;
    }

    public LibroDigital buscarLibroPorId(String id) throws SQLException {
        String sql = "SELECT * FROM libros WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construirLibroDesdeResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar libro por ID: " + e.getMessage());
            throw e;
        }

        return null;
    }

    public void eliminarLibro(String id) throws SQLException {
        String sql = "DELETE FROM libros WHERE id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
            throw e;
        }
    }

    private LibroDigital construirLibroDesdeResultSet(ResultSet rs) throws SQLException {
        return new LibroDigital(
                rs.getString("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("isbn"),
                rs.getInt("tamanoMB"),
                rs.getBoolean("disponible")
        );
    }
}
