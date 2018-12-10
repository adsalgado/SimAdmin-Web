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
import javax.persistence.FetchType;
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
 * @author asalgado
 */
@Entity
@Table(name = "ptb_empresa")
@XmlRootElement
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre", length = 128)
    private String nombre;
    @Column(name = "descripcion", length = 256)
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "fecha_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Basic(optional = false)
    @Column(name = "usuario_alta_id")
    private Long usuarioAltaId;
    @Column(name = "fecha_ult_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltModificacion;
    @Column(name = "usuario_ult_modificacion_id")
    private Long usuarioUltModificacionId;
    
    @JoinColumn(name = "usuario_alta_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Usuario usuarioAlta;

    @JoinColumn(name = "usuario_ult_modificacion_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Usuario usuarioUltModificacion;

    public Empresa() {
    }

    public Empresa(Integer id) {
        this.id = id;
    }

    public Empresa(Integer id, String nombre, Date fechaAlta, Long usuarioAltaId) {
        this.id = id;
        this.nombre = nombre;
        this.fechaAlta = fechaAlta;
        this.usuarioAltaId = usuarioAltaId;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaUltModificacion() {
        return fechaUltModificacion;
    }

    public void setFechaUltModificacion(Date fechaUltModificacion) {
        this.fechaUltModificacion = fechaUltModificacion;
    }

    public Long getUsuarioAltaId() {
        return usuarioAltaId;
    }

    public void setUsuarioAltaId(Long usuarioAltaId) {
        this.usuarioAltaId = usuarioAltaId;
    }

    public Long getUsuarioUltModificacionId() {
        return usuarioUltModificacionId;
    }

    public void setUsuarioUltModificacionId(Long usuarioUltModificacionId) {
        this.usuarioUltModificacionId = usuarioUltModificacionId;
    }

    public Usuario getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuario usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuario getUsuarioUltModificacion() {
        return usuarioUltModificacion;
    }

    public void setUsuarioUltModificacion(Usuario usuarioUltModificacion) {
        this.usuarioUltModificacion = usuarioUltModificacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.pissa.tia.lib.capacitacion.pojos.Empresa[ id=" + id + " ]";
    }

}
