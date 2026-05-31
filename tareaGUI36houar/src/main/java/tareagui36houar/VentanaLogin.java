package tareagui36houar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

public class VentanaLogin extends JFrame {

    private Set<Usuario> usuarios;
    private GestorUsuariosCsv gestorUsuariosCsv;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegistro;
    private JButton btnSalir;

    public VentanaLogin() {
        gestorUsuariosCsv = new GestorUsuariosCsv(Path.of("usuarios.csv"));
        usuarios = new LinkedHashSet<Usuario>();
        usuarios.addAll(gestorUsuariosCsv.cargarUsuarios());

        if (usuarios.isEmpty()) {
            cargarUsuariosInventados();
            gestorUsuariosCsv.guardarUsuarios(usuarios);
        }

        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JLabel lblUsuario = new JLabel();
        JLabel lblPassword = new JLabel();

        txtUsuario = new JTextField();
        txtPassword = new JPasswordField();
        btnLogin = new JButton();
        btnRegistro = new JButton();
        btnSalir = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login de usuarios");
        setResizable(false);

        lblUsuario.setText("Usuario");
        lblPassword.setText("Contrasena");

        btnLogin.setText("Login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comprobarLogin();
            }
        });

        btnRegistro.setText("Registro");
        btnRegistro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirDialogoRegistro();
            }
        });

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(lblPassword)
                                .addComponent(lblUsuario))
                            .addGap(37, 37, 37)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtUsuario)
                                .addComponent(txtPassword, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnLogin)
                            .addGap(37, 37, 37)
                            .addComponent(btnRegistro)
                            .addGap(37, 37, 37)
                            .addComponent(btnSalir)))
                    .addContainerGap(34, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblUsuario)
                        .addComponent(txtUsuario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblPassword)
                        .addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 24, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLogin)
                        .addComponent(btnRegistro)
                        .addComponent(btnSalir))
                    .addGap(25, 25, 25))
        );

        pack();
    }

    private void cargarUsuariosInventados() {
        usuarios.add(new Usuario("admin", "1234"));
        usuarios.add(new Usuario("ana", "claveana"));
        usuarios.add(new Usuario("luis", "java123"));
    }

    private void comprobarLogin() {
        String username = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());
        Usuario usuarioBuscado = new Usuario(username, password);
        boolean encontrado = false;

        // Si falta algun dato, se avisa al usuario
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes escribir usuario y contrasena.");
            return;
        }

        
        for (Usuario usuario : usuarios) {
            if (usuario.equals(usuarioBuscado) && usuario.getPassword().equals(password)) {
                encontrado = true;
            }
        }

        if (encontrado) {
            JOptionPane.showMessageDialog(this, "Has entrado con exito.");
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contrasena incorrectos.");
        }
    }

    private void abrirDialogoRegistro() {
        RegistroDialog dialog = new RegistroDialog(this, true);
        dialog.setVisible(true);
    }

    public void registrarOActualizarUsuario(String username, String password) {
        Usuario nuevoUsuario = new Usuario(username, password);
        boolean existe = false;

       
        for (Usuario usuario : usuarios) {
            if (usuario.equals(nuevoUsuario)) {
                usuario.setPassword(password);
                existe = true;
            }
        }

        if (!existe) {
            usuarios.add(nuevoUsuario);
        }

        gestorUsuariosCsv.guardarUsuarios(usuarios);
    }
}
