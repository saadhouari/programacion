package views;

import java.awt.Component;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

final class UiUtils {

    static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);

    private UiUtils() {
    }

    static void configureReadOnlyTable(JTable table, DefaultTableModel model) {
        table.setModel(model);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setReorderingAllowed(false);
    }

    static Integer selectedId(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            return null;
        }
        int modelRow = table.convertRowIndexToModel(row);
        Object value = table.getModel().getValueAt(modelRow, 0);
        return Integer.valueOf(value.toString());
    }

    static String required(String value, String fieldName) {
        String clean = value == null ? "" : value.trim();
        if (clean.isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " es obligatorio.");
        }
        return clean;
    }

    static int parseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(required(value, fieldName));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El campo " + fieldName + " debe ser un número entero.");
        }
    }

    static float parseFloat(String value, String fieldName) {
        try {
            return Float.parseFloat(required(value, fieldName).replace(',', '.'));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El campo " + fieldName + " debe ser un número decimal.");
        }
    }

    static Date parseDate(String value) {
        try {
            return DATE_FORMAT.parse(required(value, "fecha"));
        } catch (ParseException ex) {
            throw new IllegalArgumentException("La fecha debe tener el formato " + DATE_PATTERN + ".");
        }
    }

    static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMAT.format(date);
    }

    static boolean confirm(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }

    static void showError(Component parent, Exception ex) {
        String message = ex.getMessage();
        if (message == null && ex.getCause() != null) {
            message = ex.getCause().getMessage();
        }
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
