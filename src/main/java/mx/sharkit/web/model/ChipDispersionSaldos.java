package mx.sharkit.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Adrián Salgado D.
 */
@Entity
@Table(name = "ptb_chip_dispersion_saldos")
@XmlRootElement
public class ChipDispersionSaldos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "chip_id")
    private Long chipId;
    @Column(name = "no_telefono")
    private String noTelefono;
    @Column(name = "folio")
    private String folio;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto")
    private BigDecimal monto;
    
    @JoinColumn(name = "chip_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_cds_chip"))
    @OneToOne(optional = false)
    private Chip ptbChip;

    public ChipDispersionSaldos() {
    }

    public ChipDispersionSaldos(Long chipId) {
        this.chipId = chipId;
    }

    public Long getChipId() {
        return chipId;
    }

    public void setChipId(Long chipId) {
        this.chipId = chipId;
    }

    public String getNoTelefono() {
        return noTelefono;
    }

    public void setNoTelefono(String noTelefono) {
        this.noTelefono = noTelefono;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Chip getPtbChip() {
        return ptbChip;
    }

    public void setPtbChip(Chip ptbChip) {
        this.ptbChip = ptbChip;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (chipId != null ? chipId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChipDispersionSaldos)) {
            return false;
        }
        ChipDispersionSaldos other = (ChipDispersionSaldos) object;
        if ((this.chipId == null && other.chipId != null) || (this.chipId != null && !this.chipId.equals(other.chipId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.sharkit.web.models.PtbChipDispersionSaldos[ chipId=" + chipId + " ]";
    }
    
}
