
package multimedia;

public class Audio extends Multimedia {
    private double duracion;
    private String formato;

    public Audio(int id, String titulo, String genero, String autor, double duracion, String formato) {
        super(id, titulo, genero, autor);
        this.duracion = duracion;
        this.formato = formato;
    }

    public void reproducirAudio() {
        System.out.println("Reproduciendo audio: " + titulo);
    }

    @Override
    public void reproducir() {
        reproducirAudio();
    }
}
