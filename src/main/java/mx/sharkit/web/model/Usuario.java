package mx.sharkit.web.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Adrián Salgado
 */
@Entity
@Table(name = "cfg_usuario")
@XmlRootElement
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "paterno")
    private String paterno;
    @Column(name = "materno")
    private String materno;
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "activo")
    private int activo;
    @Basic(optional = false)
    @Column(name = "user_name")
    private String userName;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Column(name = "fecha_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Column(name = "fecha_ult_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltModificacion;
    @Column(name = "no_accesos")
    private Integer noAccesos;
    @Column(name = "no_intentos")
    private Integer noIntentos;
    @Column(name = "cuenta_bloqueada")
    private String cuentaBloqueada;
    @Column(name = "fecha_expiracion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaExpiracion;
    
    @Column(name = "empresa_id")
    private Integer empresaId;
    
    @JoinColumn(name = "empresa_id", referencedColumnName = "id", insertable = false, updatable = false, 
            foreignKey = @ForeignKey(name="fk_usuario_empresa"))
    @ManyToOne(optional = true)
    private Empresa empresa;

    @Column(name = "sucursal_id")
    private Integer sucursalId;
    
    @JoinColumn(name = "sucursal_id", referencedColumnName = "id", insertable = false, updatable = false, 
            foreignKey = @ForeignKey(name="fk_usuario_sucursal"))
    @ManyToOne(optional = true)
    private Sucursal sucursal;

    @Basic(optional = false)
    @Column(name = "usuario_padre_id")
    private Long usuarioPadreId;

    @JoinColumn(name = "usuario_padre_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_usuario_padre_id"))
    @ManyToOne(optional = true)
    private Usuario usuarioPadre;

    @Basic(optional = true)
    @Column(name = "pais_id")
    private Integer paisId;

    @JoinColumn(name = "pais_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_cfg_usuario_pais"))
    @ManyToOne
    private Pais pais;

    @Basic(optional = true)
    @Column(name = "zona_id")
    private Integer zonaId;

    @JoinColumn(name = "zona_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_cfg_usuario_zona"))
    @ManyToOne
    private Zona zona;

    @Basic(optional = true)
    @Column(name = "cuidad_id")
    private Integer cuidadId;

    @JoinColumn(name = "cuidad_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name="fk_cfg_usuario_cuidad"))
    @ManyToOne
    private Cuidad cuidad;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioId")
    private List<UsuarioRol> cfgUsuarioRolList;

    @OneToMany(mappedBy = "usuarioPadreId")
    private List<Usuario> cfgUsuarioList;

    public Usuario() {
    }

    public Usuario(Long id) {
        this.id = id;
    }

    public Usuario(Long id, String nombre, String paterno, int activo, String userName, String password) {
        this.id = id;
        this.nombre = nombre;
        this.paterno = paterno;
        this.activo = activo;
        this.userName = userName;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPaterno() {
        return paterno;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Integer getNoAccesos() {
        return noAccesos;
    }

    public void setNoAccesos(Integer noAccesos) {
        this.noAccesos = noAccesos;
    }

    public Integer getNoIntentos() {
        return noIntentos;
    }

    public void setNoIntentos(Integer noIntentos) {
        this.noIntentos = noIntentos;
    }

    public String getCuentaBloqueada() {
        return cuentaBloqueada;
    }

    public void setCuentaBloqueada(String cuentaBloqueada) {
        this.cuentaBloqueada = cuentaBloqueada;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    @XmlTransient
    public List<UsuarioRol> getUsuarioRolList() {
        return cfgUsuarioRolList;
    }

    public void setUsuarioRolList(List<UsuarioRol> cfgUsuarioRolList) {
        this.cfgUsuarioRolList = cfgUsuarioRolList;
    }

    public Usuario getUsuarioPadre() {
        return usuarioPadre;
    }

    public void setUsuarioPadre(Usuario usuarioPadre) {
        this.usuarioPadre = usuarioPadre;
    }

    public Long getUsuarioPadreId() {
        return usuarioPadreId;
    }

    public void setUsuarioPadreId(Long usuarioPadreId) {
        this.usuarioPadreId = usuarioPadreId;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Integer getPaisId() {
        return paisId;
    }

    public void setPaisId(Integer paisId) {
        this.paisId = paisId;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Integer getZonaId() {
        return zonaId;
    }

    public void setZonaId(Integer zonaId) {
        this.zonaId = zonaId;
    }

    public Cuidad getCuidad() {
        return cuidad;
    }

    public void setCuidad(Cuidad cuidad) {
        this.cuidad = cuidad;
    }

    public Integer getCuidadId() {
        return cuidadId;
    }

    public void setCuidadId(Integer cuidadId) {
        this.cuidadId = cuidadId;
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

    public Integer getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(Integer sucursalId) {
        this.sucursalId = sucursalId;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public List<Usuario> getUsuarioList() {
        return cfgUsuarioList;
    }

    public void setUsuarioList(List<Usuario> cfgUsuarioList) {
        this.cfgUsuarioList = cfgUsuarioList;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mx.sharktech.pojos.Usuario[ id=" + id + " ]";
    }

}
