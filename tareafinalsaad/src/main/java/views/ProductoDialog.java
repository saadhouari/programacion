package views;

import controladores.ProductoController;
import entidades.Producto;
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

class ProductoDialog extends JDialog {

    private final ProductoController controller = new ProductoController();
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Descripción", "Precio", "Stock"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable();
    private final JTextField idField = new JTextField();
    private final JTextField descripcionField = new JTextField();
    private final JTextField precioField = new JTextField();
    private final JTextField stockField = new JTextField();

    ProductoDialog(Frame owner) {
        super(owner, "Gestión de productos", true);
        setSize(820, 480);
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
        JPanel panel = new JPanel(new GridLayout(8, 1, 6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Datos"));
        panel.add(new JLabel("ID"));
        panel.add(idField);
        panel.add(new JLabel("Descripción"));
        panel.add(descripcionField);
        panel.add(new JLabel("Precio"));
        panel.add(precioField);
        panel.add(new JLabel("Stock"));
        panel.add(stockField);
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
        for (Producto producto : controller.findAll()) {
            model.addRow(new Object[]{producto.getId(), producto.getDescripcion(), producto.getPrecio(), producto.getStock()});
        }
    }

    private void loadSelected() {
        if (table.getSelectedRow() < 0) {
            return;
        }
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        idField.setText(model.getValueAt(row, 0).toString());
        descripcionField.setText(String.valueOf(model.getValueAt(row, 1)));
        precioField.setText(String.valueOf(model.getValueAt(row, 2)));
        stockField.setText(String.valueOf(model.getValueAt(row, 3)));
    }

    private void clearForm() {
        table.clearSelection();
        idField.setText("");
        descripcionField.setText("");
        precioField.setText("");
        stockField.setText("");
    }

    private void save() {
        try {
            String descripcion = UiUtils.required(descripcionField.getText(), "descripción");
            float precio = UiUtils.parseFloat(precioField.getText(), "precio");
            int stock = UiUtils.parseInt(stockField.getText(), "stock");
            if (idField.getText().isBlank()) {
                controller.create(new Producto(descripcion, precio, stock));
            } else {
                Producto producto = controller.findById(Integer.valueOf(idField.getText()));
                producto.setDescripcion(descripcion);
                producto.setPrecio(precio);
                producto.setStock(stock);
                controller.update(producto);
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
            UiUtils.showInfo(this, "Selecciona un producto para borrar.");
            return;
        }
        if (!UiUtils.confirm(this, "¿Borrar el producto seleccionado? Si aparece en detalles de venta no se permitirá.")) {
            return;
        }
        try {
            controller.delete(id);
            loadData();
            clearForm();
        } catch (Exception ex) {
            UiUtils.showError(this, new RuntimeException("No se puede borrar: revisa detalles de venta asociados.", ex));
        }
    }
}
