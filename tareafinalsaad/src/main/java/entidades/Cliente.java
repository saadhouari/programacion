package entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jfervic933
 */
@Entity
@Table(name = "cliente")

// Named queries are predefined JPQL queries.
// They can be reused without writing the full query every time.
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c"),
    @NamedQuery(name = "Cliente.findById", query = "SELECT c FROM Cliente c WHERE c.id = :id"),
    @NamedQuery(name = "Cliente.findByNombre", query = "SELECT c FROM Cliente c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Cliente.findByNif", query = "SELECT c FROM Cliente c WHERE c.nif = :nif")})
// Serializable is commonly used in JPA entities.
// It is not always required, but it is a good practice.
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "nif")
    private String nif;

    // One client can have many sales.
    // This is the inverse side of the relation because the foreign key
    // is stored in Venta.
    // cascade = PERSIST saves new sales when the client is saved.
    // orphanRemoval = true deletes a sale if it is removed from the collection.
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Collection<Venta> ventaCollection;

    public Cliente() {
    }

    public Cliente(Integer id) {
        this.id = id;
    }

    public Cliente(Integer id, String nif) {
        this.id = id;
        this.nif = nif;
        this.ventaCollection = new ArrayList<>();
    }

    public Cliente(String nombre, String nif) {
        this.nombre = nombre;
        this.nif = nif;
        this.ventaCollection = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public Collection<Venta> getVentaCollection() {
        return ventaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        String tmp = "";
        for (Venta venta : ventaCollection) {
            tmp += venta + "\n";
        }
        return "Cliente{" + "id=" + id + ", nombre=" + nombre + ", nif=" + nif + ", ventas cliente=\n" + tmp + '}';
    }

    public void setVentaCollection(Collection<Venta> ventaCollection) {
        this.ventaCollection = ventaCollection;
        for (Venta venta : ventaCollection) {
            venta.setIdcliente(this);
        }
    }

    public void addVenta(Venta venta) {
        this.ventaCollection.add(venta);
        venta.setIdcliente(this);
    }

    public void removeVenta(Venta venta) {
        this.ventaCollection.remove(venta);
        venta.setIdcliente(null);
    }

}
