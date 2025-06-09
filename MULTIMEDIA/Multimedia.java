
package multimedia;

public abstract class Multimedia {
    public int id;
    public String titulo;
    public String genero;
    public String autor;

    public Multimedia(int id, String titulo, String genero, String autor) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.autor = autor;
    }

    public abstract void reproducir();

    public String getTitulo() {
        return titulo;
    }
}
