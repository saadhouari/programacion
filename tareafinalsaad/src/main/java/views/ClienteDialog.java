package views;

import controladores.ClienteController;
import entidades.Cliente;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

class ClienteDialog extends JDialog {

    private final ClienteController controller = new ClienteController();
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nombre", "NIF"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable();
    private final JTextField idField = new JTextField();
    private final JTextField nombreField = new JTextField();
    private final JTextField nifField = new JTextField();

    ClienteDialog(Frame owner) {
        super(owner, "Gestión de clientes", true);
        setSize(760, 460);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        UiUtils.configureReadOnlyTable(table, model);
        table.getSelectionModel().addListSelectionListener(event -> loadSelected());

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(formPanel(), BorderLayout.EAST);
        add(buttonPanel(), BorderLayout.SOUTH);
        loadData();
    }

    private JPanel formPanel() {
        idField.setEditable(false);
        JPanel panel = new JPanel(new GridLayout(6, 1, 6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Datos"));
        panel.add(new JLabel("ID"));
        panel.add(idField);
        panel.add(new JLabel("Nombre"));
        panel.add(nombreField);
        panel.add(new JLabel("NIF"));
        panel.add(nifField);
        return panel;
    }

    private JPanel buttonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton nuevo = new JButton("Nuevo");
        JButton guardar = new JButton("Guardar");
        JButton borrar = new JButton("Borrar");
        JButton refrescar = new JButton("Refrescar");
        JButton cerrar = new JButton("Cerrar");
        nuevo.addActionListener(event -> clearForm());
        guardar.addActionListener(event -> save());
        borrar.addActionListener(event -> delete());
        refrescar.addActionListener(event -> loadData());
        cerrar.addActionListener(event -> dispose());
        panel.add(nuevo);
        panel.add(guardar);
        panel.add(borrar);
        panel.add(refrescar);
        panel.add(cerrar);
        return panel;
    }

    private void loadData() {
        model.setRowCount(0);
        for (Cliente cliente : controller.findAll()) {
            model.addRow(new Object[]{cliente.getId(), cliente.getNombre(), cliente.getNif()});
        }
    }

    private void loadSelected() {
        if (table.getSelectedRow() < 0) {
            return;
        }
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        idField.setText(model.getValueAt(row, 0).toString());
        nombreField.setText(String.valueOf(model.getValueAt(row, 1)));
        nifField.setText(String.valueOf(model.getValueAt(row, 2)));
    }

    private void clearForm() {
        table.clearSelection();
        idField.setText("");
        nombreField.setText("");
        nifField.setText("");
    }

    private void save() {
        try {
            String nombre = UiUtils.required(nombreField.getText(), "nombre");
            String nif = UiUtils.required(nifField.getText(), "NIF");
            if (idField.getText().isBlank()) {
                controller.create(new Cliente(nombre, nif));
            } else {
                Cliente cliente = controller.findById(Integer.valueOf(idField.getText()));
                cliente.setNombre(nombre);
                cliente.setNif(nif);
                controller.update(cliente);
            }
            loadData();
            clearForm();
        } catch (Exception ex) {
            UiUtils.showError(this, ex);
        }
    }

    private void delete() {
        Integer id = UiUtils.selectedId(table);
        if (id == null) {
            UiUtils.showInfo(this, "Selecciona un cliente para borrar.");
            return;
        }
        if (!UiUtils.confirm(this, "¿Borrar el cliente seleccionado? Si tiene ventas asociadas no se permitirá.")) {
            return;
        }
        try {
            controller.delete(id);
            loadData();
            clearForm();
        } catch (Exception ex) {
            UiUtils.showError(this, new RuntimeException("No se puede borrar: revisa ventas asociadas.", ex));
        }
    }
}
