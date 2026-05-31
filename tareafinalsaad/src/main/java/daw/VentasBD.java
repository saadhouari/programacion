package daw;

import javax.swing.SwingUtilities;
import views.MainFrame;

public class VentasBD {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
