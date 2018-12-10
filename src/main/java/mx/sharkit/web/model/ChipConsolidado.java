package mx.sharkit.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Adrián Salgado D.
 */
@Entity
@Table(name = "ptb_chip_consolidado")
@XmlRootElement
public class ChipConsolidado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "chip_id")
    private Long chipId;
    @Basic(optional = false)
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @Column(name = "factura")
    private String factura;
    @Column(name = "importe_factura")
    private BigDecimal importeFactura;
    @Column(name = "importe_venta")
    private BigDecimal importeVenta;
    @Column(name = "importe")
    private BigDecimal importe;
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "ncredito")
    private String ncredito;
    @Column(name = "motivo")
    private String motivo;
    @Column(name = "llave")
    private String llave;
    
    @JoinColumn(name = "chip_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_cc_chip"))
    @ManyToOne(optional = false)
    private Chip chip;

    public ChipConsolidado() {
    }

    public ChipConsolidado(Long chipId) {
        this.chipId = chipId;
    }

    public Long getChipId() {
        return chipId;
    }

    public void setChipId(Long chipId) {
        this.chipId = chipId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public BigDecimal getImporteFactura() {
        return importeFactura;
    }

    public void setImporteFactura(BigDecimal importeFactura) {
        this.importeFactura = importeFactura;
    }

    public BigDecimal getImporteVenta() {
        return importeVenta;
    }

    public void setImporteVenta(BigDecimal importeVenta) {
        this.importeVenta = importeVenta;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getNcredito() {
        return ncredito;
    }

    public void setNcredito(String ncredito) {
        this.ncredito = ncredito;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public Chip getChip() {
        return chip;
    }

    public void setChip(Chip chip) {
        this.chip = chip;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.chipId);
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
        final ChipConsolidado other = (ChipConsolidado) obj;
        if (!Objects.equals(this.chipId, other.chipId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ChipConsolidado{" + "chipId=" + chipId + '}';
    }

}
