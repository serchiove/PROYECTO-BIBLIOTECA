
package usuarios;

public abstract class Usuario {
    public int id_usuario;
    public String nombre;
    public String correo;
    public String telefono;
    public String direccion;

    public abstract void login();

    public void verPerfil() {
        System.out.println("Usuario: " + nombre + ", Correo: " + correo);
    }
}
