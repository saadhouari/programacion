package views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("Tarea final Saad - Gestión de ventas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(720, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        JLabel title = new JLabel("Gestión de base de datos: empresa_ventas", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setBorder(BorderFactory.createEmptyBorder(18, 18, 8, 18));
        add(title, BorderLayout.NORTH);

        JPanel actions = new JPanel(new GridLayout(3, 2, 14, 14));
        actions.setBorder(BorderFactory.createEmptyBorder(18, 80, 18, 80));
        actions.add(button("Clientes", () -> new ClienteDialog(this).setVisible(true)));
        actions.add(button("Productos", () -> new ProductoDialog(this).setVisible(true)));
        actions.add(button("Ventas", () -> new VentaDialog(this).setVisible(true)));
        actions.add(button("Detalles de venta", () -> new DetalleVentaDialog(this).setVisible(true)));
        actions.add(button("Copias de seguridad", () -> new BackupDialog(this).setVisible(true)));
        actions.add(button("Salir", this::dispose));
        add(actions, BorderLayout.CENTER);

        JLabel footer = new JLabel("CRUD con JPA + Swing, respetando claves foráneas y restauración por CSV.",
                SwingConstants.CENTER);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 18, 18, 18));
        add(footer, BorderLayout.SOUTH);
    }

    private JButton button(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(button.getFont().deriveFont(Font.PLAIN, 16f));
        button.addActionListener(event -> action.run());
        return button;
    }
}
