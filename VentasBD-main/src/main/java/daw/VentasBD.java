package daw;

import java.time.LocalDateTime;

import controladores.ClienteController;
import controladores.DetalleventaController;
import controladores.VentaController;
import entidades.Cliente;
import entidades.Detalleventa;
import entidades.Venta;
import servicios.ServicioCliente;
import servicios.ServicioDetalleVenta;
import servicios.ServicioProducto;
import servicios.ServicioVenta;

public class VentasBD {

    // In a bigger application, the service classes would usually contain
    // the business logic and use the controllers for database access.
    // In this exercise, the code is simpler and uses the controllers directly.
    private static final ClienteController clienteController = new ClienteController();
    // private static final ProductoController pc = new ProductoController(); // Not used in this main
    private static final VentaController ventaController = new VentaController();
    private static final DetalleventaController detalleVentaController = new DetalleventaController();

    public static void main(String[] args) {

        // Prepare the database.
        // Each time the program runs, old data is deleted to make test easier.
        prepararBaseDatos();

        System.out.println("Clientes en la base de datos ----------- ");
        ServicioCliente.mostrarTodosClientes();
        System.out.println("Productos en la base de datos ----------- ");
        ServicioProducto.mostrarTodosProductos();
        System.out.println("Ventas en la base de datos ----------- ");
        ServicioVenta.mostrarTodasVentas();

        // Insert one sample sale -----------------------------------------
        ServicioVenta.insertarVentaEjemplo();
        // Show the data after the insert
        System.out.println("Clientes en la base de datos con sus ventas ----------- ");
        ServicioCliente.mostrarTodosClientes();

        // Update the client with id 2 -------------------------------------------
        // Find the client by id and change the name
        Cliente cliente = clienteController.findById(2);
        cliente.setNombre("nombre nuevo");
        clienteController.update(cliente);
        // Show the data after the update
        System.out.println("Clientes en la base de datos con nombre modificado ----------- ");
        ServicioCliente.mostrarTodosClientes();

        // Update the date of sale 1 for client 1 -------------------------
        // Find the client by id
        Cliente clienteModificar = clienteController.findById(1);
        // Get the first sale of that client as an example
        Venta venta = clienteModificar.getVentaCollection().stream().toList().get(0);
        // Create the new date we want to save
        LocalDateTime localDateTime = LocalDateTime.of(2000, 1, 1, 12, 0, 0);
        venta.setFecha(localDateTime);
        // Save the updated sale
        ventaController.update(venta);
        // Show the data after the update
        System.out.println("Clientes en la base de datos con fecha de venta modificada ----------- ");
        ServicioCliente.mostrarTodosClientes();

        // Update the quantity of the first detail in sale 1 for client 1 -------------------------
        // Find the client by id
        Cliente clienteModificar2 = clienteController.findById(1);
        // Get the first sale as an example
        Venta venta2 = clienteModificar2.getVentaCollection().stream().toList().get(0);
        // Get the first detail of that sale as an example
        Detalleventa detalle = venta2.getDetalleventaCollection().stream().toList().get(0);
        detalle.setCantidad(100000);
        // Save the updated detail
        detalleVentaController.update(detalle);
        // Show the data after the update
        System.out.println("Clientes en la base de datos con detalle venta modificado ----------- ");
        ServicioCliente.mostrarTodosClientes();

        // Delete the first detail of sale 1 for client 1 -------------------------
        // Find the client by id
        Cliente clienteModificar3 = clienteController.findById(1);
        // Get the first sale
        Venta venta3 = clienteModificar3.getVentaCollection().stream().toList().get(0);
        // Get the first detail to delete
        Detalleventa detalle2 = venta3.getDetalleventaCollection().stream().toList().get(0);
        // Remove the detail from the sale to keep both sides of the relation in sync.
        // Because orphanRemoval = true, this detail is also deleted from the database
        // when the sale is updated.
        venta3.removeDetalleVenta(detalle2);
        // Save the updated sale
        ventaController.update(venta3);
        // Show the data after the delete
        System.out.println("Clientes en la base de datos con detalle venta eliminado ----------- ");
        ServicioCliente.mostrarTodosClientes();
    }

    private static void borrarTodo() {
        // First delete sale details because they contain foreign keys
        ServicioDetalleVenta.borrarTodosDetallesVentas();
        // Then delete sales because they depend on clients
        ServicioVenta.borrarTodasVentas();
        // After that, clients and products can be deleted safely
        ServicioCliente.borrarTodosClientes();
        ServicioProducto.borrarTodosProductos();
        System.out.println("Se han borrado todos los registros e inicializado las claves primarias de todas las tablas");
    }

    // Delete all data, reset ids, and insert sample clients and products
    private static void prepararBaseDatos() {
        borrarTodo();
        ServicioCliente.insertarClientesEjemplo();
        ServicioProducto.insertarProductosEjemplo();
    }

}
