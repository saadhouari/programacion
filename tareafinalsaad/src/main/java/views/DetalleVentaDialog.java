package views;

import controladores.DetalleventaController;
import controladores.ProductoController;
import controladores.VentaController;
import entidades.Detalleventa;
import entidades.Producto;
import entidades.Venta;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
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

class DetalleVentaDialog extends JDialog {

    private final DetalleventaController detalleController = new DetalleventaController();
    private final VentaController ventaController = new VentaController();
    private final ProductoController productoController = new ProductoController();
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Venta", "Producto", "Cantidad", "Precio venta"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable();
    private final JTextField idField = new JTextField();
    private final JComboBox<ComboItem<Venta>> ventaCombo = new JComboBox<>();
    private final JComboBox<ComboItem<Producto>> productoCombo = new JComboBox<>();
    private final JTextField cantidadField = new JTextField();
    private final JTextField precioField = new JTextField();

    DetalleVentaDialog(Frame owner) {
        super(owner, "Gestión de detalles de venta", true);
        setSize(940, 520);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));
        UiUtils.configureReadOnlyTable(table, model);
        table.getSelectionModel().addListSelectionListener(event -> loadSelected());
        productoCombo.addActionListener(event -> fillProductPrice());

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(formPanel(), BorderLayout.EAST);
        add(buttonPanel(), BorderLayout.SOUTH);
        loadCombos();
        loadData();
    }

    private JPanel formPanel() {
        idField.setEditable(false);
        JPanel panel = new JPanel(new GridLayout(10, 1, 6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Datos"));
        panel.add(new JLabel("ID"));
        panel.add(idField);
        panel.add(new JLabel("Venta"));
        panel.add(ventaCombo);
        panel.add(new JLabel("Producto"));
        panel.add(productoCombo);
        panel.add(new JLabel("Cantidad"));
        panel.add(cantidadField);
        panel.add(new JLabel("Precio venta"));
        panel.add(precioField);
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
        ventaCombo.removeAllItems();
        productoCombo.removeAllItems();
        for (Venta venta : ventaController.findAll()) {
            ventaCombo.addItem(new ComboItem<>(venta, venta.getId() + " - " + UiUtils.formatDate(venta.getFecha())));
        }
        for (Producto producto : productoController.findAll()) {
            productoCombo.addItem(new ComboItem<>(producto, producto.getId() + " - " + producto.getDescripcion()));
        }
    }

    private void loadData() {
        model.setRowCount(0);
        for (Detalleventa detalle : detalleController.findAll()) {
            model.addRow(new Object[]{detalle.getId(), detalle.getVenta().getId(),
                detalle.getIdproducto().getDescripcion(), detalle.getCantidad(), detalle.getPrecioventa()});
        }
    }

    private void loadSelected() {
        if (table.getSelectedRow() < 0) {
            return;
        }
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        Integer id = Integer.valueOf(model.getValueAt(row, 0).toString());
        Detalleventa detalle = detalleController.findById(id);
        idField.setText(id.toString());
        selectVenta(detalle.getVenta().getId());
        selectProducto(detalle.getIdproducto().getId());
        cantidadField.setText(String.valueOf(detalle.getCantidad()));
        precioField.setText(String.valueOf(detalle.getPrecioventa()));
    }

    private void selectVenta(Integer id) {
        for (int i = 0; i < ventaCombo.getItemCount(); i++) {
            if (ventaCombo.getItemAt(i).getValue().getId().equals(id)) {
                ventaCombo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void selectProducto(Integer id) {
        for (int i = 0; i < productoCombo.getItemCount(); i++) {
            if (productoCombo.getItemAt(i).getValue().getId().equals(id)) {
                productoCombo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void fillProductPrice() {
        ComboItem<Producto> selected = (ComboItem<Producto>) productoCombo.getSelectedItem();
        if (selected != null && idField.getText().isBlank()) {
            precioField.setText(String.valueOf(selected.getValue().getPrecio()));
        }
    }

    private void clearForm() {
        table.clearSelection();
        idField.setText("");
        cantidadField.setText("1");
        if (ventaCombo.getItemCount() > 0) {
            ventaCombo.setSelectedIndex(0);
        }
        if (productoCombo.getItemCount() > 0) {
            productoCombo.setSelectedIndex(0);
        }
        fillProductPrice();
    }

    private void save() {
        try {
            ComboItem<Venta> ventaItem = (ComboItem<Venta>) ventaCombo.getSelectedItem();
            ComboItem<Producto> productoItem = (ComboItem<Producto>) productoCombo.getSelectedItem();
            if (ventaItem == null || productoItem == null) {
                throw new IllegalArgumentException("Debe existir al menos una venta y un producto.");
            }
            int cantidad = UiUtils.parseInt(cantidadField.getText(), "cantidad");
            float precio = UiUtils.parseFloat(precioField.getText(), "precio venta");
            if (cantidad <= 0 || precio < 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor que 0 y el precio no puede ser negativo.");
            }
            Detalleventa detalle = idField.getText().isBlank()
                    ? new Detalleventa()
                    : detalleController.findById(Integer.valueOf(idField.getText()));
            detalle.setVenta(ventaItem.getValue());
            detalle.setIdproducto(productoItem.getValue());
            detalle.setCantidad(cantidad);
            detalle.setPrecioventa(precio);
            if (idField.getText().isBlank()) {
                detalleController.create(detalle);
            } else {
                detalleController.update(detalle);
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
            UiUtils.showInfo(this, "Selecciona un detalle para borrar.");
            return;
        }
        if (!UiUtils.confirm(this, "¿Borrar el detalle seleccionado?")) {
            return;
        }
        try {
            detalleController.delete(id);
            loadData();
            clearForm();
        } catch (Exception ex) {
            UiUtils.showError(this, ex);
        }
    }
}
