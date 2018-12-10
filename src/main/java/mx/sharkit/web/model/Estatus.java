/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.sharkit.web.model;

import java.io.Serializable;
import java.util.List;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jlopez
 */
@Entity
@Table(name = "ptb_estatus")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estatus.findAll", query = "SELECT e FROM Estatus e")
    , @NamedQuery(name = "Estatus.findById", query = "SELECT e FROM Estatus e WHERE e.id = :id")
    , @NamedQuery(name = "Estatus.findByNombre", query = "SELECT e FROM Estatus e WHERE e.nombre = :nombre")})
public class Estatus implements Serializable {

    private static final long serialVersionUID = 1L;

    public static Integer ID_ESTATUS_CHIP_NUEVO = 1;

    public static Integer ID_ESTATUS_ASIGNADO_SUPERVISOR = 2;
    public static Integer ID_ESTATUS_ASIGNADO_VENDEDOR = 3;
    public static Integer ID_ESTATUS_ASIGNADO_MAYORISTA = 5;
    public static Integer ID_ESTATUS_ASIGNADO_DISTRIBUIDOR = 17;
    public static Integer ID_ESTATUS_EN_ALMACEN = 18;
    public static Integer ID_ESTATUS_VENDIDO = 4;
    public static Integer ID_ESTATUS_DN_TEMPORAL = 6;
    public static Integer ID_ESTATUS_PERDIDO = 15;
    public static Integer ID_ESTATUS_LIBERADO = 16;
    public static Integer ID_ESTATUS_PORTABILIDAD_EXITOSA = 11;
    public static Integer ID_ESTATUS_CANCELADO = 19;
    public static Integer ID_ESTATUS_CERRADO = 20;
    
    public static Integer ID_ESTATUS_PROCESO_FINALIZADO = 12;
    public static Integer ID_ESTATUS_PROCESO_EN_PROCESO = 13;
    public static Integer ID_ESTATUS_PROCESO_ERRONEA = 14;
    
    public static Integer TIPO_ESTATUS_INICIAL = 1;
    public static Integer TIPO_ESTATUS_PORTABILIDAD = 2;
    public static Integer TIPO_ESTATUS_PROCESO = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estatusId")
    private List<Chip> chipList;

    @JoinColumn(name = "tipo_estatus_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private TipoEstatus tipoEstatus;
    @Basic(optional = false)
    @Column(name = "tipo_estatus_id")
    private Integer tipoEstatusId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estatusId")
    private List<ChipHistoricoEstatus> chipHistoricoEstatusList;

    public Estatus() {
    }

    public Estatus(Integer id) {
        this.id = id;
    }

    public Estatus(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    @XmlTransient
    public List<Chip> getChipList() {
        return chipList;
    }

    public void setChipList(List<Chip> chipList) {
        this.chipList = chipList;
    }

    public TipoEstatus getTipoEstatus() {
        return tipoEstatus;
    }

    public void setTipoEstatus(TipoEstatus tipoEstatus) {
        this.tipoEstatus = tipoEstatus;
    }

    public Integer getTipoEstatusId() {
        return tipoEstatusId;
    }

    public void setTipoEstatusId(Integer tipoEstatusId) {
        this.tipoEstatusId = tipoEstatusId;
    }

    @XmlTransient
    public List<ChipHistoricoEstatus> getChipHistoricoEstatusList() {
        return chipHistoricoEstatusList;
    }

    public void setChipHistoricoEstatusList(List<ChipHistoricoEstatus> chipHistoricoEstatusList) {
        this.chipHistoricoEstatusList = chipHistoricoEstatusList;
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
        if (!(object instanceof Estatus)) {
            return false;
        }
        Estatus other = (Estatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mx.sharktech.pojos.Estatus[ id=" + id + " ]";
    }

}
