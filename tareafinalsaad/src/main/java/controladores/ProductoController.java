package controladores;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import entidades.Producto;

/**
 *
 * @author jfervic933
 */
public class ProductoController {
    private final EntityManagerFactory emf;

    public ProductoController() {
        // Name of the persistence unit defined in persistence.xml
        this.emf = Persistence.createEntityManagerFactory("ventas");
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Create a new product in the database.
     * @param producto product to save
     */
    public void create(Producto producto) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(producto);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error creating product", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Find a product by id.
     * @param id product id
     * @return the product or null if it does not exist
     */
    public Producto findById(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Get all products from the database.
     * @return list of products
     */
    public List<Producto> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createNamedQuery("Producto.findAll", Producto.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Update an existing product.
     * @param producto product to update
     */
    public void update(Producto producto) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(producto);
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error updating product", ex);
        } finally {
            em.close();
        }
    }

    /**
     * Delete a product from the database.
     * @param id product id to delete
     */
    public void delete(Integer id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Producto producto = em.find(Producto.class, id);
            if (producto != null) {
                em.remove(producto);
            }
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error deleting product", ex);
        } finally {
            em.close();
        }
    }

    public void deleteAll() {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createNativeQuery("delete from producto").executeUpdate();
            em.createNativeQuery("alter table empresa_ventas.producto AUTO_INCREMENT = 1").executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error deleting all products", ex);
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
