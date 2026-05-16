package modelo;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;

public class PersonaDAO {

    private final File archivo = new File("contactos.txt");

    // Lectura del archivo principal
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock  = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    // Exportaciones simultáneas
    private final ReentrantLock exportLock = new ReentrantLock();

    // Bloqueo por nombre
    private final Map<String, String> contactosBloqueados = new HashMap<>();

    public PersonaDAO() {
        try {
            if (!archivo.exists()) archivo.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Bloqueo de contacto

    /**
     * Intento de bloqueo de contacto
     * @return true si el bloqueo fue exitoso, false si ya está bloqueado por otro hilo.
     */
    public synchronized boolean bloquearContacto(String nombre) {
        String hiloActual = Thread.currentThread().getName();
        if (contactosBloqueados.containsKey(nombre)) {
            String hiloEditor = contactosBloqueados.get(nombre);
            return hiloEditor.equals(hiloActual);
        }
        contactosBloqueados.put(nombre, hiloActual);
        return true;
    }

    // Liberación de bloqueo
    public synchronized void liberarContacto(String nombre) {
        contactosBloqueados.remove(nombre);
    }
    
    // Verificación de bloqueo por hilo
    public synchronized boolean estaEditando(String nombre) {
        return contactosBloqueados.containsKey(nombre);
    }

    public void guardar(Persona p) {
        writeLock.lock();
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo, true))) {
            pw.println(p.formatoArchivo());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    public List<Persona> listar() {
        readLock.lock();
        try {
            return leerArchivo();
        } finally {
            readLock.unlock();
        }
    }

    private List<Persona> leerArchivo() {
        List<Persona> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] x = linea.split(",");
                if (x.length == 5) {
                    lista.add(new Persona(x[0], x[1], x[2], x[3], Boolean.parseBoolean(x[4])));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    // Verificación de duplicados
    public boolean existeContacto(String nombre, String telefono) {
        readLock.lock();
        try {
            for (Persona p : leerArchivo()) {
                if (p.getNombre().equalsIgnoreCase(nombre.trim()) &&
                    p.getTelefono().equalsIgnoreCase(telefono.trim())) {
                    return true;
                }
            }
            return false;
        } finally {
            readLock.unlock();
        }
    }
    
    // Busqueda
    public List<Persona> buscarPorNombre(String texto) {
        readLock.lock();
        try {
            List<Persona> resultado = new ArrayList<>();
            String textoBajo = texto.toLowerCase().trim();
            for (Persona p : leerArchivo()) {
                if (p.getNombre().toLowerCase().contains(textoBajo)) {
                    resultado.add(p);
                }
            }
            return resultado;
        } finally {
            readLock.unlock();
        }
    }


    public void actualizarArchivo(List<Persona> lista) {
        writeLock.lock();
        try (PrintWriter pw = new PrintWriter(archivo)) {
            for (Persona p : lista) pw.println(p.formatoArchivo());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }
    
    // CVS
    public boolean exportarCSV(List<Persona> lista) {
        if (!exportLock.tryLock()) {
            return false; // ya hay una exportación en curso
        }
        try (PrintWriter pw = new PrintWriter("contactos.csv")) {
            pw.println("Nombre,Telefono,Email,Categoria,Favorito");
            for (Persona p : lista) pw.println(p.formatoArchivo());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            exportLock.unlock();
        }
    }
}