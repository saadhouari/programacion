package servicios;

import controladores.ClienteController;
import entidades.Cliente;
import java.util.ArrayList;

/**
 *
 * @author jfervic933
 *
 */
public class ServicioCliente {

    private static final ClienteController clienteController = new ClienteController();

    public static void insertarClientesEjemplo() {
        var lista = new ArrayList<Cliente>();
        lista.add(new Cliente("Elena Gutierrez", "23234554T"));
        lista.add(new Cliente("Marina Gil", "11223344G"));
        lista.add(new Cliente("Julia Martin", "75664499E"));
        lista.add(new Cliente("Manuel Perez", "99887766A"));
        lista.add(new Cliente("Luis Hernandez", "33445566T"));
        // All clients are saved without sales
        for (Cliente cliente : lista) {
            clienteController.create(cliente);
        }
        System.out.println("--- > Clientes de ejemplo insertados ");
    }

    public static void mostrarTodosClientes() {
        clienteController.findAll().forEach(System.out::println);
        System.out.println("-------------------------------------------------");
    }

    public static void borrarTodosClientes() {
        clienteController.deleteAll();
    }

}
