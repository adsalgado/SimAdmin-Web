/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.sharkit.web.model;

import java.io.Serializable;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jlopez
 */
@Entity
@Table(name = "ptb_cuidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuidad.findAll", query = "SELECT c FROM Cuidad c")
    , @NamedQuery(name = "Cuidad.findById", query = "SELECT c FROM Cuidad c WHERE c.id = :id")
    , @NamedQuery(name = "Cuidad.findByClave", query = "SELECT c FROM Cuidad c WHERE c.clave = :clave")
    , @NamedQuery(name = "Cuidad.findByNombre", query = "SELECT c FROM Cuidad c WHERE c.nombre = :nombre")})
public class Cuidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "clave")
    private String clave;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    
    @JoinColumn(name = "zona_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private Zona zona;
    
    @Basic(optional = false)
    @Column(name = "zona_id")
    private Integer zonaId;
    
    @OneToMany(mappedBy = "cuidadId")
    private List<Usuario> cfgUsuarioList;

    public Cuidad() {
    }

    public Cuidad(Integer id) {
        this.id = id;
    }

    public Cuidad(Integer id, String clave, String nombre) {
        this.id = id;
        this.clave = clave;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Integer getZonaId() {
        return zonaId;
    }

    public void setZonaId(Integer zonaId) {
        this.zonaId = zonaId;
    }
    
    

    @XmlTransient
    public List<Usuario> getCfgUsuarioList() {
        return cfgUsuarioList;
    }

    public void setCfgUsuarioList(List<Usuario> cfgUsuarioList) {
        this.cfgUsuarioList = cfgUsuarioList;
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
        if (!(object instanceof Cuidad)) {
            return false;
        }
        Cuidad other = (Cuidad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mx.sharktech.pojos.Cuidad[ id=" + id + " ]";
    }
    
}
