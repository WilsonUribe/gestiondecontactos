package modelo;
import java.io.*;
import java.util.*;

public class PersonaDAO {

    private File archivo = new File("contactos.txt");

    public PersonaDAO() {

        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Guardado del contacto
    public void guardar(Persona p) {

        try (PrintWriter pw =
                     new PrintWriter(
                             new FileWriter(archivo, true))) {

            pw.println(p.formatoArchivo());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lectura de contactos
    public List<Persona> listar() {

        List<Persona> lista = new ArrayList<>();

        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(archivo))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                String[] x = linea.split(",");

                if (x.length == 5) {

                    Persona p = new Persona(
                            x[0],
                            x[1],
                            x[2],
                            x[3],
                            Boolean.parseBoolean(x[4])
                    );

                    lista.add(p);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void actualizarArchivo(List<Persona> lista) {

        try (PrintWriter pw =
                     new PrintWriter(archivo)) {

            for (Persona p : lista) {
                pw.println(p.formatoArchivo());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Exportar CSV
    public void exportarCSV(List<Persona> lista) {

        try (PrintWriter pw =
                     new PrintWriter("contactos.csv")) {

            pw.println("Nombre,Telefono,Email,Categoria,Favorito");

            for (Persona p : lista) {
                pw.println(p.formatoArchivo());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}