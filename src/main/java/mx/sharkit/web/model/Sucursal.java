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
@Table(name = "ptb_sucursal")
@XmlRootElement
public class Sucursal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "empresa_id")
    private Integer empresaId;    
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
    
    @JoinColumn(name = "empresa_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Empresa empresa;
    
    @JoinColumn(name = "usuario_alta_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Usuario usuarioAlta;

    @JoinColumn(name = "usuario_ult_modificacion_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Usuario usuarioUltModificacion;

    @Basic(optional = true)
    @Column(name = "pais_id")
    private Integer paisId;

    @JoinColumn(name = "pais_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne
    private Pais pais;

    @Basic(optional = true)
    @Column(name = "zona_id")
    private Integer zonaId;

    @JoinColumn(name = "zona_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne
    private Zona zona;

    @Basic(optional = true)
    @Column(name = "cuidad_id")
    private Integer cuidadId;

    @JoinColumn(name = "cuidad_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne
    private Cuidad cuidad;

    public Sucursal() {
    }

    public Sucursal(Integer id) {
        this.id = id;
    }

    public Sucursal(Integer id, String nombre, Date fechaAlta, Long usuarioAltaId) {
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

    public Integer getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Integer empresaId) {
        this.empresaId = empresaId;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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

    public Integer getPaisId() {
        return paisId;
    }

    public void setPaisId(Integer paisId) {
        this.paisId = paisId;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Integer getZonaId() {
        return zonaId;
    }

    public void setZonaId(Integer zonaId) {
        this.zonaId = zonaId;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Integer getCuidadId() {
        return cuidadId;
    }

    public void setCuidadId(Integer cuidadId) {
        this.cuidadId = cuidadId;
    }

    public Cuidad getCuidad() {
        return cuidad;
    }

    public void setCuidad(Cuidad cuidad) {
        this.cuidad = cuidad;
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
        if (!(object instanceof Sucursal)) {
            return false;
        }
        Sucursal other = (Sucursal) object;
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
