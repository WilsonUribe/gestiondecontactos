package controlador;

import vista.Ventana;
import modelo.*;

import java.awt.event.*;
import java.util.List;

import javax.swing.JOptionPane;

/*
 * Controlador principal
 */
public class LogicaContactos implements ActionListener, KeyListener {

    private Ventana vista;
    private PersonaDAO dao;

    public LogicaContactos() {

        vista = new Ventana();
        dao = new PersonaDAO();

        // Manejo de botones y busqueda
        vista.btnAgregar.addActionListener(this);
        vista.btnEliminar.addActionListener(this);
        vista.btnExportar.addActionListener(this);
        vista.txtBuscar.addKeyListener(this);

        cargarTabla();

        vista.setVisible(true);
    }
    
 // Main
    public static void main(String[] args) {

        new LogicaContactos();
    }

    // Carga del JTable
    public void cargarTabla() {

        new Thread(() -> {

            vista.modelo.setRowCount(0);
            vista.barra.setValue(0);

            List<Persona> lista = dao.listar();

            if (lista.size() == 0) {
                vista.barra.setValue(100);
                return;
            }

            int total = lista.size();
            int contador = 0;

            for (Persona p : lista) {

                vista.modelo.addRow(p.formatoTabla());

                contador++;

                int progreso = (contador * 100) / total;

                vista.barra.setValue(progreso);

                try {
                    Thread.sleep(100);
                } catch (Exception e) { }
            }

        }).start();
    }

    public void buscarPorNombre() {

        String texto =
                vista.txtBuscar.getText().toLowerCase();

        vista.modelo.setRowCount(0);

        List<Persona> lista = dao.listar();

        for (Persona p : lista) {

            if (p.getNombre().toLowerCase()
                    .contains(texto)) {

                vista.modelo.addRow(
                        p.formatoTabla()
                );
            }
        }
    }

    // Eventos botones
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.btnAgregar) {

            Persona p = new Persona(
                    vista.txtNombre.getText(),
                    vista.txtTelefono.getText(),
                    vista.txtEmail.getText(),
                    vista.cmbCategoria.getSelectedItem().toString(),
                    vista.chkFavorito.isSelected()
            );

            dao.guardar(p);

            limpiar();

            cargarTabla();

            JOptionPane.showMessageDialog(
                    null,
                    "Contacto agregado"
            );
        }

        if (e.getSource() == vista.btnEliminar) {

            int fila =
                    vista.tabla.getSelectedRow();

            if (fila == -1) {

                JOptionPane.showMessageDialog(
                        null,
                        "Seleccione un contacto"
                );

                return;
            }

            List<Persona> lista = dao.listar();

            lista.remove(fila);

            dao.actualizarArchivo(lista);

            cargarTabla();

            JOptionPane.showMessageDialog(
                    null,
                    "Contacto eliminado"
            );
        }

        // CSV
        if (e.getSource() == vista.btnExportar) {

            new Thread(() -> {

                vista.barra.setValue(0);

                for (int i = 0; i <= 100; i += 10) {

                    vista.barra.setValue(i);

                    try {
                        Thread.sleep(80);
                    } catch (Exception ex) { }
                }

                dao.exportarCSV(dao.listar());

                JOptionPane.showMessageDialog(
                        null,
                        "Archivo contactos.csv exportado"
                );

            }).start();
        }
    }

    // Limpiar casillas
    public void limpiar() {

        vista.txtNombre.setText("");
        vista.txtTelefono.setText("");
        vista.txtEmail.setText("");
        vista.chkFavorito.setSelected(false);
        vista.cmbCategoria.setSelectedIndex(0);
    }

    // Evento de búsqueda
    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getSource() == vista.txtBuscar) {
            buscarPorNombre();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) { }

}