package controladores;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import entidades.Venta;

/**
 * @author jfervic933
 */
public class VentaController {

    private final EntityManagerFactory emf;

    public VentaController() {
        // Name of the persistence unit defined in persistence.xml
        this.emf = Persistence.createEntityManagerFactory("ventas");
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Create a new sale in the database.
     * @param venta sale to save
     */
    public void create(Venta venta) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(venta);
            tx.commit();

        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error creating sale", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Find a sale by id.
     * @param id sale id
     * @return the sale or null if it does not exist
     */
    public Venta findById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Venta.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Get all sales from the database.
     * @return list of sales
     */
    public List<Venta> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createNamedQuery("Venta.findAll", Venta.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Update an existing sale.
     * @param venta sale to update
     */
    public void update(Venta venta) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(venta);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error updating sale", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Delete a sale from the database.
     * @param id sale id to delete
     */
    public void delete(Integer id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Venta venta = em.find(Venta.class, id);
            if (venta != null) {
                em.createNativeQuery("delete from detalleventa where idventa = ?")
                        .setParameter(1, id)
                        .executeUpdate();
                em.remove(venta);
            }
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error deleting sale", ex);
        } finally {
            em.close();
        }
    }

    public void deleteAll() {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createNativeQuery("delete from venta").executeUpdate();
            em.createNativeQuery("alter table empresa_ventas.venta AUTO_INCREMENT = 1").executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error deleting all sales", ex);
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

    public void sincronizar() {
        getEntityManager().flush();
    }
}
