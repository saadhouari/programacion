package controladores;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import entidades.Detalleventa;

/**
 * @author jfervic933
 */
public class DetalleventaController {

    private final EntityManagerFactory emf;

    public DetalleventaController() {
        // Name of the persistence unit defined in persistence.xml
        this.emf = Persistence.createEntityManagerFactory("ventas");
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Create a new sale detail in the database.
     *
     * @param detalleventa detail to save
     */
    public void create(Detalleventa detalleventa) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(detalleventa);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error creating sale detail", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Find a sale detail by id.
     *
     * @param id detail id
     * @return the detail or null if it does not exist
     */
    public Detalleventa findById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detalleventa.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Get all sale details from the database.
     *
     * @return list of sale details
     */
    public List<Detalleventa> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createNamedQuery("Detalleventa.findAll", Detalleventa.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Update an existing sale detail.
     *
     * @param detalleventa detail to update
     */
    public void update(Detalleventa detalleventa) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(detalleventa);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error updating sale detail", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Delete a sale detail from the database.
     *
     * @param id detail id to delete
     */
    public void delete(Integer id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Detalleventa detalleventa = em.find(Detalleventa.class, id);
            if (detalleventa != null) {
                em.remove(detalleventa);
            }
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error deleting sale detail", ex);
        } finally {
            em.close();
        }
    }

    public void deleteAll() {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createNativeQuery("delete from detalleventa").executeUpdate();
            // Reset the auto increment value
            em.createNativeQuery("alter table empresa_ventas.detalleventa AUTO_INCREMENT = 1").executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error deleting all sale details", ex);
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
