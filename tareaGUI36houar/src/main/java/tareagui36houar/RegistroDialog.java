package tareagui36houar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

public class RegistroDialog extends JDialog {

    private final VentanaLogin ventanaPadre;
    private JTextField txtNuevoUsuario;
    private JPasswordField txtNuevaPassword;
    private JPasswordField txtConfirmarPassword;
    private JCheckBox chkMostrarPasswords;
    private JButton btnRegistrar;
    private JButton btnCancelar;

    public RegistroDialog(VentanaLogin parent, boolean modal) {
        super(parent, modal);
        ventanaPadre = parent;
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        JLabel lblUsuario = new JLabel();
        JLabel lblPassword = new JLabel();
        JLabel lblConfirmacion = new JLabel();

        txtNuevoUsuario = new JTextField();
        txtNuevaPassword = new JPasswordField();
        txtConfirmarPassword = new JPasswordField();
        chkMostrarPasswords = new JCheckBox();
        btnRegistrar = new JButton();
        btnCancelar = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Registro");
        setResizable(false);

        lblUsuario.setText("Usuario");
        lblPassword.setText("Contrasena");
        lblConfirmacion.setText("Confirmacion Contrasena");

        chkMostrarPasswords.setText("Ver/Ocultar contrasenas");
        chkMostrarPasswords.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiarVisibilidadPasswords();
            }
        });

        btnRegistrar.setText("Registrar");
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(chkMostrarPasswords)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(lblConfirmacion)
                                .addComponent(lblPassword)
                                .addComponent(lblUsuario))
                            .addGap(34, 34, 34)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtNuevoUsuario)
                                .addComponent(txtNuevaPassword)
                                .addComponent(txtConfirmarPassword, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(208, 208, 208)
                            .addComponent(btnRegistrar)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnCancelar)))
                    .addContainerGap(34, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(31, 31, 31)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblUsuario)
                        .addComponent(txtNuevoUsuario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblPassword)
                        .addComponent(txtNuevaPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lblConfirmacion)
                        .addComponent(txtConfirmarPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addComponent(chkMostrarPasswords)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 18, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(btnRegistrar)
                        .addComponent(btnCancelar))
                    .addGap(26, 26, 26))
        );

        pack();
    }

    private void cambiarVisibilidadPasswords() {
        if (chkMostrarPasswords.isSelected()) {
            txtNuevaPassword.setEchoChar((char) 0);
            txtConfirmarPassword.setEchoChar((char) 0);
        } else {
            txtNuevaPassword.setEchoChar('*');
            txtConfirmarPassword.setEchoChar('*');
        }
    }

    private void registrarUsuario() {
        String username = txtNuevoUsuario.getText().trim();
        String password = new String(txtNuevaPassword.getPassword());
        String confirmacion = new String(txtConfirmarPassword.getPassword());

        // Comprobamos que todos los campos tengan datos
        if (username.isEmpty() || password.isEmpty() || confirmacion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes rellenar todos los campos.");
            return;
        }

        if (!password.equals(confirmacion)) {
            JOptionPane.showMessageDialog(this, "Las contrasenas no coinciden.");
            return;
        }

        ventanaPadre.registrarOActualizarUsuario(username, password);
        JOptionPane.showMessageDialog(this, "Usuario guardado correctamente.");
        dispose();
    }
}
