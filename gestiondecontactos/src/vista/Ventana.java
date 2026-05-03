package vista;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;

public class Ventana extends JFrame {

    public JTextField txtNombre, txtTelefono, txtEmail, txtBuscar;
    public JButton btnAgregar, btnEliminar, btnExportar, btnModificar, btnEstadisticas;
    public JCheckBox chkFavorito;
    public JComboBox<String> cmbCategoria;
    public JTable tabla;
    public DefaultTableModel modelo;
    public JProgressBar barra;

    private JLabel lNombre, lTelefono, lEmail, lBuscar, lblIdioma;
    private JComboBox<String> cmbIdioma;
    private Properties props = new Properties();

    public Ventana() {

        getContentPane().setBackground(new Color(51, 0, 0));
        setSize(920, 650);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Español como idioma por defecto
        cargarIdioma("Es");

        // Selector de idiomas
        lblIdioma = new JLabel("Idioma:");
        lblIdioma.setForeground(new Color(255, 255, 255));
        lblIdioma.setBounds(710, 30, 60, 25);
        getContentPane().add(lblIdioma);

        cmbIdioma = new JComboBox<String>();
        cmbIdioma.addItem("Español");
        cmbIdioma.addItem("English");
        cmbIdioma.addItem("Français");
        cmbIdioma.setBounds(760, 30, 110, 25);
        cmbIdioma.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idx = cmbIdioma.getSelectedIndex();
                if (idx == 0)      cargarIdioma("Es");
                else if (idx == 1) cargarIdioma("En");
                else               cargarIdioma("Fr");
                actualizarTextos();
            }
        });
        getContentPane().add(cmbIdioma);

        lNombre = new JLabel("NOMBRES:");
        lNombre.setForeground(new Color(255, 255, 255));
        lNombre.setBounds(30, 30, 120, 25);
        getContentPane().add(lNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(130, 30, 300, 25);
        getContentPane().add(txtNombre);

        lTelefono = new JLabel("TELEFONO:");
        lTelefono.setBackground(new Color(255, 255, 255));
        lTelefono.setForeground(new Color(255, 255, 255));
        lTelefono.setBounds(30, 70, 120, 25);
        getContentPane().add(lTelefono);

        txtTelefono = new JTextField();
        txtTelefono.setBounds(130, 70, 300, 25);
        getContentPane().add(txtTelefono);

        lEmail = new JLabel("CORREO:");
        lEmail.setForeground(new Color(255, 255, 255));
        lEmail.setBounds(30, 110, 120, 25);
        getContentPane().add(lEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(130, 110, 300, 25);
        getContentPane().add(txtEmail);

        // Iconos
        JLabel lblIconoFav = new JLabel();
        lblIconoFav.setBounds(30, 150, 20, 25);
        try {
            ImageIcon iconoFav = new ImageIcon("iconos/favorito.png");
            Image imgFav = iconoFav.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            lblIconoFav.setIcon(new ImageIcon(imgFav));
        } catch (Exception e) {}
        getContentPane().add(lblIconoFav);

        chkFavorito = new JCheckBox("FAVORITOS");
        chkFavorito.setBounds(52, 150, 110, 25);
        chkFavorito.setBackground(Color.WHITE);
        getContentPane().add(chkFavorito);

        cmbCategoria = new JComboBox<String>();
        cmbCategoria.addItem("Familia");
        cmbCategoria.addItem("Trabajo");
        cmbCategoria.addItem("Amigos");
        cmbCategoria.addItem("Otros");
        cmbCategoria.setBounds(170, 150, 260, 25);
        getContentPane().add(cmbCategoria);

        btnAgregar = new JButton();
        btnAgregar.setBounds(506, 30, 68, 65);
        btnAgregar.setBackground(Color.WHITE);
        btnAgregar.setToolTipText("Agregar contacto");
        try {
            ImageIcon icono = new ImageIcon("iconos/agregar.png");
            Image img = icono.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            btnAgregar.setIcon(new ImageIcon(img));
        } catch (Exception e) {}
        getContentPane().add(btnAgregar);

        btnEliminar = new JButton();
        btnEliminar.setBounds(590, 30, 68, 65);
        btnEliminar.setBackground(Color.WHITE);
        btnEliminar.setToolTipText("Eliminar contacto");
        try {
            ImageIcon icono = new ImageIcon("iconos/eliminar.png");
            Image img = icono.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            btnEliminar.setIcon(new ImageIcon(img));
        } catch (Exception e) {}
        getContentPane().add(btnEliminar);

        btnModificar = new JButton();
        btnModificar.setBounds(506, 110, 68, 65);
        btnModificar.setBackground(Color.WHITE);
        btnModificar.setToolTipText("Modificar contacto");
        try {
            ImageIcon icono = new ImageIcon("iconos/modificar.png");
            Image img = icono.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            btnModificar.setIcon(new ImageIcon(img));
        } catch (Exception e) {}
        getContentPane().add(btnModificar);

        btnExportar = new JButton();
        btnExportar.setBounds(668, 110, 68, 65);
        btnExportar.setBackground(Color.WHITE);
        btnExportar.setToolTipText("Exportar contactos");
        try {
            ImageIcon icono = new ImageIcon("iconos/exportar.png");
            Image img = icono.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            btnExportar.setIcon(new ImageIcon(img));
        } catch (Exception e) {}
        getContentPane().add(btnExportar);

        btnEstadisticas = new JButton();
        btnEstadisticas.setBounds(590, 110, 68, 65);
        btnEstadisticas.setBackground(Color.WHITE);
        btnEstadisticas.setToolTipText("Ver estadisticas");
        try {
            ImageIcon icono = new ImageIcon("iconos/estadisticas.png");
            Image img = icono.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            btnEstadisticas.setIcon(new ImageIcon(img));
        } catch (Exception e) {}
        getContentPane().add(btnEstadisticas);

        // Tabla
        modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");
        modelo.addColumn("Telefono");
        modelo.addColumn("Correo");
        modelo.addColumn("Categoria");
        modelo.addColumn("Favorito");

        tabla = new JTable(modelo);
        JScrollPane sp = new JScrollPane(tabla);
        sp.setBounds(30, 266, 840, 260);
        getContentPane().add(sp);

        // --- ICONO BUSCAR (decorativo) ---
        JLabel lblIconoBuscar = new JLabel();
        lblIconoBuscar.setBounds(30, 208, 20, 25);
        try {
            ImageIcon iconoBuscar = new ImageIcon("iconos/buscar.png");
            Image imgBuscar = iconoBuscar.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            lblIconoBuscar.setIcon(new ImageIcon(imgBuscar));
        } catch (Exception e) {}
        getContentPane().add(lblIconoBuscar);

        lBuscar = new JLabel("BUSCAR:");
        lBuscar.setForeground(new Color(255, 255, 255));
        lBuscar.setBounds(52, 208, 80, 25);
        getContentPane().add(lBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(130, 208, 731, 25);
        getContentPane().add(txtBuscar);

        barra = new JProgressBar();
        barra.setForeground(new Color(0, 102, 0));
        barra.setBounds(30, 550, 840, 25);
        barra.setStringPainted(true);
        getContentPane().add(barra);

        setTitle("GESTION DE CONTACTOS");
    }

    private void cargarIdioma(String codigo) {
        props = new Properties();
        String ruta = "/Internacionalización/" + codigo + ".properties";
        try (InputStream is = getClass().getResourceAsStream(ruta)) {
            if (is != null) {
                props.load(new InputStreamReader(is, "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarTextos() {
        setTitle(props.getProperty("titulo", "GESTION DE CONTACTOS"));
        lNombre.setText(props.getProperty("label.nombre", "NOMBRES:"));
        lTelefono.setText(props.getProperty("label.telefono", "TELEFONO:"));
        lEmail.setText(props.getProperty("label.correo", "CORREO:"));
        lBuscar.setText(props.getProperty("label.buscar", "BUSCAR:"));
        lblIdioma.setText(props.getProperty("idioma.label", "Idioma:"));
        chkFavorito.setText(props.getProperty("label.favorito", "FAVORITOS"));

        btnAgregar.setToolTipText(props.getProperty("btn.agregar"));
        btnEliminar.setToolTipText(props.getProperty("btn.eliminar"));
        btnModificar.setToolTipText(props.getProperty("btn.modificar"));
        btnExportar.setToolTipText(props.getProperty("btn.exportar"));
        btnEstadisticas.setToolTipText(props.getProperty("btn.estadisticas"));

        int seleccion = cmbCategoria.getSelectedIndex();
        cmbCategoria.removeAllItems();
        cmbCategoria.addItem(props.getProperty("combo.familia", "Familia"));
        cmbCategoria.addItem(props.getProperty("combo.trabajo", "Trabajo"));
        cmbCategoria.addItem(props.getProperty("combo.amigos", "Amigos"));
        cmbCategoria.addItem(props.getProperty("combo.otros", "Otros"));
        if (seleccion >= 0) cmbCategoria.setSelectedIndex(seleccion);

        modelo.setColumnIdentifiers(new String[]{
            props.getProperty("col.nombre", "Nombre"),
            props.getProperty("col.telefono", "Telefono"),
            props.getProperty("col.correo", "Correo"),
            props.getProperty("col.categoria", "Categoria"),
            props.getProperty("col.favorito", "Favorito")
        });

        revalidate();
        repaint();
    }

    public Properties getProps() {
        return props;
    }
}