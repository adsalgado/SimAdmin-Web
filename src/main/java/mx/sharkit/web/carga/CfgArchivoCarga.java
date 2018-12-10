package mx.sharkit.web.carga;

import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author asalgado
 */
@XmlRootElement
public class CfgArchivoCarga implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer parentId;
    private Integer tipoParametroId;
    private String claveArchivo;
    private String descripcion;
    private String encabezados;
    private Integer filaInicio;
    private Integer columnaInicio;
    private String procesaTodoValido;
    private String caracteristica5;
    private String configuracionArchivo;
        
    public CfgArchivoCarga() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getClaveArchivo() {
        return claveArchivo;
    }

    public void setClaveArchivo(String claveArchivo) {
        this.claveArchivo = claveArchivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEncabezados() {
        return encabezados;
    }

    public void setEncabezados(String encabezados) {
        this.encabezados = encabezados;
    }

    public Integer getFilaInicio() {
        return filaInicio;
    }

    public void setFilaInicio(Integer filaInicio) {
        this.filaInicio = filaInicio;
    }

    public Integer getColumnaInicio() {
        return columnaInicio;
    }

    public void setColumnaInicio(Integer columnaInicio) {
        this.columnaInicio = columnaInicio;
    }

    public String getProcesaTodoValido() {
        return procesaTodoValido;
    }

    public void setProcesaTodoValido(String procesaTodoValido) {
        this.procesaTodoValido = procesaTodoValido;
    }

    public String getCaracteristica5() {
        return caracteristica5;
    }

    public void setCaracteristica5(String caracteristica5) {
        this.caracteristica5 = caracteristica5;
    }

    public String getConfiguracionArchivo() {
        return configuracionArchivo;
    }

    public void setConfiguracionArchivo(String configuracionArchivo) {
        this.configuracionArchivo = configuracionArchivo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final CfgArchivoCarga other = (CfgArchivoCarga) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CfgArchivoCarga{" + "id=" + id + ", parentId=" + parentId + ", tipoParametroId=" + tipoParametroId + ", claveArchivo=" + claveArchivo + ", descripcion=" + descripcion + ", encabezados=" + encabezados + ", filaInicio=" + filaInicio + ", columnaInicio=" + columnaInicio + ", procesaTodoValido=" + procesaTodoValido + ", caracteristica5=" + caracteristica5 + ", configuracionArchivo=" + configuracionArchivo + '}';
    }

}
