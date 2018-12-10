package mx.sharkit.web.model;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Adrián Salgado
 */
@Entity
@Table(name = "cfg_permiso")
@XmlRootElement
public class Permiso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "consultar")
    private String consultar;
    @Basic(optional = false)
    @Column(name = "crear")
    private String crear;
    @Basic(optional = false)
    @Column(name = "eliminar")
    private String eliminar;
    @Basic(optional = false)
    @Column(name = "modificar")
    private String modificar;
    @Basic(optional = false)
    @Column(name = "opcion_id")
    private Long opcionId;
    @Basic(optional = false)
    @Column(name = "rol_id")    
    private Long rolId;

    @JoinColumn(name = "opcion_id", referencedColumnName = "id", insertable = false, updatable = false, 
            foreignKey = @ForeignKey(name="fk_permiso_opcion"))
    @ManyToOne(optional = false)
    private Opcion opcion;
    
    @JoinColumn(name = "rol_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_permiso_rol"))
    @ManyToOne(optional = false)
    private Rol rol;

    public Permiso() {
    }

    public Permiso(Long id) {
        this.id = id;
    }

    public Permiso(Long id, String consultar, String crear, String eliminar, String modificar) {
        this.id = id;
        this.consultar = consultar;
        this.crear = crear;
        this.eliminar = eliminar;
        this.modificar = modificar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConsultar() {
        return consultar;
    }

    public void setConsultar(String consultar) {
        this.consultar = consultar;
    }

    public String getCrear() {
        return crear;
    }

    public void setCrear(String crear) {
        this.crear = crear;
    }

    public String getEliminar() {
        return eliminar;
    }

    public void setEliminar(String eliminar) {
        this.eliminar = eliminar;
    }

    public String getModificar() {
        return modificar;
    }

    public void setModificar(String modificar) {
        this.modificar = modificar;
    }

    public Long getOpcionId() {
        return opcionId;
    }

    public void setOpcionId(Long opcionId) {
        this.opcionId = opcionId;
    }

    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }

    public Opcion getOpcion() {
        return opcion;
    }

    public void setOpcion(Opcion opcion) {
        this.opcion = opcion;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
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
        if (!(object instanceof Permiso)) {
            return false;
        }
        Permiso other = (Permiso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.sharkit.web.models.Permiso[ id=" + id + " ]";
    }
    
}
