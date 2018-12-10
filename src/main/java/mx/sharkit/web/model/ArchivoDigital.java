package mx.sharkit.web.model;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "ptb_archivo_digital")
@XmlRootElement
public class ArchivoDigital implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "ruta")
    private String ruta;
    @Basic(optional = false)
    @Column(name = "nombre_archivo_real")
    private String nombreArchivoReal;
    @Basic(optional = false)
    @Column(name = "nombre_archivo_fisico")
    private String nombreArchivoFisico;
    @Basic(optional = false)
    @Column(name = "content_type")
    private String contentType;
    @Basic(optional = false)
    @Column(name = "content_size")
    private int contentSize;
    @Basic(optional = false)
    @Column(name = "fecha_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Basic(optional = false)
    @Column(name = "usuario_alta")
    private String usuarioAlta;
    @Column(name = "total_registros")
    private Integer totalRegistros;
    @Column(name = "total_actualizados")
    private Integer totalActualizados;

    public ArchivoDigital() {
    }

    public ArchivoDigital(Long id) {
        this.id = id;
    }

    public ArchivoDigital(Long id, String ruta, String nombreArchivoReal, String nombreArchivoFisico, String contentType, int contentSize, Date fechaAlta, String usuarioAlta) {
        this.id = id;
        this.ruta = ruta;
        this.nombreArchivoReal = nombreArchivoReal;
        this.nombreArchivoFisico = nombreArchivoFisico;
        this.contentType = contentType;
        this.contentSize = contentSize;
        this.fechaAlta = fechaAlta;
        this.usuarioAlta = usuarioAlta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getNombreArchivoReal() {
        return nombreArchivoReal;
    }

    public void setNombreArchivoReal(String nombreArchivoReal) {
        this.nombreArchivoReal = nombreArchivoReal;
    }

    public String getNombreArchivoFisico() {
        return nombreArchivoFisico;
    }

    public void setNombreArchivoFisico(String nombreArchivoFisico) {
        this.nombreArchivoFisico = nombreArchivoFisico;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getContentSize() {
        return contentSize;
    }

    public void setContentSize(int contentSize) {
        this.contentSize = contentSize;
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

    public Integer getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(Integer totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

    public Integer getTotalActualizados() {
        return totalActualizados;
    }

    public void setTotalActualizados(Integer totalActualizados) {
        this.totalActualizados = totalActualizados;
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
        if (!(object instanceof ArchivoDigital)) {
            return false;
        }
        ArchivoDigital other = (ArchivoDigital) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.sharkit.web.models.ArchivoDigital[ id=" + id + " ]";
    }
    
}
