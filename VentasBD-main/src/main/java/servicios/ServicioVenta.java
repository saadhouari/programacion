package servicios;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import controladores.ClienteController;
import controladores.ProductoController;
import controladores.VentaController;
import entidades.Cliente;
import entidades.Detalleventa;
import entidades.Producto;
import entidades.Venta;

/**
 *
 * @author jfervic933
 */

public class ServicioVenta {

    private static final VentaController vc = new VentaController();
    private static final ClienteController cc = new ClienteController();
    private static final ProductoController pc = new ProductoController();

    public static void insertarVentaEjemplo() {
        // Find the client with id 1
        Cliente cliente = cc.findById(1);
        System.out.println("Insertando una venta para "
                + cliente.getNombre() + " " + cliente.getNif());

        // Create the sale with the current date and the selected client
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        Instant ahora = fechaHoraActual.toInstant(ZoneOffset.UTC);
        Venta venta = new Venta(Date.from(ahora), cliente);

        // Create detail rows with sample products and quantities
        Detalleventa detalle = new Detalleventa();
        Producto p1 = pc.findById(1);
        detalle.setIdproducto(p1);
        detalle.setCantidad(12);
        detalle.setPrecioventa(p1.getPrecio());

        // Add the detail to the sale
        venta.addDetalleVenta(detalle);

        // Create another detail
        Producto p2 = pc.findById(2);
        detalle = new Detalleventa();
        detalle.setIdproducto(p2);
        detalle.setCantidad(120);
        detalle.setPrecioventa(p2.getPrecio());

        // Add the second detail to the sale
        venta.addDetalleVenta(detalle);

        // Add the sale to the client
        cliente.addVenta(venta);

        // Save the sale. Because of cascade, the details are saved too
        vc.create(venta);

        // Update the client with the new sale
        cc.update(cliente);

    }

    public static void borrarTodasVentas() {
        vc.deleteAll();
    }

    public static void mostrarTodasVentas() {
        vc.findAll().forEach(System.out::println);

    }

}
