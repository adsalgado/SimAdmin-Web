package mx.sharkit.web.view.reportes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import mx.sharkit.web.view.util.TypeCast;
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
public class ReporteVentasBean implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(ReporteVentasBean.class.getName());

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
    private List<Chip> selectedChips;
    private Chip chip;
    private Chip selectedChip;
    private List<Chip> chipsAsignados;
    private List<Chip> chipsCerrados;

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
    private Long idSupervisor;

    private Integer estatusInicial;
    private Integer estatusPo;
    private Integer estatusPro;

    private BigDecimal totalVenta;

    private SimpleDateFormat formatFechaBasica = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        current = new Usuario();

        if (hasRole("ROLE_SUPERVISOR")) {
            Usuario usSubdistribuidor = userDetails.getUser();
            idSupervisor = usSubdistribuidor.getId();
            obtenerVendedor();
        }

//        search();
    }

    public void search() {
        try {
            DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
            //dc.add(Restrictions.isNotNull("estatusPortabilidadId"));
            chips = chipService.findByCriteria(dc);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error interno, si continua consulte a su administrador", ex);
        }
    }

    public void cierreChips() {
        try {
            if (selectedChips == null || selectedChips.isEmpty()) {
                PFMessages.showMessageError("No se seleccionó ningún chip");
                return;
            }
            chipService.cierreChipsVenta(selectedChips, totalVenta, idSupervisor, idVendedor, fechaInicio, fechaFin, userDetails.getUsername());
            cleanSearch();
            PFMessages.showMessageInfo("La operación se realizó correctamente.");

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error interno, si continua consulte a su administrador", ex);
            PFMessages.showMessageError(ex.getMessage());
        }
    }
    
    public void calculaTotales() {
        totalVenta = BigDecimal.ZERO;
        for (Chip chip1 : selectedChips) {
            if (chip1.getCosto() != null) {
                totalVenta = totalVenta.add(chip1.getCosto());
            }
        }
        LOG.log(Level.INFO, "totalVenta: {0}", totalVenta);
    }

    public void obtenerVendedor() {
        try {
            List<UsuarioRol> userRol;
            DetachedCriteria dc = DetachedCriteria.forClass(UsuarioRol.class);
            dc.add(Restrictions.eq("rolId", new Long(UsuarioRol.ID_ROL_VENDEDOR)));
            dc.createAlias("usuario", "usuario");
            dc.add(Restrictions.eq("usuario.usuarioPadreId", idSupervisor));
            dc.add(Restrictions.eq("usuario.activo", 1));
            userRol = usuarioRolService.findByCriteria(dc);
            vendedores = new ArrayList<>();
            for (UsuarioRol usuarioRol : userRol) {
                vendedores.add(usuarioRol.getUsuario());
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error interno, si continua consulte a su administrador", ex);
        }
    }

    public void obtenerChips() {
        chips = obtenerChipsByEstatus(Estatus.ID_ESTATUS_VENDIDO);
        chipsAsignados = obtenerChipsByEstatus(Estatus.ID_ESTATUS_ASIGNADO_VENDEDOR);
        chipsCerrados = obtenerChipsByEstatus(Estatus.ID_ESTATUS_CERRADO);
    }

    public List<Chip> obtenerChipsByEstatus(Integer estatus) {

        List<Chip> chps = new ArrayList<>();
        DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
        if (fechaInicio != null && fechaFin != null) {
            if (fechaInicio.after(fechaFin)) {
                PFMessages.showMessageValidacion("Fecha inicio no puede ser mayor que fecha fin");
                return chps;
            }
            String fInicial = TypeCast.toString(fechaInicio, "yyyy-MM-dd") + " 00:00:00";
            String fInicialF = TypeCast.toString(fechaFin, "yyyy-MM-dd") + " 23:59:59";
            dc.add(Restrictions.between("fechaUltModificacion",
                    TypeCast.toDate(fInicial, "yyyy-MM-dd HH:mm:ss"),
                    TypeCast.toDate(fInicialF, "yyyy-MM-dd HH:mm:ss")));
        }
        if (idVendedor != null && idVendedor > 0) {
            dc.add(Restrictions.eq("usuarioVendedorId", idVendedor));
        }
        dc.add(Restrictions.eq("estatusId", estatus));
        return chipService.findByCriteria(dc);

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

    private void cleanSearch() {
        idVendedor = null;
        fechaInicio = null;
        fechaFin = null;
        totalVenta = null;
        if (chips != null && !chips.isEmpty()) {
            chips.clear();
        }
        if (selectedChips != null && !selectedChips.isEmpty()) {
            selectedChips.clear();
        }
        if (chipsCerrados != null && !chipsCerrados.isEmpty()) {
            chipsCerrados.clear();
        }
        if (chipsAsignados != null && !chipsAsignados.isEmpty()) {
            chipsAsignados.clear();
        }
    }

}
