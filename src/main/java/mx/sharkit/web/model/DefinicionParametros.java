package mx.sharkit.web.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.Type;

/**
 *
 * @author Adrián Salgado
 */
@Entity
@Table(name = "cfg_definicion_parametros")
@XmlRootElement
public class DefinicionParametros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Column(name = "parent_id")
    private Integer parentId;
    @Basic(optional = false)
    @Column(name = "tipo_parametro_id")
    private Integer tipoParametroId;
    @Basic(optional = false)
    @Column(name = "clave_parametro")
    private String claveParametro;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "caracteristica_1")
    private String caracteristica1;
    @Column(name = "caracteristica_2")
    private String caracteristica2;
    @Column(name = "caracteristica_3")
    private String caracteristica3;
    @Column(name = "caracteristica_4")
    private String caracteristica4;
    @Column(name = "caracteristica_5")
    private String caracteristica5;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "configuracion")
    private String configuracion;
    @Column(name = "usuario_alta")
    private String usuarioAlta;
    @Column(name = "fecha_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Column(name = "usuario_ult_modificacion")
    private String usuarioUltModificacion;
    @Column(name = "fecha_ult_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltModificacion;
    @OneToMany(mappedBy = "parentId")
    private List<DefinicionParametros> definicionParametrosList;

    @JoinColumn(name = "parent_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne
    private DefinicionParametros parent;

    @JoinColumn(name = "tipo_parametro_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TipoParametro tipoParametro;

    public DefinicionParametros() {
    }

    public DefinicionParametros(Integer id) {
        this.id = id;
    }

    public DefinicionParametros(Integer id, String claveParametro, String descripcion) {
        this.id = id;
        this.claveParametro = claveParametro;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClaveParametro() {
        return claveParametro;
    }

    public void setClaveParametro(String claveParametro) {
        this.claveParametro = claveParametro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCaracteristica1() {
        return caracteristica1;
    }

    public void setCaracteristica1(String caracteristica1) {
        this.caracteristica1 = caracteristica1;
    }

    public String getCaracteristica2() {
        return caracteristica2;
    }

    public void setCaracteristica2(String caracteristica2) {
        this.caracteristica2 = caracteristica2;
    }

    public String getCaracteristica3() {
        return caracteristica3;
    }

    public void setCaracteristica3(String caracteristica3) {
        this.caracteristica3 = caracteristica3;
    }

    public String getCaracteristica4() {
        return caracteristica4;
    }

    public void setCaracteristica4(String caracteristica4) {
        this.caracteristica4 = caracteristica4;
    }

    public String getCaracteristica5() {
        return caracteristica5;
    }

    public void setCaracteristica5(String caracteristica5) {
        this.caracteristica5 = caracteristica5;
    }

    public String getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(String configuracion) {
        this.configuracion = configuracion;
    }

    public String getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(String usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getUsuarioUltModificacion() {
        return usuarioUltModificacion;
    }

    public void setUsuarioUltModificacion(String usuarioUltModificacion) {
        this.usuarioUltModificacion = usuarioUltModificacion;
    }

    public Date getFechaUltModificacion() {
        return fechaUltModificacion;
    }

    public void setFechaUltModificacion(Date fechaUltModificacion) {
        this.fechaUltModificacion = fechaUltModificacion;
    }

    @XmlTransient
    public List<DefinicionParametros> getDefinicionParametrosList() {
        return definicionParametrosList;
    }

    public void setDefinicionParametrosList(List<DefinicionParametros> definicionParametrosList) {
        this.definicionParametrosList = definicionParametrosList;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getTipoParametroId() {
        return tipoParametroId;
    }

    public void setTipoParametroId(Integer tipoParametroId) {
        this.tipoParametroId = tipoParametroId;
    }

    public DefinicionParametros getParent() {
        return parent;
    }

    public void setParent(DefinicionParametros parent) {
        this.parent = parent;
    }

    public TipoParametro getTipoParametro() {
        return tipoParametro;
    }

    public void setTipoParametro(TipoParametro tipoParametro) {
        this.tipoParametro = tipoParametro;
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
        if (!(object instanceof DefinicionParametros)) {
            return false;
        }
        DefinicionParametros other = (DefinicionParametros) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.sharkit.web.models.DefinicionParametros[ id=" + id + " ]";
    }
    
}
