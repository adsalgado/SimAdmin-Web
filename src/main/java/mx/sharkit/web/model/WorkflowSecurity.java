package mx.sharkit.web.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Adrián Salgado D.
 */
@Entity
@Table(name = "cfg_workflow_security")
@XmlRootElement
public class WorkflowSecurity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "workflow_config_id")
    private Integer workflowConfigId;
    @Basic(optional = false)
    @Column(name = "rol_id")
    private Long rolId;
    @Basic(optional = false)
    @Column(name = "permiso_estatus")
    private String permisoEstatus;
    @Basic(optional = false)
    @Column(name = "permiso_evento")
    private String permisoEvento;
    @Basic(optional = false)
    @Column(name = "fecha_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Basic(optional = false)
    @Column(name = "usuario_alta")
    private String usuarioAlta;
    @Column(name = "fecha_ult_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltModificacion;
    @Column(name = "usuario_ult_modificacion")
    private String usuarioUltModificacion;

    public WorkflowSecurity() {
    }

    public WorkflowSecurity(Integer workflowConfigId, Long rolId, String permisoEstatus, String permisoEvento, Date fechaAlta, String usuarioAlta) {
        this.workflowConfigId = workflowConfigId;
        this.rolId = rolId;
        this.permisoEstatus = permisoEstatus;
        this.permisoEvento = permisoEvento;
        this.fechaAlta = fechaAlta;
        this.usuarioAlta = usuarioAlta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWorkflowConfigId() {
        return workflowConfigId;
    }

    public void setWorkflowConfigId(Integer workflowConfigId) {
        this.workflowConfigId = workflowConfigId;
    }

    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }

    public String getPermisoEstatus() {
        return permisoEstatus;
    }

    public void setPermisoEstatus(String permisoEstatus) {
        this.permisoEstatus = permisoEstatus;
    }

    public String getPermisoEvento() {
        return permisoEvento;
    }

    public void setPermisoEvento(String permisoEvento) {
        this.permisoEvento = permisoEvento;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(String usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Date getFechaUltModificacion() {
        return fechaUltModificacion;
    }

    public void setFechaUltModificacion(Date fechaUltModificacion) {
        this.fechaUltModificacion = fechaUltModificacion;
    }

    public String getUsuarioUltModificacion() {
        return usuarioUltModificacion;
    }

    public void setUsuarioUltModificacion(String usuarioUltModificacion) {
        this.usuarioUltModificacion = usuarioUltModificacion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final WorkflowSecurity other = (WorkflowSecurity) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WorkflowSecurity{" + "id=" + id + ", workflowConfigId=" + workflowConfigId + ", rolId=" + rolId + ", permisoEstatus=" + permisoEstatus + ", permisoEvento=" + permisoEvento + '}';
    }

}
