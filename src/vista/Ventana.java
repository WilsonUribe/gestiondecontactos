package vista;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;

/*
 * Interfaz gráfica
 */
public class Ventana extends JFrame {

    public JTextField txtNombre, txtTelefono, txtEmail;
    public JButton btnAgregar, btnEliminar, btnExportar;
    public JCheckBox chkFavorito;
    public JComboBox<String> cmbCategoria;
    public JTable tabla;
    public DefaultTableModel modelo;
    public JProgressBar barra;

    public Ventana() {
    	getContentPane().setBackground(new Color(0, 64, 64));

        setTitle("GESTIÓN DE CONTACTOS");
        setSize(900, 620);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel l1 = new JLabel("NOMBRES:");
        l1.setForeground(new Color(255, 255, 255));
        l1.setBounds(30, 30, 100, 25);
        getContentPane().add(l1);

        txtNombre = new JTextField();
        txtNombre.setBounds(130, 30, 300, 25);
        getContentPane().add(txtNombre);

        JLabel l2 = new JLabel("TELÉFONO:");
        l2.setForeground(new Color(255, 255, 255));
        l2.setBounds(30, 70, 100, 25);
        getContentPane().add(l2);

        txtTelefono = new JTextField();
        txtTelefono.setBounds(130, 70, 300, 25);
        getContentPane().add(txtTelefono);

        JLabel l3 = new JLabel("CORREO:");
        l3.setForeground(new Color(255, 255, 255));
        l3.setBounds(30, 110, 100, 25);
        getContentPane().add(l3);

        txtEmail = new JTextField();
        txtEmail.setBounds(130, 110, 300, 25);
        getContentPane().add(txtEmail);

        chkFavorito = new JCheckBox("CONTACTOS FAVORITOS");
        chkFavorito.setBounds(30, 150, 179, 25);
        getContentPane().add(chkFavorito);

        cmbCategoria = new JComboBox<>();
        cmbCategoria.setBounds(528, 150, 346, 25);
        cmbCategoria.addItem("Elige una categoria");
        cmbCategoria.addItem("Familia");
        cmbCategoria.addItem("Trabajo");
        cmbCategoria.addItem("Amigos");
        cmbCategoria.addItem("Otros");
        getContentPane().add(cmbCategoria);

        btnAgregar = new JButton("AGREGAR");
        btnAgregar.setForeground(new Color(255, 255, 255));
        btnAgregar.setBackground(new Color(0, 128, 0));
        btnAgregar.setBounds(528, 30, 100, 40);
        getContentPane().add(btnAgregar);

        btnEliminar = new JButton("ELIMINAR");
        btnEliminar.setBackground(new Color(128, 64, 64));
        btnEliminar.setForeground(new Color(255, 255, 255));
        btnEliminar.setBounds(774, 30, 100, 40);
        getContentPane().add(btnEliminar);

        // Botón exportar
        btnExportar = new JButton("EXPORTAR CSV");
        btnExportar.setBounds(638, 30, 126, 40);
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
        sp.setBounds(30, 220, 820, 280);
        getContentPane().add(sp);

        // Barra progreso
        barra = new JProgressBar();
        barra.setForeground(new Color(0, 128, 0));
        barra.setBounds(30, 530, 820, 25);
        barra.setStringPainted(true);
        getContentPane().add(barra);
    }
}
