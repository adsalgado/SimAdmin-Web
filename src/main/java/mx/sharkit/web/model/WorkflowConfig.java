package mx.sharkit.web.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Adrián Salgado D.
 */
@Entity
@Table(name = "cfg_workflow_config")
@XmlRootElement
public class WorkflowConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "workflow_id")
    private Integer workflowId;
    @Basic(optional = false)
    @Column(name = "estatus_actual_id")
    private Integer estatusActualId;
    @Basic(optional = false)
    @Column(name = "estatus_siguiente_id")
    private Integer estatusSiguienteId;
    @Column(name = "etiqueta")
    private String etiqueta;
    @Column(name = "icono")
    private String icono;
    @Basic(optional = false)
    @Column(name = "evento")
    private String evento;
    @Column(name = "orden")
    private Integer orden;
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

    @JoinColumn(name = "workflow_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_wc_workflow"))
    @ManyToOne(optional = false)
    private Workflow workflow;
    @JoinColumn(name = "estatus_actual_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_wc_estatus_actual"))
    @ManyToOne(optional = false)
    private Estatus estatusActual;
    @JoinColumn(name = "estatus_siguiente_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_wc_estatus_siguiente"))
    @ManyToOne(optional = false)
    private Estatus estatusSiguiente;

    public WorkflowConfig() {
    }

    public WorkflowConfig(Integer workflowId, Integer estatusActualId, Integer estatusSiguienteId) {
        this.workflowId = workflowId;
        this.estatusActualId = estatusActualId;
        this.estatusSiguienteId = estatusSiguienteId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Integer workflowId) {
        this.workflowId = workflowId;
    }

    public Integer getEstatusActualId() {
        return estatusActualId;
    }

    public void setEstatusActualId(Integer estatusActualId) {
        this.estatusActualId = estatusActualId;
    }

    public Integer getEstatusSiguienteId() {
        return estatusSiguienteId;
    }

    public void setEstatusSiguienteId(Integer estatusSiguienteId) {
        this.estatusSiguienteId = estatusSiguienteId;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
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

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public Estatus getEstatusActual() {
        return estatusActual;
    }

    public void setEstatusActual(Estatus estatusActual) {
        this.estatusActual = estatusActual;
    }

    public Estatus getEstatusSiguiente() {
        return estatusSiguiente;
    }

    public void setEstatusSiguiente(Estatus estatusSiguiente) {
        this.estatusSiguiente = estatusSiguiente;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.id);
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
        final WorkflowConfig other = (WorkflowConfig) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WorkflowConfig{" + "id=" + id + ", workflowId=" + workflowId + ", estatusActualId=" + estatusActualId + ", estatusSiguienteId=" + estatusSiguienteId + ", etiqueta=" + etiqueta + ", icono=" + icono + ", evento=" + evento + '}';
    }

}
