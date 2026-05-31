package controladores;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import entidades.Cliente;

/**
 * @author jfervic933
 */
public class ClienteController {

    private final EntityManagerFactory emf;

    public ClienteController() {
        // Name of the persistence unit defined in persistence.xml
        this.emf = Persistence.createEntityManagerFactory("ventas");
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Create a new client in the database.
     *
     * @param cliente client to save
     */
    public void create(Cliente cliente) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(cliente);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error creating client", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Find a client by id.
     *
     * @param id client id
     * @return the client or null if it does not exist
     */
    public Cliente findById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Get all clients from the database.
     *
     * @return list of clients
     */
    public List<Cliente> findAll() {
        EntityManager em = getEntityManager();
        try {
            // Use the named query defined in Cliente
            return em.createNamedQuery("Cliente.findAll", Cliente.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Update an existing client.
     *
     * @param cliente client to update
     */
    public void update(Cliente cliente) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(cliente);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error updating client", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Delete a client from the database.
     *
     * @param id client id to delete
     */
    public void delete(Integer id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Cliente cliente = em.find(Cliente.class, id);
            if (cliente != null) {
                em.remove(cliente);
            }
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error deleting client", ex);
        } finally {
            em.close();
        }
    }

    public void deleteAll() {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Example of a native SQL query used to delete all rows
            // and reset the auto-increment counter.
            em.createNativeQuery("delete from cliente").executeUpdate();
            em.createNativeQuery("alter table empresa_ventas.cliente AUTO_INCREMENT = 1").executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error deleting all clients", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Close the EntityManagerFactory when it is no longer needed.
     */
    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
