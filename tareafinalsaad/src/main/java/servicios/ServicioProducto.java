package servicios;

import controladores.ProductoController;
import entidades.Producto;
import java.util.ArrayList;

/**
 *
 * @author jfervic933
 */
public class ServicioProducto {

    private static final ProductoController productoController = new ProductoController();

    public static void insertarProductosEjemplo() {
        var lista = new ArrayList<Producto>();
        lista.add(new Producto("Raton inalambrico", 34.45f, 100));
        lista.add(new Producto("Auriculares bluetooth", 54.05f, 100));
        lista.add(new Producto("Iphone 16", 934.0f, 100));
        lista.add(new Producto("Monitor LG", 67.45f, 100));
        lista.add(new Producto("Altavoz logitech", 14.98f, 100));

        for (Producto producto : lista) {
            productoController.create(producto);
        }
        System.out.println("--- > Productos de ejemplo insertados ");

    }

    public static void mostrarTodosProductos() {
        productoController.findAll().forEach(System.out::println);

    }
    
    public static void borrarTodosProductos(){
        productoController.deleteAll();
    }
}
