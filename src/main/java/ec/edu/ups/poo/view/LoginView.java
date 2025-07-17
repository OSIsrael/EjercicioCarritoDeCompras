package ec.edu.ups.poo.view;

import ec.edu.ups.poo.util.Idioma;

import javax.swing.*; 
import java.io.File;

public class LoginView extends JFrame{
    private JPanel panelPrincipal;
    private JPanel panelSecundario;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnIniciarSesion;
    private JButton btnRegistrarse;
    private JLabel lblUsuario;
    private JLabel lblContrasena;
    private JButton btnOlvide;
    private JButton btnSeleccionarRuta;
    private JTextField txtRuta;
    private JComboBox<String> cbxTipoStorage;
    private JLabel lblTipoStorage;
    private JLabel lblRuta;

    private JMenuBar menuBar;
    private JMenu menuIdiomas;
    private JMenuItem menuItemEspañol;
    private JMenuItem menuItemIngles;
    private JMenuItem menuItemFrances;

    public LoginView(){
        setContentPane(panelPrincipal);
        setTitle("Iniciar Sesion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700,700);
        setLocationRelativeTo(null);
        btnOlvide.setIcon(new ImageIcon(getClass().getResource("/icons/pregunta.png")));
        btnRegistrarse.setIcon(new ImageIcon(getClass().getResource("/icons/usuario.png")));
        btnIniciarSesion.setIcon(new ImageIcon(getClass().getResource("/icons/entrar.png")));

        // --- CAMBIO DE DIAGNÓSTICO ---
        // El siguiente bloque de código ayuda a identificar el problema exacto.
        // Si cbxTipoStorage es null, significa que el componente en la VISTA DE DISEÑO
        // no está correctamente vinculado al campo en el CÓDIGO.
        if (cbxTipoStorage == null) {
            // Este error te dice exactamente qué hacer.
            throw new IllegalStateException("Error de Vinculación: El componente JComboBox 'cbxTipoStorage' no está conectado. \n\nSOLUCIÓN: \n1. Abre LoginView.java en la VISTA DE DISEÑO (Design). \n2. Haz clic en el JComboBox. \n3. En el panel de Propiedades, busca 'fieldName' y escribe 'cbxTipoStorage'.");
        }

        // Esta lógica solo se ejecuta si la vinculación es correcta.
        cbxTipoStorage.setModel(new DefaultComboBoxModel<>(new String[]{"MEMORIA", "TEXTO", "BINARIO"}));
        // Añadir listeners para la selección de almacenamiento
        cbxTipoStorage.addActionListener(e -> actualizarVisibilidadRuta());
        btnSeleccionarRuta.addActionListener(e -> seleccionarRuta());

        crearMenu(); // Debe llamarse antes de actualizar textos
        actualizarTextos();
        actualizarVisibilidadRuta(); // Establecer el estado inicial correcto
    }
    private void crearMenu() {
        menuBar = new JMenuBar();
        menuIdiomas = new JMenu();
        menuItemEspañol = new JMenuItem();
        menuItemIngles = new JMenuItem();
        menuItemFrances = new JMenuItem();

        menuIdiomas.add(menuItemEspañol);
        menuIdiomas.add(menuItemIngles);
        menuIdiomas.add(menuItemFrances);
        menuBar.add(menuIdiomas);
        setJMenuBar(menuBar);
    }
    public void actualizarTextos() {
        setTitle(Idioma.get("login.titulo"));
        lblUsuario.setText(Idioma.get("login.lbl.usuario"));
        lblContrasena.setText(Idioma.get("login.lbl.contrasena"));
        btnIniciarSesion.setText(Idioma.get("login.btn.iniciar"));
        btnRegistrarse.setText(Idioma.get("login.btn.registrar"));
        btnOlvide.setText(Idioma.get("login.btn.olvide"));
        lblTipoStorage.setText(Idioma.get("login.lbl.storage")); // Clave a añadir en properties
        lblRuta.setText(Idioma.get("login.lbl.ruta"));           // Clave a añadir en properties

        menuIdiomas.setText(Idioma.get("menu.idiomas"));
        menuItemEspañol.setText(Idioma.get("menu.idiomas.español"));
        menuItemIngles.setText(Idioma.get("menu.idiomas.ingles"));
        menuItemFrances.setText(Idioma.get("menu.idiomas.frances"));
    }

    private void actualizarVisibilidadRuta() {
        String seleccion = (String) cbxTipoStorage.getSelectedItem();
        boolean visible = "TEXTO".equals(seleccion) || "BINARIO".equals(seleccion);

        lblRuta.setVisible(visible);
        txtRuta.setVisible(visible);
        btnSeleccionarRuta.setVisible(visible);
    }

    private void seleccionarRuta() {
        JFileChooser fileChooser = new JFileChooser();
        // Configurar para que solo se puedan seleccionar directorios
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File directorioSeleccionado = fileChooser.getSelectedFile();
            txtRuta.setText(directorioSeleccionado.getAbsolutePath());
        }
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JPanel getPanelSecundario() {
        return panelSecundario;
    }

    public void setPanelSecundario(JPanel panelSecundario) {
        this.panelSecundario = panelSecundario;
    }

    public JTextField getTxtUsername() {
        return txtUsername;
    }

    public void setTxtUsername(JTextField txtUsername) {
        this.txtUsername = txtUsername;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public void setTxtPassword(JPasswordField txtPassword) {
        this.txtPassword = txtPassword;
    }

    public JButton getBtnIniciarSesion() {
        return btnIniciarSesion;
    }

    public void setBtnIniciarSesion(JButton btnIniciarSesion) {
        this.btnIniciarSesion = btnIniciarSesion;
    }

    public JButton getBtnRegistrarse() {
        return btnRegistrarse;
    }

    public void setBtnRegistrarse(JButton btnRegistrarse) {
        this.btnRegistrarse = btnRegistrarse;
    }

    public JButton getBtnOlvide() {
        return btnOlvide;
    }

    public void setBtnOlvide(JButton btnOlvide) {
        this.btnOlvide = btnOlvide;
    }

    public JMenuItem getMenuItemEspañol() {
        return menuItemEspañol;
    }

    public void setMenuItemEspañol(JMenuItem menuItemEspañol) {
        this.menuItemEspañol = menuItemEspañol;
    }

    public JMenuItem getMenuItemIngles() {
        return menuItemIngles;
    }

    public void setMenuItemIngles(JMenuItem menuItemIngles) {
        this.menuItemIngles = menuItemIngles;
    }

    public JMenuItem getMenuItemFrances() {
        return menuItemFrances;
    }

    public void setMenuItemFrances(JMenuItem menuItemFrances) {
        this.menuItemFrances = menuItemFrances;
    }

    public void mostrar (String mensaje){
        JOptionPane.showMessageDialog(this, Idioma.get(mensaje));
    }

    // --- GETTERS PARA LOS NUEVOS COMPONENTES ---

    public JComboBox<String> getCbxTipoStorage() {
        return cbxTipoStorage;
    }

    public JTextField getTxtRuta() {
        return txtRuta;
    }

    public JButton getBtnSeleccionarRuta() {
        return btnSeleccionarRuta;
    }

    public void setCbxTipoStorage(JComboBox<String> cbxTipoStorage) {
        this.cbxTipoStorage = cbxTipoStorage;
    }

    public JLabel getLblTipoStorage() {
        return lblTipoStorage;
    }

    public JLabel getLblRuta() {
        return lblRuta;
    }

    public void setLblRuta(JLabel lblRuta) {
        this.lblRuta = lblRuta;
    }

    public void setLblTipoStorage(JLabel lblTipoStorage) {
        this.lblTipoStorage = lblTipoStorage;
    }
}