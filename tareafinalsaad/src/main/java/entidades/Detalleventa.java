package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author jfervic933
 */
@Entity
@Table(name = "detalleventa")
@NamedQueries({
    @NamedQuery(name = "Detalleventa.findAll", query = "SELECT d FROM Detalleventa d"),
    @NamedQuery(name = "Detalleventa.findById", query = "SELECT d FROM Detalleventa d WHERE d.id = :id"),
    @NamedQuery(name = "Detalleventa.findByCantidad", query = "SELECT d FROM Detalleventa d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "Detalleventa.findByPrecioventa", query = "SELECT d FROM Detalleventa d WHERE d.precioventa = :precioventa")})
public class Detalleventa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "cantidad")
    private Integer cantidad;
    @Column(name = "precioventa")
    private Float precioventa;

    // Detalleventa owns the relation with Producto because this table
    // contains the foreign key.
    @JoinColumn(name = "idproducto", referencedColumnName = "id")
    @ManyToOne
    private Producto idproducto;

    // Detalleventa also owns the relation with Venta because this table
    // contains that foreign key too.
    @JoinColumn(name = "idventa", referencedColumnName = "id")
    @ManyToOne
    private Venta idventa;

    public Detalleventa() {
    }

    public Detalleventa(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Float getPrecioventa() {
        return precioventa;
    }

    public void setPrecioventa(Float precioventa) {
        this.precioventa = precioventa;
    }

    public Producto getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(Producto idproducto) {
        this.idproducto = idproducto;
    }

    public Venta getVenta() {
        return idventa;
    }

    public void setVenta(Venta venta) {
        this.idventa = venta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Detalleventa)) {
            return false;
        }
        Detalleventa other = (Detalleventa) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "Detalleventa{" + "id=" + id + ", cantidad=" + cantidad + ", precioventa="
                + precioventa + ", idproducto=" + idproducto.getId() + ", idventa=" + idventa.getId() + '}';
    }

}
