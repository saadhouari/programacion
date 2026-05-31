package views;

import controladores.ClienteController;
import controladores.VentaController;
import entidades.Cliente;
import entidades.Venta;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

class VentaDialog extends JDialog {

    private final VentaController ventaController = new VentaController();
    private final ClienteController clienteController = new ClienteController();
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Fecha", "Cliente"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable();
    private final JTextField idField = new JTextField();
    private final JTextField fechaField = new JTextField();
    private final JComboBox<ComboItem<Cliente>> clienteCombo = new JComboBox<>();

    VentaDialog(Frame owner) {
        super(owner, "Gestión de ventas", true);
        setSize(860, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        UiUtils.configureReadOnlyTable(table, model);
        table.getSelectionModel().addListSelectionListener(event -> loadSelected());

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(formPanel(), BorderLayout.EAST);
        add(buttonPanel(), BorderLayout.SOUTH);
        loadCombos();
        loadData();
        clearForm();
    }

    private JPanel formPanel() {
        idField.setEditable(false);
        JPanel panel = new JPanel(new GridLayout(6, 1, 6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Datos"));
        panel.add(new JLabel("ID"));
        panel.add(idField);
        panel.add(new JLabel("Fecha (" + UiUtils.DATE_PATTERN + ")"));
        panel.add(fechaField);
        panel.add(new JLabel("Cliente"));
        panel.add(clienteCombo);
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
        refrescar.addActionListener(event -> {
            loadCombos();
            loadData();
        });
        cerrar.addActionListener(event -> dispose());
        panel.add(nuevo);
        panel.add(guardar);
        panel.add(borrar);
        panel.add(refrescar);
        panel.add(cerrar);
        return panel;
    }

    private void loadCombos() {
        clienteCombo.removeAllItems();
        for (Cliente cliente : clienteController.findAll()) {
            clienteCombo.addItem(new ComboItem<>(cliente, cliente.getId() + " - " + cliente.getNombre() + " (" + cliente.getNif() + ")"));
        }
    }

    private void loadData() {
        model.setRowCount(0);
        for (Venta venta : ventaController.findAll()) {
            Cliente cliente = venta.getIdcliente();
            model.addRow(new Object[]{venta.getId(), UiUtils.formatDate(venta.getFecha()),
                cliente.getId() + " - " + cliente.getNombre()});
        }
    }

    private void loadSelected() {
        if (table.getSelectedRow() < 0) {
            return;
        }
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        Integer id = Integer.valueOf(model.getValueAt(row, 0).toString());
        Venta venta = ventaController.findById(id);
        idField.setText(id.toString());
        fechaField.setText(UiUtils.formatDate(venta.getFecha()));
        selectCliente(venta.getIdcliente().getId());
    }

    private void selectCliente(Integer id) {
        for (int i = 0; i < clienteCombo.getItemCount(); i++) {
            if (clienteCombo.getItemAt(i).getValue().getId().equals(id)) {
                clienteCombo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void clearForm() {
        table.clearSelection();
        idField.setText("");
        fechaField.setText(UiUtils.formatDate(new Date()));
        if (clienteCombo.getItemCount() > 0) {
            clienteCombo.setSelectedIndex(0);
        }
    }

    private void save() {
        try {
            ComboItem<Cliente> selected = (ComboItem<Cliente>) clienteCombo.getSelectedItem();
            if (selected == null) {
                throw new IllegalArgumentException("Primero crea al menos un cliente.");
            }
            Date fecha = UiUtils.parseDate(fechaField.getText());
            Cliente cliente = selected.getValue();
            if (idField.getText().isBlank()) {
                ventaController.create(new Venta(fecha, cliente));
            } else {
                Venta venta = ventaController.findById(Integer.valueOf(idField.getText()));
                venta.setFecha(fecha);
                venta.setIdcliente(cliente);
                ventaController.update(venta);
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
            UiUtils.showInfo(this, "Selecciona una venta para borrar.");
            return;
        }
        if (!UiUtils.confirm(this, "¿Borrar la venta? También se borrarán sus detalles.")) {
            return;
        }
        try {
            ventaController.delete(id);
            loadData();
            clearForm();
        } catch (Exception ex) {
            UiUtils.showError(this, ex);
        }
    }
}
