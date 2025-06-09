
package usuarios;

public class Estudiante extends Usuario {
    public String carrera;

    public Estudiante(int id, String nombre, String correo, String telefono, String direccion, String carrera) {
        this.id_usuario = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.carrera = carrera;
    }

    public void registrarUsuario() {
        System.out.println("Estudiante registrado: " + nombre);
    }

    public void modificarUsuario() {
        System.out.println("Datos modificados para: " + nombre);
    }

    @Override
    public void login() {
        System.out.println("Estudiante " + nombre + " ha iniciado sesión.");
    }
}
