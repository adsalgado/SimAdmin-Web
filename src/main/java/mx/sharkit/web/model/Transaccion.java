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
 * @author Adrián Salgado
 */
@Entity
@Table(name = "cfg_transaccion")
@XmlRootElement
public class Transaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha_transaccion")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date fechaTransaccion;
    @Basic(optional = false)
    @Column(name = "clave_transaccion")
    private String claveTransaccion;
    @Column(name = "detalle")
    private String detalle;
    @Basic(optional = true)
    @Column(name = "aplicacion_id")
    private Integer aplicacionId;
    @Basic(optional = false)
    @Column(name = "user_name")
    private String userName;

    public Transaccion() {
    }

    public Transaccion(Long id) {
        this.id = id;
    }

    public Transaccion(Date fechaTransaccion, String claveTransaccion, String detalle, String userName) {
        this.fechaTransaccion = fechaTransaccion;
        this.claveTransaccion = claveTransaccion;
        this.detalle = detalle;
        this.userName = userName;
    }

    public Transaccion(Date fechaTransaccion, String claveTransaccion, String detalle, Integer aplicacionId, String userName) {
        this.fechaTransaccion = fechaTransaccion;
        this.claveTransaccion = claveTransaccion;
        this.detalle = detalle;
        this.aplicacionId = aplicacionId;
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(Date fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public String getClaveTransaccion() {
        return claveTransaccion;
    }

    public void setClaveTransaccion(String claveTransaccion) {
        this.claveTransaccion = claveTransaccion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Integer getAplicacionId() {
        return aplicacionId;
    }

    public void setAplicacionId(Integer aplicacionId) {
        this.aplicacionId = aplicacionId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final Transaccion other = (Transaccion) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Transaccion{" + "id=" + id + ", fechaTransaccion=" + fechaTransaccion + ", claveTransaccion=" + claveTransaccion + ", userName=" + userName + '}';
    }
    
}
