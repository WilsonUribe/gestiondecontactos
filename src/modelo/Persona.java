package modelo;

public class Persona {

    private String nombre;
    private String telefono;
    private String email;
    private String categoria;
    private boolean favorito;

    public Persona(String nombre, String telefono,
                   String email, String categoria,
                   boolean favorito) {

        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.categoria = categoria;
        this.favorito = favorito;
    }

    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getCategoria() { return categoria; }
    public boolean isFavorito() { return favorito; }

    // Formato para JTable
    public String[] formatoTabla() {

        return new String[] {
            nombre,
            telefono,
            email,
            categoria,
            favorito ? "SI" : "NO"
        };
    }

    // Formato para archivo txt / csv
    public String formatoArchivo() {

        return nombre + "," +
               telefono + "," +
               email + "," +
               categoria + "," +
               favorito;
    }
}