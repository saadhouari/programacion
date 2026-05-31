package views;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.nio.file.Path;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import servicios.BackupService;

class BackupDialog extends JDialog {

    private final BackupService backupService = new BackupService();
    private final JTextArea logArea = new JTextArea();

    BackupDialog(Frame owner) {
        super(owner, "Copias de seguridad", true);
        setSize(720, 360);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        add(new JScrollPane(logArea), BorderLayout.CENTER);
        add(buttonPanel(), BorderLayout.SOUTH);
        showLastBackup();
    }

    private JPanel buttonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton crear = new JButton("Crear copia");
        JButton restaurar = new JButton("Restaurar última copia");
        JButton cerrar = new JButton("Cerrar");
        crear.addActionListener(event -> createBackup());
        restaurar.addActionListener(event -> restoreBackup());
        cerrar.addActionListener(event -> dispose());
        panel.add(crear);
        panel.add(restaurar);
        panel.add(cerrar);
        return panel;
    }

    private void showLastBackup() {
        try {
            String last = backupService.ultimaCopia()
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .orElse("Aún no existe ninguna copia.");
            log("Última copia: " + last);
        } catch (Exception ex) {
            log("No se pudo revisar la última copia: " + ex.getMessage());
        }
    }

    private void createBackup() {
        try {
            Path folder = backupService.crearCopiaSeguridad();
            log("Copia creada en: " + folder);
            UiUtils.showInfo(this, "Copia creada correctamente.");
        } catch (Exception ex) {
            UiUtils.showError(this, ex);
            log("Error creando copia: " + ex.getMessage());
        }
    }

    private void restoreBackup() {
        if (!UiUtils.confirm(this, "Se borrarán todas las tablas y se restaurará la última copia. ¿Continuar?")) {
            return;
        }
        try {
            Path folder = backupService.restaurarUltimaCopia();
            log("Restaurada la copia: " + folder);
            UiUtils.showInfo(this, "Restauración completada correctamente.");
        } catch (Exception ex) {
            UiUtils.showError(this, ex);
            log("Error restaurando copia: " + ex.getMessage());
        }
    }

    private void log(String message) {
        logArea.append(message + System.lineSeparator());
    }
}
