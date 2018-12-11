package mx.sharkit.web.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 *
 * @author jlopez
 */
@Entity
@Table(name = "ptb_chip")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Chip.findAll", query = "SELECT c FROM Chip c")
    , @NamedQuery(name = "Chip.findById", query = "SELECT c FROM Chip c WHERE c.id = :id")
    , @NamedQuery(name = "Chip.findBySerie", query = "SELECT c FROM Chip c WHERE c.serie = :serie")
    , @NamedQuery(name = "Chip.findByFecha", query = "SELECT c FROM Chip c WHERE c.fecha = :fecha")
    , @NamedQuery(name = "Chip.findByDocumento", query = "SELECT c FROM Chip c WHERE c.documento = :documento")
    , @NamedQuery(name = "Chip.findByReferencia", query = "SELECT c FROM Chip c WHERE c.referencia = :referencia")
    , @NamedQuery(name = "Chip.findByArticulo", query = "SELECT c FROM Chip c WHERE c.articulo = :articulo")
    , @NamedQuery(name = "Chip.findByDescripcion", query = "SELECT c FROM Chip c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "Chip.findByModelo", query = "SELECT c FROM Chip c WHERE c.modelo = :modelo")
    , @NamedQuery(name = "Chip.findByCantidad", query = "SELECT c FROM Chip c WHERE c.cantidad = :cantidad")
    , @NamedQuery(name = "Chip.findByCostoCompra", query = "SELECT c FROM Chip c WHERE c.costoCompra = :costoCompra")
    , @NamedQuery(name = "Chip.findByNombreCliente", query = "SELECT c FROM Chip c WHERE c.nombreCliente = :nombreCliente")
    , @NamedQuery(name = "Chip.findByDn", query = "SELECT c FROM Chip c WHERE c.dn = :dn")
    , @NamedQuery(name = "Chip.findByNip", query = "SELECT c FROM Chip c WHERE c.nip = :nip")
    , @NamedQuery(name = "Chip.findByUsuarioSupervisor", query = "SELECT c FROM Chip c WHERE c.usuarioSupervisor = :usuarioSupervisor")
    , @NamedQuery(name = "Chip.findByFechaAsignacionSupervisor", query = "SELECT c FROM Chip c WHERE c.fechaAsignacionSupervisor = :fechaAsignacionSupervisor")
    , @NamedQuery(name = "Chip.findByUsuarioVendedor", query = "SELECT c FROM Chip c WHERE c.usuarioVendedor = :usuarioVendedor")
    , @NamedQuery(name = "Chip.findByFechaAsignacionVendedor", query = "SELECT c FROM Chip c WHERE c.fechaAsignacionVendedor = :fechaAsignacionVendedor")
    , @NamedQuery(name = "Chip.findByFechaVenta", query = "SELECT c FROM Chip c WHERE c.fechaVenta = :fechaVenta")
    , @NamedQuery(name = "Chip.findByFechaRecarga", query = "SELECT c FROM Chip c WHERE c.fechaRecarga = :fechaRecarga")})
public class Chip implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @Column(name = "serie")
    private String serie;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "documento")
    private String documento;
    @Column(name = "referencia")
    private String referencia;
    @Column(name = "articulo")
    private String articulo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "modelo")
    private String modelo;
    @Column(name = "cantidad")
    private Integer cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "costo_compra")
    private BigDecimal costoCompra;
    @Column(name = "nombre_cliente")
    private String nombreCliente;
    @Column(name = "dn")
    private String dn;
    @Column(name = "nip")
    private String nip;
    @Column(name = "dn_temporal")
    private String dnTemporal;
    @Column(name = "monto_recarga")
    private BigDecimal montoRecarga;
    @Column(name = "observacion_error_proceso")
    private String observacionErrorProceso;
    @Column(name = "folio")
    private Integer folio;
    @Column(name = "tipo_venta_id")
    private Integer tipoVentaId;

    @Column(name = "costo")
    private BigDecimal costo;

    @Column(name = "fecha_asignacion_supervisor")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAsignacionSupervisor;

    @Column(name = "fecha_asignacion_mayorista")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAsignacionMayorista;

    @Column(name = "fecha_asignacion_vendedor")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAsignacionVendedor;

    @Column(name = "fecha_recarga")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRecarga;

    @Column(name = "fecha_estatus")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEstatus;

    @Column(name = "fecha_est_portabilidad")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEstPortabilidad;

    @Column(name = "fecha_est_proceso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEstProceso;

    @Column(name = "fecha_ult_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltModificacion;

    @Column(name = "fecha_venta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVenta;

    @JoinColumn(name = "compania_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_chip_compania"))
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(optional = true)
    private Compania compania;

    @Basic(optional = true)
    @Column(name = "compania_id")
    private Integer companiaId;

    @JoinColumn(name = "id_usuario_vendedor", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_chip_usuario_vendedor"))
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(optional = true)
    private Usuario usuarioVendedor;
    @Basic(optional = true)
    @Column(name = "id_usuario_vendedor")
    private Long usuarioVendedorId;

    @JoinColumn(name = "id_usuario_supervisor", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_chip_usuario_supervisor"))
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(optional = true)
    private Usuario usuarioSupervisor;
    @Basic(optional = true)
    @Column(name = "id_usuario_supervisor")
    private Long usuarioSupervisorId;

    @JoinColumn(name = "id_usuario_coordinador", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_chip_usuario_coordinador"))
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(optional = true)
    private Usuario usuarioCoordinador;
    @Basic(optional = true)
    @Column(name = "id_usuario_coordinador")
    private Long usuarioCoordinadorId;

    @JoinColumn(name = "id_usuario_mayorista", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_chip_usuario_mayorista"))
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(optional = true)
    private Usuario usuarioMayorista;
    @Basic(optional = true)
    @Column(name = "id_usuario_mayorista")
    private Long usuarioMayoristaId;

    @JoinColumn(name = "estatus_portabilidad", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_chip_estatus_portabilidad"))
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(optional = true)
    private Estatus estatusPortabilidad;
    @Basic(optional = false)
    @Column(name = "estatus_portabilidad")
    private Integer estatusPortabilidadId;

    @JoinColumn(name = "estatus_proceso", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_chip_estatus_proceso"))
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(optional = true)
    private Estatus estatusProceso;
    @Basic(optional = false)
    @Column(name = "estatus_proceso")
    private Integer estatusProcesoId;

    @JoinColumn(name = "estatus_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_chip_usuario_estatus"))
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(optional = true)
    private Estatus estatus;
    @Basic(optional = false)
    @Column(name = "estatus_id")
    private Integer estatusId;

    @Column(name = "latitud_venta")
    private String latitudVenta;

    @Column(name = "longitud_venta")
    private String longitudVenta;

    @JoinColumn(name = "archivo_digital_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_chip_archivo_digital"))
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(optional = true)
    private ArchivoDigital archivoDigital;

    @Basic(optional = true)
    @Column(name = "archivo_digital_id")
    private Long archivoDigitalId;

    @Basic(optional = true)
    @Column(name = "cierre_chip_id")
    private Long cierreChipId;

    @JoinColumn(name = "cierre_chip_id", referencedColumnName = "id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_chip_cierre"))
    @ManyToOne(optional = true)
    private CierreChip cierreChip;

    @Column(name = "producto_id")
    private Integer productoId;

    @Column(name = "consecutivo")
    private Long consecutivo;

    @Column(name = "consecutivo_total")
    private Long consecutivoTotal;

    @Column(name = "fecha_escaneo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEscaneo;

    @Column(name = "caja", length = 64)
    private String caja;

    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrega;

    @Column(name = "orden", length = 32)
    private String orden;

    @Column(name = "factura", length = 32)
    private String factura;

    public Chip() {
    }

    public Chip(Long id) {
        this.id = id;
    }

    public Chip(Long id, String serie) {
        this.id = id;
        this.serie = serie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getCostoCompra() {
        return costoCompra;
    }

    public void setCostoCompra(BigDecimal costoCompra) {
        this.costoCompra = costoCompra;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getDnTemporal() {
        return dnTemporal;
    }

    public void setDnTemporal(String dnTemporal) {
        this.dnTemporal = dnTemporal;
    }

    public BigDecimal getMontoRecarga() {
        return montoRecarga;
    }

    public void setMontoRecarga(BigDecimal montoRecarga) {
        this.montoRecarga = montoRecarga;
    }

    public String getObservacionErrorProceso() {
        return observacionErrorProceso;
    }

    public void setObservacionErrorProceso(String observacionErrorProceso) {
        this.observacionErrorProceso = observacionErrorProceso;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public Integer getTipoVentaId() {
        return tipoVentaId;
    }

    public void setTipoVentaId(Integer tipoVentaId) {
        this.tipoVentaId = tipoVentaId;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public Date getFechaAsignacionSupervisor() {
        return fechaAsignacionSupervisor;
    }

    public void setFechaAsignacionSupervisor(Date fechaAsignacionSupervisor) {
        this.fechaAsignacionSupervisor = fechaAsignacionSupervisor;
    }

    public Date getFechaAsignacionMayorista() {
        return fechaAsignacionMayorista;
    }

    public void setFechaAsignacionMayorista(Date fechaAsignacionMayorista) {
        this.fechaAsignacionMayorista = fechaAsignacionMayorista;
    }

    public Date getFechaAsignacionVendedor() {
        return fechaAsignacionVendedor;
    }

    public void setFechaAsignacionVendedor(Date fechaAsignacionVendedor) {
        this.fechaAsignacionVendedor = fechaAsignacionVendedor;
    }

    public Date getFechaRecarga() {
        return fechaRecarga;
    }

    public void setFechaRecarga(Date fechaRecarga) {
        this.fechaRecarga = fechaRecarga;
    }

    public Date getFechaEstatus() {
        return fechaEstatus;
    }

    public void setFechaEstatus(Date fechaEstatus) {
        this.fechaEstatus = fechaEstatus;
    }

    public Date getFechaEstPortabilidad() {
        return fechaEstPortabilidad;
    }

    public void setFechaEstPortabilidad(Date fechaEstPortabilidad) {
        this.fechaEstPortabilidad = fechaEstPortabilidad;
    }

    public Date getFechaEstProceso() {
        return fechaEstProceso;
    }

    public void setFechaEstProceso(Date fechaEstProceso) {
        this.fechaEstProceso = fechaEstProceso;
    }

    public Date getFechaUltModificacion() {
        return fechaUltModificacion;
    }

    public void setFechaUltModificacion(Date fechaUltModificacion) {
        this.fechaUltModificacion = fechaUltModificacion;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Compania getCompania() {
        return compania;
    }

    public void setCompania(Compania compania) {
        this.compania = compania;
    }

    public Integer getCompaniaId() {
        return companiaId;
    }

    public void setCompaniaId(Integer companiaId) {
        this.companiaId = companiaId;
    }

    public Usuario getUsuarioVendedor() {
        return usuarioVendedor;
    }

    public void setUsuarioVendedor(Usuario usuarioVendedor) {
        this.usuarioVendedor = usuarioVendedor;
    }

    public Long getUsuarioVendedorId() {
        return usuarioVendedorId;
    }

    public void setUsuarioVendedorId(Long usuarioVendedorId) {
        this.usuarioVendedorId = usuarioVendedorId;
    }

    public Usuario getUsuarioSupervisor() {
        return usuarioSupervisor;
    }

    public void setUsuarioSupervisor(Usuario usuarioSupervisor) {
        this.usuarioSupervisor = usuarioSupervisor;
    }

    public Long getUsuarioSupervisorId() {
        return usuarioSupervisorId;
    }

    public void setUsuarioSupervisorId(Long usuarioSupervisorId) {
        this.usuarioSupervisorId = usuarioSupervisorId;
    }

    public Usuario getUsuarioCoordinador() {
        return usuarioCoordinador;
    }

    public void setUsuarioCoordinador(Usuario usuarioCoordinador) {
        this.usuarioCoordinador = usuarioCoordinador;
    }

    public Long getUsuarioCoordinadorId() {
        return usuarioCoordinadorId;
    }

    public void setUsuarioCoordinadorId(Long usuarioCoordinadorId) {
        this.usuarioCoordinadorId = usuarioCoordinadorId;
    }

    public Usuario getUsuarioMayorista() {
        return usuarioMayorista;
    }

    public void setUsuarioMayorista(Usuario usuarioMayorista) {
        this.usuarioMayorista = usuarioMayorista;
    }

    public Long getUsuarioMayoristaId() {
        return usuarioMayoristaId;
    }

    public void setUsuarioMayoristaId(Long usuarioMayoristaId) {
        this.usuarioMayoristaId = usuarioMayoristaId;
    }

    public Estatus getEstatusPortabilidad() {
        return estatusPortabilidad;
    }

    public void setEstatusPortabilidad(Estatus estatusPortabilidad) {
        this.estatusPortabilidad = estatusPortabilidad;
    }

    public Integer getEstatusPortabilidadId() {
        return estatusPortabilidadId;
    }

    public void setEstatusPortabilidadId(Integer estatusPortabilidadId) {
        this.estatusPortabilidadId = estatusPortabilidadId;
    }

    public Estatus getEstatusProceso() {
        return estatusProceso;
    }

    public void setEstatusProceso(Estatus estatusProceso) {
        this.estatusProceso = estatusProceso;
    }

    public Integer getEstatusProcesoId() {
        return estatusProcesoId;
    }

    public void setEstatusProcesoId(Integer estatusProcesoId) {
        this.estatusProcesoId = estatusProcesoId;
    }

    public Estatus getEstatus() {
        return estatus;
    }

    public void setEstatus(Estatus estatus) {
        this.estatus = estatus;
    }

    public Integer getEstatusId() {
        return estatusId;
    }

    public void setEstatusId(Integer estatusId) {
        this.estatusId = estatusId;
    }

    public String getLatitudVenta() {
        return latitudVenta;
    }

    public void setLatitudVenta(String latitudVenta) {
        this.latitudVenta = latitudVenta;
    }

    public String getLongitudVenta() {
        return longitudVenta;
    }

    public void setLongitudVenta(String longitudVenta) {
        this.longitudVenta = longitudVenta;
    }

    public ArchivoDigital getArchivoDigital() {
        return archivoDigital;
    }

    public void setArchivoDigital(ArchivoDigital archivoDigital) {
        this.archivoDigital = archivoDigital;
    }

    public Long getArchivoDigitalId() {
        return archivoDigitalId;
    }

    public void setArchivoDigitalId(Long archivoDigitalId) {
        this.archivoDigitalId = archivoDigitalId;
    }

    public Long getCierreChipId() {
        return cierreChipId;
    }

    public void setCierreChipId(Long cierreChipId) {
        this.cierreChipId = cierreChipId;
    }

    public CierreChip getCierreChip() {
        return cierreChip;
    }

    public void setCierreChip(CierreChip cierreChip) {
        this.cierreChip = cierreChip;
    }

    public Long getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(Long consecutivo) {
        this.consecutivo = consecutivo;
    }

    public Long getConsecutivoTotal() {
        return consecutivoTotal;
    }

    public void setConsecutivoTotal(Long consecutivoTotal) {
        this.consecutivoTotal = consecutivoTotal;
    }

    public Date getFechaEscaneo() {
        return fechaEscaneo;
    }

    public void setFechaEscaneo(Date fechaEscaneo) {
        this.fechaEscaneo = fechaEscaneo;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
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
        if (!(object instanceof Chip)) {
            return false;
        }
        Chip other = (Chip) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Chip{" + "id=" + id + ", serie=" + serie + ", fecha=" + fecha + ", documento=" + documento + ", referencia=" + referencia + ", articulo=" + articulo + ", descripcion=" + descripcion + ", modelo=" + modelo + ", cantidad=" + cantidad + ", costoCompra=" + costoCompra + ", nombreCliente=" + nombreCliente + ", dn=" + dn + ", nip=" + nip + ", dnTemporal=" + dnTemporal + ", montoRecarga=" + montoRecarga + ", fechaAsignacionSupervisor=" + fechaAsignacionSupervisor + ", fechaAsignacionMayorista=" + fechaAsignacionMayorista + ", fechaAsignacionVendedor=" + fechaAsignacionVendedor + ", fechaRecarga=" + fechaRecarga + ", fechaVenta=" + fechaVenta + ", compania=" + compania + ", companiaId=" + companiaId + ", usuarioVendedor=" + usuarioVendedor + ", usuarioVendedorId=" + usuarioVendedorId + ", usuarioSupervisor=" + usuarioSupervisor + ", usuarioSupervisorId=" + usuarioSupervisorId + ", usuarioCoordinador=" + usuarioCoordinador + ", usuarioCoordinadorId=" + usuarioCoordinadorId + ", usuarioMayorista=" + usuarioMayorista + ", usuarioMayoristaId=" + usuarioMayoristaId + ", estatusPortabilidad=" + estatusPortabilidad + ", estatusPortabilidadId=" + estatusPortabilidadId + ", estatusProceso=" + estatusProceso + ", estatusProcesoId=" + estatusProcesoId + ", estatus=" + estatus + ", estatusId=" + estatusId + ", latitudVenta=" + latitudVenta + ", longitudVenta=" + longitudVenta + ", chipHistoricoEstatusList=" + '}';
    }

}
