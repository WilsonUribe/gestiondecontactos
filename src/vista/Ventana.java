package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.SystemColor;

/*
 * Interfaz gráfica
 */
public class Ventana extends JFrame {

    public JTextField txtNombre, txtTelefono, txtEmail, txtBuscar;
    public JButton btnAgregar, btnEliminar, btnExportar;
    public JCheckBox chkFavorito;
    public JComboBox<String> cmbCategoria;
    public JTable tabla;
    public DefaultTableModel modelo;
    public JProgressBar barra;

    public Ventana() {
    	getContentPane().setBackground(new Color(64, 0, 0));

        setTitle("GESTIÓN DE CONTACTOS");
        setSize(920, 650);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Nombre
        JLabel l1 = new JLabel("NOMBRES:");
        l1.setForeground(new Color(255, 255, 255));
        l1.setBounds(30, 30, 100, 25);
        getContentPane().add(l1);

        txtNombre = new JTextField();
        txtNombre.setBounds(130, 30, 300, 25);
        getContentPane().add(txtNombre);

        // Teléfono
        JLabel l2 = new JLabel("TELÉFONO:");
        l2.setForeground(new Color(255, 255, 255));
        l2.setBounds(30, 70, 100, 25);
        getContentPane().add(l2);

        txtTelefono = new JTextField();
        txtTelefono.setBounds(130, 70, 300, 25);
        getContentPane().add(txtTelefono);

        // Email
        JLabel l3 = new JLabel("CORREO:");
        l3.setForeground(new Color(255, 255, 255));
        l3.setBounds(30, 110, 100, 25);
        getContentPane().add(l3);

        txtEmail = new JTextField();
        txtEmail.setBounds(130, 110, 300, 25);
        getContentPane().add(txtEmail);

        // Favorito
        chkFavorito = new JCheckBox("CONTACTOS FAVORITOS");
        chkFavorito.setBounds(30, 150, 192, 25);
        getContentPane().add(chkFavorito);

        cmbCategoria = new JComboBox<>();
        cmbCategoria.setBounds(492, 150, 378, 25);
        cmbCategoria.addItem("Familia");
        cmbCategoria.addItem("Trabajo");
        cmbCategoria.addItem("Amigos");
        cmbCategoria.addItem("Otros");
        getContentPane().add(cmbCategoria);

        btnAgregar = new JButton("AGREGAR");
        btnAgregar.setForeground(new Color(255, 255, 255));
        btnAgregar.setBackground(new Color(50, 205, 50));
        btnAgregar.setBounds(489, 30, 100, 40);
        getContentPane().add(btnAgregar);

        btnEliminar = new JButton("ELIMINAR");
        btnEliminar.setForeground(new Color(255, 255, 255));
        btnEliminar.setBackground(new Color(255, 0, 0));
        btnEliminar.setBounds(751, 30, 119, 40);
        getContentPane().add(btnEliminar);

        btnExportar = new JButton("EXPORTAR CSV");
        btnExportar.setForeground(new Color(255, 255, 255));
        btnExportar.setBackground(SystemColor.textHighlight);
        btnExportar.setBounds(599, 30, 140, 40);
        getContentPane().add(btnExportar);

        // Tabla
        modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");
        modelo.addColumn("Telefono");
        modelo.addColumn("Correo");
        modelo.addColumn("Categoria");
        modelo.addColumn("Favorito");

        tabla = new JTable(modelo);

        JScrollPane sp = new JScrollPane(tabla);
        sp.setBounds(30, 220, 840, 260);
        getContentPane().add(sp);

        JLabel l4 = new JLabel("BUSCAR POR NOMBRE:");
        l4.setBounds(30, 500, 170, 25);
        getContentPane().add(l4);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(190, 500, 300, 25);
        getContentPane().add(txtBuscar);

        // Barra progreso
        barra = new JProgressBar();
        barra.setForeground(new Color(128, 255, 128));
        barra.setBounds(30, 550, 840, 25);
        barra.setStringPainted(true);
        getContentPane().add(barra);
    }
}