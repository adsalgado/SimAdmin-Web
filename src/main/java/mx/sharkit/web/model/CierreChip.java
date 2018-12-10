package mx.sharkit.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Adrián Salgado
 */
@Entity
@Table(name = "ptb_cierre_chip")
@XmlRootElement
public class CierreChip implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "usuario_vendedor_id")
    private Long usuarioVendedorId;
    @Basic(optional = false)
    @Column(name = "total_venta")
    private BigDecimal totalVenta;
    @Basic(optional = false)
    @Column(name = "usuario_cierre_id")
    private Long usuarioCierreId;
    @Basic(optional = false)
    @Column(name = "fecha_cierre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCierre;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    
    @JoinColumn(name = "usuario_vendedor_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_cierre_vendedor"))
    @ManyToOne(optional = true)
    private Usuario usuarioVendedor;
    
    @JoinColumn(name = "usuario_cierre_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_cierre_usuario"))
    @ManyToOne(optional = true)
    private Usuario usuarioCierre;

    public CierreChip() {
    }

    public CierreChip(Long usuarioVendedorId, BigDecimal totalVenta, Long usuarioCierreId, Date fechaCierre) {
        this.usuarioVendedorId = usuarioVendedorId;
        this.totalVenta = totalVenta;
        this.usuarioCierreId = usuarioCierreId;
        this.fechaCierre = fechaCierre;
    }

    public CierreChip(Long usuarioVendedorId, BigDecimal totalVenta, Long usuarioCierreId, Date fechaCierre, Date fechaInicio, Date fechaFin) {
        this.usuarioVendedorId = usuarioVendedorId;
        this.totalVenta = totalVenta;
        this.usuarioCierreId = usuarioCierreId;
        this.fechaCierre = fechaCierre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioVendedorId() {
        return usuarioVendedorId;
    }

    public void setUsuarioVendedorId(Long usuarioVendedorId) {
        this.usuarioVendedorId = usuarioVendedorId;
    }

    public BigDecimal getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(BigDecimal totalVenta) {
        this.totalVenta = totalVenta;
    }

    public Long getUsuarioCierreId() {
        return usuarioCierreId;
    }

    public void setUsuarioCierreId(Long usuarioCierreId) {
        this.usuarioCierreId = usuarioCierreId;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public Usuario getUsuarioVendedor() {
        return usuarioVendedor;
    }

    public void setUsuarioVendedor(Usuario usuarioVendedor) {
        this.usuarioVendedor = usuarioVendedor;
    }

    public Usuario getUsuarioCierre() {
        return usuarioCierre;
    }

    public void setUsuarioCierre(Usuario usuarioCierre) {
        this.usuarioCierre = usuarioCierre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CierreChip other = (CierreChip) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CierreChip{" + "id=" + id + ", usuarioVendedorId=" + usuarioVendedorId + ", totalVenta=" + totalVenta + ", usuarioCierreId=" + usuarioCierreId + ", fechaCierre=" + fechaCierre + '}';
    }
    
}
