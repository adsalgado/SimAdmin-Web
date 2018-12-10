/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.sharkit.web.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jlopez
 */
@Entity
@Table(name = "ptb_chip_historico_estatus")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChipHistoricoEstatus.findAll", query = "SELECT c FROM ChipHistoricoEstatus c")
    , @NamedQuery(name = "ChipHistoricoEstatus.findById", query = "SELECT c FROM ChipHistoricoEstatus c WHERE c.id = :id")
    , @NamedQuery(name = "ChipHistoricoEstatus.findByFechaEstatus", query = "SELECT c FROM ChipHistoricoEstatus c WHERE c.fechaEstatus = :fechaEstatus")
    , @NamedQuery(name = "ChipHistoricoEstatus.findByUsuarioEstatus", query = "SELECT c FROM ChipHistoricoEstatus c WHERE c.usuarioEstatus = :usuarioEstatus")
    , @NamedQuery(name = "ChipHistoricoEstatus.findByObservaciones", query = "SELECT c FROM ChipHistoricoEstatus c WHERE c.observaciones = :observaciones")})
public class ChipHistoricoEstatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "fecha_estatus")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEstatus;
    @Basic(optional = false)
    @Column(name = "usuario_estatus")
    private String usuarioEstatus;
    @Column(name = "observaciones")
    private String observaciones;
    
    @JoinColumn(name = "serie", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_chs_chip"))
    @ManyToOne(optional = true)
    private Chip serie;
    @Basic(optional = false)
    @Column(name = "serie")
    private Long serieId;
    
    @JoinColumn(name = "estatus_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_chs_estatus"))
    @ManyToOne(optional = true)
    private Estatus estatus;
    @Basic(optional = false)
    @Column(name = "estatus_id")
    private Integer estatusId;

    public ChipHistoricoEstatus() {
    }

    public ChipHistoricoEstatus(Long id) {
        this.id = id;
    }

    public ChipHistoricoEstatus(Long id, Date fechaEstatus, String usuarioEstatus) {
        this.id = id;
        this.fechaEstatus = fechaEstatus;
        this.usuarioEstatus = usuarioEstatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaEstatus() {
        return fechaEstatus;
    }

    public void setFechaEstatus(Date fechaEstatus) {
        this.fechaEstatus = fechaEstatus;
    }

    public String getUsuarioEstatus() {
        return usuarioEstatus;
    }

    public void setUsuarioEstatus(String usuarioEstatus) {
        this.usuarioEstatus = usuarioEstatus;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Chip getSerie() {
        return serie;
    }

    public void setSerie(Chip serie) {
        this.serie = serie;
    }

    public Long getSerieId() {
        return serieId;
    }

    public void setSerieId(Long serieId) {
        this.serieId = serieId;
    }

    public Estatus getEstatus() {
        return estatus;
    }

    public void setEstatus(Estatus estatus) {
        this.estatus = estatus;
    }

    public Integer getEstatusId() {
        return estatusId;
    }

    public void setEstatusId(Integer estatusId) {
        this.estatusId = estatusId;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChipHistoricoEstatus)) {
            return false;
        }
        ChipHistoricoEstatus other = (ChipHistoricoEstatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mx.sharktech.pojos.ChipHistoricoEstatus[ id=" + id + " ]";
    }
    
}
