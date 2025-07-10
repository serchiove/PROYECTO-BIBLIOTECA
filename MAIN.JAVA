import SERVICIOS.UsuarioService;
import SERVICIOS.PrestamoService;
import RECURSOS.RecursoService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Instancias de los servicios
        UsuarioService usuarioService = new UsuarioService();
        PrestamoService prestamoService = new PrestamoService();
        RecursoService recursoService = new RecursoService();
        Scanner scanner = new Scanner(System.in);

        boolean autenticado = false;
        String usuarioActual = "";

        while (true) {
            System.out.println("\n===== SISTEMA DE BIBLIOTECA =====");

            if (!autenticado) {
                System.out.println("1. Iniciar sesión");
                System.out.println("2. Salir");
                System.out.print("Elige una opción: ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); // limpiar buffer

                if (opcion == 1) {
                    System.out.print("Usuario: ");
                    String user = scanner.nextLine();
                    System.out.print("Contraseña: ");
                    String pass = scanner.nextLine();

                    // Aquí puedes reemplazar con autenticación real
                    if (user.equals("admin") && pass.equals("123")) {
                        autenticado = true;
                        usuarioActual = user;
                        System.out.println("Sesión iniciada correctamente.");
                    } else {
                        System.out.println("Credenciales incorrectas.");
                    }
                } else if (opcion == 2) {
                    System.out.println("Saliendo del sistema...");
                    break;
                } else {
                    System.out.println("Opción inválida.");
                }

            } else {
                System.out.println("\nBienvenido, " + usuarioActual);
                System.out.println("1. Ver recursos disponibles");
                System.out.println("2. Reservar recurso");
                System.out.println("3. Liberar recurso");
                System.out.println("4. Cerrar sesión");
                System.out.print("Elige una opción: ");
                int opcion = scanner.nextInt();

                switch (opcion) {
                    case 1:
                        recursoService.mostrarRecursos();
                        break;
                    case 2:
                        System.out.print("ID del recurso a reservar: ");
                        int idRes = scanner.nextInt();
                        if (recursoService.reservarRecurso(idRes)) {
                            System.out.println("Recurso reservado correctamente.");
                        } else {
                            System.out.println("Ya está reservado.");
                        }
                        break;
                    case 3:
                        System.out.print("ID del recurso a liberar: ");
                        int idLib = scanner.nextInt();
                        if (recursoService.liberarRecurso(idLib)) {
                            System.out.println("Recurso liberado.");
                        } else {
                            System.out.println("No se encontró el recurso.");
                        }
                        break;
                    case 4:
                        autenticado = false;
                        usuarioActual = "";
                        System.out.println("Sesión cerrada.");
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            }
        }

        scanner.close();
    }
}
