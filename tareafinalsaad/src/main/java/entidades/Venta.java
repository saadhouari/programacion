package entidades;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jfervic933
 */
@Entity
@Table(name = "venta")
@NamedQueries({
    @NamedQuery(name = "Venta.findAll", query = "SELECT v FROM Venta v"),
    @NamedQuery(name = "Venta.findById", query = "SELECT v FROM Venta v WHERE v.id = :id"),
    @NamedQuery(name = "Venta.findByFecha", query = "SELECT v FROM Venta v WHERE v.fecha = :fecha")})
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    // Venta owns the relation with Cliente because this table
    // contains the foreign key.
    @JoinColumn(name = "idcliente", referencedColumnName = "id")
    // A sale must always have a client
    @ManyToOne(optional = false)
    private Cliente cliente;

    // One sale can have many detail rows.
    // This is the inverse side because the foreign key is stored in Detalleventa.
    // cascade = PERSIST saves new details when the sale is saved.
    // orphanRemoval = true deletes a detail if it is removed from the collection.
    @OneToMany(mappedBy = "idventa", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Collection<Detalleventa> detalleventaCollection;

    public Venta() {
    }

    public Venta(Integer id) {
        this.id = id;
    }

    // Extra constructor used in this project
    public Venta(Date fecha, Cliente cliente) {
        this.fecha = fecha;
        this.cliente = cliente;
        this.detalleventaCollection = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    // Return the date as LocalDateTime
    public LocalDateTime getFechaLocalDateTime() {
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        // Convert LocalDateTime to Date
        this.fecha = Date.from(fecha.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Cliente getIdcliente() {
        return cliente;
    }

    public void setIdcliente(Cliente idcliente) {
        this.cliente = idcliente;
    }

    public Collection<Detalleventa> getDetalleventaCollection() {
        return detalleventaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Venta)) {
            return false;
        }
        Venta other = (Venta) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        String tmp = "";
        for (Detalleventa detalle : detalleventaCollection) {
            tmp += detalle + "\n";
        }
        return "Venta{" + "id=" + id + ", fecha=" + fecha + ", idcliente=" + cliente.getNif()
                + ", detalleventaCollection=\n" + tmp + '}';
    }

    /*
            In JPA, relations must also be handled in the Java code.
            In a bidirectional relation, both sides should stay synchronized.
            If a related object is added to a collection, the back reference
            should also be updated.
     */
    public void setDetalleventaCollection(Collection<Detalleventa> detalleventaCollection) {
        this.detalleventaCollection = detalleventaCollection;
        // Set this sale in every detail to keep both sides synchronized
        for (Detalleventa detalleventa : detalleventaCollection) {
            detalleventa.setVenta(this);
        }
    }

    // Add one detail to this sale
    public void addDetalleVenta(Detalleventa detalleVenta) {
        this.detalleventaCollection.add(detalleVenta);
        // Keep the bidirectional relation synchronized
        detalleVenta.setVenta(this);
    }

    public void removeDetalleVenta(Detalleventa detalleVenta) {
        // Remove the detail from this sale
        this.detalleventaCollection.remove(detalleVenta);
        // Break the relation on the other side too
        detalleVenta.setVenta(null);
    }

}
