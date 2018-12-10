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
@Table(name = "cfg_usuario_rol")
@XmlRootElement
public class UsuarioRol implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static Integer ID_ROL_SUPERVISOR = 3;
    public static Integer ID_ROL_VENDEDOR = 4;
    public static Integer ID_ROL_COORDINADOR = 5;
    public static Integer ID_ROL_MAYORISTA = 6;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "rol_id")
    private Long rolId;
    @Basic(optional = false)
    @Column(name = "usuario_id")
    private Long usuarioId;
    @JoinColumn(name = "rol_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_ur_rol"))
    @ManyToOne(optional = false)
    private Rol rol;
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_ur_usuario"))
    @ManyToOne(optional = false)
    private Usuario usuario;

    public UsuarioRol() {
    }

    public UsuarioRol(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
        if (!(object instanceof UsuarioRol)) {
            return false;
        }
        UsuarioRol other = (UsuarioRol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.sharkit.web.models.UsuarioRol[ id=" + id + " ]";
    }
    
}
