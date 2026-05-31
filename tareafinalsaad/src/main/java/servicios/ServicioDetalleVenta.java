
package servicios;

import controladores.DetalleventaController;

/**
 *
 * @author jfervic933
 */
public class ServicioDetalleVenta {
    
    private static final DetalleventaController detalleVentaController = new DetalleventaController();
    
    public static void borrarTodosDetallesVentas(){
        detalleVentaController.deleteAll();
    }
    
    public static void mostrarTodosDetallesVenta() {
        detalleVentaController.findAll().forEach(System.out::println);

    }
}
