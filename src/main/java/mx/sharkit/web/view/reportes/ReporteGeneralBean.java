package mx.sharkit.web.view.reportes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.Estatus;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.model.UsuarioRol;
import mx.sharkit.web.security.SSUserDetails;
import mx.sharkit.web.service.ChipService;
import mx.sharkit.web.service.UsuarioRolService;
import mx.sharkit.web.service.UsuarioService;
import mx.sharkit.web.view.util.PFMessages;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author aalquisira
 */
@Setter
@Getter
@Named
@ViewScoped
public class ReporteGeneralBean implements Serializable {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRolService usuarioRolService;

    @Autowired
    private ChipService chipService;

    private List<Estatus> estatus;
    private List<Estatus> estatusPortabilidad;
    private List<Estatus> estatusProceso;

    private List<Usuario> currents;
    private List<Usuario> vendedores;
    private Usuario current;
    private Usuario selectedCurrent;

    private List<Chip> chips;
    private Chip chip;
    private Chip selectedChip;

    private SSUserDetails userDetails;

    private Date fechaInicio;
    private Date fechaFin;
    private Date fechaAsignacionSupervisor;
    private Date fechaAsignacionVendedor;
    private Date fechaAsignacionMayorista;
    private Date fechaEstatusPortabilidad;
    private Date fechaEstatusProceso;
    private Date fechaVenta;

    private String serie;
    private String nip;

    private Long idVendedor;

    private Integer estatusInicial;
    private Integer estatusPo;
    private Integer estatusPro;

    private SimpleDateFormat formatFechaBasica = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        current = new Usuario();
        obtenerEstatusInicial();
        obtenerEstatusPortabilidad();
        obtenerEstatusProceso();
        obtenerVendedor();
//        search();
    }

    public void search() {
        try {
            DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
            //dc.add(Restrictions.isNotNull("estatusPortabilidadId"));
            chips = chipService.findByCriteria(dc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void obtenerVendedor() {
        try {
            List<UsuarioRol> userRol = new ArrayList<>();
            DetachedCriteria dc = DetachedCriteria.forClass(UsuarioRol.class);
            dc.add(Restrictions.eq("rolId", new Long(UsuarioRol.ID_ROL_VENDEDOR)));
            dc.createAlias("usuario", "usuario");
            dc.add(Restrictions.eq("usuario.activo", 1));
            userRol = usuarioRolService.findByCriteria(dc);
            vendedores = new ArrayList<>();
            for (UsuarioRol usuarioRol : userRol) {
                vendedores.add(usuarioRol.getUsuario());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void obtenerChips() {
        try {

            DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
            if (fechaInicio != null && fechaFin != null) {
                if (fechaInicio.after(fechaFin)) {
                    PFMessages.showMessageValidacion("Fecha inicio no puede ser mayor que fecha fin");
                    return;
                }
                dc.add(Restrictions.between("fecha", fechaInicio, fechaFin));
            }
            if (!StringUtils.isBlank(serie)) {
                dc.add(Restrictions.eq("serie", serie));
            }
            if (idVendedor != null && idVendedor > 0) {
                dc.add(Restrictions.eq("usuarioVendedorId", idVendedor));
            }
            if (!StringUtils.isBlank(nip)) {
                dc.add(Restrictions.eq("nip", nip));
            }
            if (estatusInicial != null && estatusInicial > 0) {
                dc.add(Restrictions.eq("estatusId", estatusInicial));
            }
            if (estatusPo != null && estatusPo > 0) {
                dc.add(Restrictions.eq("estatusPortabilidadId", estatusPo));
            }
            if (estatusPro != null && estatusPro > 0) {
                dc.add(Restrictions.eq("estatusProcesoId", estatusPro));
            }
            if (fechaAsignacionSupervisor != null) {
                String fInicial = formatFechaBasica.format(fechaAsignacionSupervisor) + " 00:00:00";
                String fInicialF = formatFechaBasica.format(fechaAsignacionSupervisor) + " 23:59:59";
                dc.add(Restrictions.between("fechaAsignacionSupervisor", formatFecha.parse(fInicial), formatFecha.parse(fInicialF)));
            }
            if (fechaAsignacionVendedor != null) {
                String fVend = formatFechaBasica.format(fechaAsignacionVendedor) + " 00:00:00";
                String fVendF = formatFechaBasica.format(fechaAsignacionVendedor) + " 23:59:59";
                dc.add(Restrictions.between("fechaAsignacionVendedor", formatFecha.parse(fVend), formatFecha.parse(fVendF)));
            }
            if (fechaAsignacionMayorista != null) {
                String fMay = formatFechaBasica.format(fechaAsignacionMayorista) + " 00:00:00";
                String fMayF = formatFechaBasica.format(fechaAsignacionMayorista) + " 23:59:59";
                dc.add(Restrictions.between("fechaAsignacionMayorista", formatFecha.parse(fMay), formatFecha.parse(fMayF)));
            }
            if (fechaEstatusPortabilidad != null) {
                String fPortabilidad = formatFechaBasica.format(fechaEstatusPortabilidad) + " 00:00:00";
                String fPortabilidadF = formatFechaBasica.format(fechaEstatusPortabilidad) + " 23:59:59";
                dc.add(Restrictions.between("fechaEstPortabilidad", formatFecha.parse(fPortabilidad), formatFecha.parse(fPortabilidadF)));
            }
            if (fechaEstatusProceso != null) {
                String fProceso = formatFechaBasica.format(fechaEstatusProceso) + " 00:00:00";
                String fProcesoF = formatFechaBasica.format(fechaEstatusProceso) + " 23:59:59";
                dc.add(Restrictions.between("fechaEstProceso", formatFecha.parse(fProceso), formatFecha.parse(fProcesoF)));
            }
            if (fechaVenta != null) {
                String fVenta = formatFechaBasica.format(fechaVenta) + " 00:00:00";
                String fVentaF = formatFechaBasica.format(fechaVenta) + " 23:59:59";
                dc.add(Restrictions.between("fechaVenta", formatFecha.parse(fVenta), formatFecha.parse(fVentaF)));
            }
            if (hasRole("ROLE_SUPERVISOR")) {
                Long idDistribuidor = userDetails.getUser().getId();
                dc.add(Restrictions.eq("usuarioSupervisorId", idDistribuidor));
            }
            chips = chipService.findByCriteria(dc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void obtenerEstatusInicial() {
        DetachedCriteria dc = DetachedCriteria.forClass(Estatus.class);
        dc.add(Restrictions.eq("tipoEstatusId", Estatus.TIPO_ESTATUS_INICIAL));
        estatus = chipService.findByCriteria(dc);
    }

    public void obtenerEstatusPortabilidad() {
        DetachedCriteria dc = DetachedCriteria.forClass(Estatus.class);
        dc.add(Restrictions.eq("tipoEstatusId", Estatus.TIPO_ESTATUS_PORTABILIDAD));
        estatusPortabilidad = chipService.findByCriteria(dc);
    }

    public void obtenerEstatusProceso() {
        DetachedCriteria dc = DetachedCriteria.forClass(Estatus.class);
        dc.add(Restrictions.eq("tipoEstatusId", Estatus.TIPO_ESTATUS_PROCESO));
        estatusProceso = chipService.findByCriteria(dc);
    }

    protected boolean hasRole(String role) {
        // get security context from thread local
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return false;
        }

        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return false;
        }

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if (role.equals(auth.getAuthority())) {
                return true;
            }
        }

        return false;
    }

}
