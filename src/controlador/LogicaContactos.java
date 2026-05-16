package controlador;
import vista.Ventana;
import modelo.*;

import java.awt.event.*;
import java.util.List;
import java.util.concurrent.*;
import javax.swing.*;

public class LogicaContactos implements ActionListener, KeyListener {

    private final Ventana vista;
    private final PersonaDAO dao;
    private final Notificaciones notif;

    // Hilo para operaciones de fondo secuenciales
    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "FondoWorker");
        t.setDaemon(true);
        return t;
    });

    // SwingWorker 
    private SwingWorker<List<Persona>, Void> busquedaWorker;

    // Contactos existentes para bloqueo
    private String contactoEnEdicion = null;

    public LogicaContactos() {
        vista = new Ventana();
        dao   = new PersonaDAO();
        notif = new Notificaciones(vista.barra);

        vista.btnAgregar.addActionListener(this);
        vista.btnEliminar.addActionListener(this);
        vista.btnExportar.addActionListener(this);
        vista.btnModificar.addActionListener(this);
        vista.btnEstadisticas.addActionListener(this);
        vista.txtBuscar.addKeyListener(this);

        cargarTabla();
        vista.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LogicaContactos::new);
    }

    // Tabla
    public void cargarTabla() {
        executor.submit(() -> {
            List<Persona> lista = dao.listar();

            SwingUtilities.invokeLater(() -> {
                vista.modelo.setRowCount(0);
                vista.barra.setValue(0);
            });

            if (lista.isEmpty()) {
                SwingUtilities.invokeLater(() -> vista.barra.setValue(100));
                return;
            }

            int total = lista.size();
            int contador = 0;

            for (Persona p : lista) {
                final int progreso = (++contador * 100) / total;
                final Object[] fila = p.formatoTabla();

                SwingUtilities.invokeLater(() -> {
                    vista.modelo.addRow(fila);
                    vista.barra.setValue(progreso);
                });

                try { Thread.sleep(30); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        });
    }

    // Threads

    private void agregarContacto() {
        String nombre   = vista.txtNombre.getText().trim();
        String telefono = vista.txtTelefono.getText().trim();
        String email    = vista.txtEmail.getText().trim();

        if (nombre.isEmpty() || telefono.isEmpty()) {
            notif.mostrarError("Complete nombre y teléfono antes de agregar.");
            return;
        }

        vista.btnAgregar.setEnabled(false);
        notif.mostrarInfo("Verificando datos...");

        // Thread de validación de duplicados
        Thread validador = new Thread(() -> {
            boolean duplicado = dao.existeContacto(nombre, telefono);

            SwingUtilities.invokeLater(() -> {
                vista.btnAgregar.setEnabled(true);

                if (duplicado) {
                    notif.mostrarError("El contacto ya está registrado.");
                    return;
                }

                Persona p = new Persona(
                    nombre, telefono, email,
                    vista.cmbCategoria.getSelectedItem().toString(),
                    vista.chkFavorito.isSelected()
                );
                dao.guardar(p);
                limpiar();
                cargarTabla();
                notif.mostrarExito("Contacto guardado con éxito.");
            });
        }, "ValidadorDuplicados");

        validador.setDaemon(true);
        validador.start();
    }

    // Busqueda Swingworker
    public void buscarPorNombre() {
        if (busquedaWorker != null && !busquedaWorker.isDone()) {
            busquedaWorker.cancel(true);
        }

        String texto = vista.txtBuscar.getText();

        busquedaWorker = new SwingWorker<List<Persona>, Void>() {
            @Override
            protected List<Persona> doInBackground() throws Exception {
                Thread.sleep(200);
                if (isCancelled()) return null;
                return dao.buscarPorNombre(texto);
            }

            @Override
            protected void done() {
                if (isCancelled()) return;
                try {
                    List<Persona> resultado = get();
                    // Actualizaciín de tabla
                    SwingUtilities.invokeLater(() -> {
                        vista.modelo.setRowCount(0);
                        if (resultado != null) {
                            for (Persona p : resultado) {
                                vista.modelo.addRow(p.formatoTabla());
                            }
                        }
                    });
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        busquedaWorker.execute();
    }

    // Exportar CSV
    private void exportarCSV() {
        notif.mostrarInfo("Exportando contactos...");
        vista.btnExportar.setEnabled(false);

        executor.submit(() -> {
            // Simular progreso visual
            for (int i = 0; i <= 100; i += 10) {
                final int prog = i;
                SwingUtilities.invokeLater(() -> vista.barra.setValue(prog));
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }

            List<Persona> lista = dao.listar();
            boolean exito = dao.exportarCSV(lista);

            SwingUtilities.invokeLater(() -> {
                vista.btnExportar.setEnabled(true);
                if (exito) {
                    notif.mostrarExito("Exportación completada: contactos.csv");
                } else {
                    notif.mostrarError("Ya hay una exportación en curso. Espere.");
                }
            });
        });
    }

    private void modificarContacto() {
        int fila = vista.tabla.getSelectedRow();

        if (fila == -1) {
            notif.mostrarError("Seleccione un contacto para modificar.");
            return;
        }

        List<Persona> lista = dao.listar();
        Persona p = lista.get(fila);

        // Bloqueo en modificacion de contacto
        boolean bloqueado = dao.bloquearContacto(p.getNombre());
        if (!bloqueado) {
            notif.mostrarError("El contacto está siendo editado. Intente luego.");
            return;
        }

        // Liberar bloqueo
        if (contactoEnEdicion != null) {
            dao.liberarContacto(contactoEnEdicion);
        }
        contactoEnEdicion = p.getNombre();

        // Carga de datos
        vista.txtNombre.setText(p.getNombre());
        vista.txtTelefono.setText(p.getTelefono());
        vista.txtEmail.setText(p.getEmail());
        vista.cmbCategoria.setSelectedItem(p.getCategoria());
        vista.chkFavorito.setSelected(p.isFavorito());
        lista.remove(fila);
        dao.actualizarArchivo(lista);
        cargarTabla();

        notif.mostrarInfo("Modifique los datos y presione AGREGAR.");
    }

    private void eliminarContacto() {
        int fila = vista.tabla.getSelectedRow();

        if (fila == -1) {
            notif.mostrarError("Seleccione un contacto para eliminar.");
            return;
        }

        List<Persona> lista = dao.listar();
        String nombre = lista.get(fila).getNombre();

        if (dao.estaEditando(nombre)) {
            notif.mostrarError("No se puede eliminar: el contacto está en edición.");
            return;
        }

        lista.remove(fila);
        dao.actualizarArchivo(lista);
        cargarTabla();
        notif.mostrarExito("Contacto eliminado correctamente.");
    }

    private void mostrarEstadisticas() {
        executor.submit(() -> {
            List<Persona> lista = dao.listar();
            int total = lista.size(), favoritos = 0;
            int familia = 0, trabajo = 0, amigos = 0, otros = 0;

            for (Persona p : lista) {
                if (p.isFavorito()) favoritos++;
                switch (p.getCategoria()) {
                    case "Familia": familia++; break;
                    case "Trabajo": trabajo++; break;
                    case "Amigos":  amigos++;  break;
                    default:        otros++;
                }
            }

            String msg =
                "TOTAL: " + total +
                "\nFAVORITOS: " + favoritos +
                "\n\nCATEGORÍAS:" +
                "\nFamilia: " + familia +
                "\nTrabajo: " + trabajo +
                "\nAmigos: "  + amigos +
                "\nOtros: "   + otros;

            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(vista, msg, "Estadísticas", JOptionPane.INFORMATION_MESSAGE)
            );
        });
    }

    public void limpiar() {
        if (contactoEnEdicion != null) {
            dao.liberarContacto(contactoEnEdicion);
            contactoEnEdicion = null;
        }
        vista.txtNombre.setText("");
        vista.txtTelefono.setText("");
        vista.txtEmail.setText("");
        vista.chkFavorito.setSelected(false);
        vista.cmbCategoria.setSelectedIndex(0);
    }
    
    // Eventos
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnAgregar)      agregarContacto();
        if (e.getSource() == vista.btnEliminar)      eliminarContacto();
        if (e.getSource() == vista.btnModificar)     modificarContacto();
        if (e.getSource() == vista.btnExportar)      exportarCSV();
        if (e.getSource() == vista.btnEstadisticas)  mostrarEstadisticas();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == vista.txtBuscar) buscarPorNombre();
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {}
}