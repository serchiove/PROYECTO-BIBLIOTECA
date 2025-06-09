
package servicios;

import java.util.*;
import multimedia.Multimedia;

public class MultimediaService {
    private Map<Integer, Multimedia> catalogo = new HashMap<>();

    public void registrarMultimedia(Multimedia m) {
        catalogo.put(m.id, m);
        System.out.println("Multimedia registrada: " + m.getTitulo());
    }

    public Multimedia buscarMultimedia(int id) {
        return catalogo.get(id);
    }

    public void reproducirPorId(int id) {
        Multimedia m = catalogo.get(id);
        if (m != null) {
            m.reproducir();
        } else {
            System.out.println("No se encontró multimedia con ID: " + id);
        }
    }
}
