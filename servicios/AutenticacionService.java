    public Usuario autenticar(String usuarioIngresado, String passwordIngresado) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuarioIngresado);
            ps.setString(2, passwordIngresado);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construirUsuarioDesdeResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error en autenticación: " + e.getMessage());
        }

        return null;
    }

    public List<Usuario> obtenerTodosUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = construirUsuarioDesdeResultSet(rs);
                if (u != null) lista.add(u);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al obtener usuarios: " + e.getMessage());
        }

        return lista;
    }

    private Usuario construirUsuarioDesdeResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String nombre = rs.getString("nombre");
        String usuario = rs.getString("usuario");
        String contrasena = rs.getString("contrasena");
        String rol = rs.getString("rol");

        return switch (rol.toUpperCase()) {
            case "ADMIN" -> new Administrador(id, nombre, usuario, contrasena);
            case "PROFESOR" -> new Profesor(id, nombre, usuario, contrasena);
            case "ESTUDIANTE" -> new Estudiante(id, nombre, usuario, contrasena);
            default -> {
                System.err.println("⚠️ Rol desconocido: " + rol);
                yield null;
            }
        };
    }
}
