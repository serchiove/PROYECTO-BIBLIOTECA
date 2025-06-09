
package usuarios;

public class Bibliotecario extends Usuario {
    public String codigoEmpleado;

    public Bibliotecario(int id, String nombre, String correo, String telefono, String direccion, String codigoEmpleado) {
        this.id_usuario = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.codigoEmpleado = codigoEmpleado;
    }

    public void registrarLibro() {
        System.out.println("Libro registrado por: " + nombre);
    }

    @Override
    public void login() {
        System.out.println("Bibliotecario " + nombre + " ha iniciado sesión.");
    }
}
