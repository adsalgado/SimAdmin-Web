package mx.sharkit.web.view.operacion;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.ChipHistoricoEstatus;
import mx.sharkit.web.model.Estatus;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.model.UsuarioRol;
import mx.sharkit.web.security.SSUserDetails;
import mx.sharkit.web.service.ChipHistoricoService;
import mx.sharkit.web.service.ChipService;
import mx.sharkit.web.service.UsuarioRolService;
import mx.sharkit.web.service.UsuarioService;
import mx.sharkit.web.view.util.PFMessages;
import mx.sharkit.web.view.util.TypeCast;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
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
public class SalidaAlmacenPromotorBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(SalidaAlmacenPromotorBean.class.getName());

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRolService usuarioRolService;

    @Autowired
    private ChipService chipService;

    @Autowired
    private ChipHistoricoService chipHistoricoService;

    private List<Usuario> distribuidores;
    private List<Usuario> subdistribuidores;
    private List<Usuario> promotores;

    private List<Chip> chips;
    private List<Chip> chipsAsignados;
    private Chip chip;
    private Chip selectedChip;

    private Long idSupervisor;
    private Long idDistribuidor;
    private Long idVendedor;

    private SSUserDetails userDetails;

    private String serie;
    private String serieFinal;
    private String tipoAsignacion;
    private Boolean distribuidorDisabled;
    private Boolean subdistribuidorDisabled;
    private Boolean promotorDisabled;

    private ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();

    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        distribuidorDisabled = false;
        subdistribuidorDisabled = false;
        promotorDisabled = false;
        search();

        if (hasRole("ROLE_COORDINADOR")) {
            idDistribuidor = userDetails.getUser().getId();
            distribuidorDisabled = true;
            onDistribuidorChange();
        } else if (hasRole("ROLE_SUPERVISOR")) {
            Usuario usSubdistribuidor = userDetails.getUser();
            Usuario usDistribuidor = usSubdistribuidor.getUsuarioPadre();

            if (usDistribuidor != null) {
                idDistribuidor = usDistribuidor.getId();
                distribuidorDisabled = true;
                onDistribuidorChange();

                idSupervisor = usSubdistribuidor.getId();
                subdistribuidorDisabled = true;
                onSubDistribuidorChange();
            }

        } else if (hasRole("ROLE_VENDEDOR")) {
            Usuario usPromotor = userDetails.getUser();
            Usuario usSubdistribuidor = usPromotor.getUsuarioPadre();
            Usuario usDistribuidor = usSubdistribuidor.getUsuarioPadre();

            if (usDistribuidor != null) {
                idDistribuidor = usDistribuidor.getId();
                distribuidorDisabled = true;
                onDistribuidorChange();

                idSupervisor = usSubdistribuidor.getId();
                subdistribuidorDisabled = true;
                onSubDistribuidorChange();

                idVendedor = usPromotor.getId();
                promotorDisabled = true;
            }

        }

        obtenerChips();
    }

    public void search() {
        distribuidores = usuarioService.findByRolIdOrderByNombre(TypeCast.toLong(UsuarioRol.ID_ROL_COORDINADOR));
    }

    public void onCreateAsignacion() {
        try {
            if (StringUtils.equals("SSID", tipoAsignacion)) {
                Date fechaAsignacion = new Date();
                chip = chipService.findBySerie(serie);
                if (chip != null) {
                    if (Objects.equals(chip.getEstatusId(), Estatus.ID_ESTATUS_EN_ALMACEN)) {
                        if (Objects.equals(chip.getUsuarioSupervisorId(), idSupervisor)) {
                            chip.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_VENDEDOR);
                            chip.setUsuarioVendedorId(idVendedor);
                            chip.setFechaAsignacionVendedor(fechaAsignacion);
                            chip.setFechaEstatus(fechaAsignacion);
                            chip.setFechaUltModificacion(new Date());
                            //chipService.update(chip);
                            historicoEstatus = new ChipHistoricoEstatus();
                            historicoEstatus.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_VENDEDOR);
                            historicoEstatus.setFechaEstatus(fechaAsignacion);
                            historicoEstatus.setUsuarioEstatus(userDetails.getUser().getUserName());
                            if (chipHistoricoService.saveChipAndHistory(chip, historicoEstatus)) {
                                obtenerChips();
                                PFMessages.showMessageInfo("El chip con numero: " + serie + " ha sido asignado correctamente");
                                cleanSearch();
                            } else {
                                PFMessages.showMessageError("Error interno, si continua consulte a su administrador");
                                System.out.println("El chip ya fue asignado");
                            }
                        } else {
                            PFMessages.showMessageError("El chip: '" + serie + "' no pertenece al Subdistribuidor");
                            return;
                        }

                    } else {
                        PFMessages.showMessageError("El chip con numero: " + serie + " ya esta asignado o fue vendido");
                        System.out.println("El chip ya fue asignado");
                    }
                } else {
                    PFMessages.showMessageError("El chip con numero: " + serie + " no ha sido registrado");
                    System.out.println("El chip no existe");
                }
            } else {
                Integer noActualizados = chipService.asignarChipsVendedorByRango(serie, serieFinal,
                        idDistribuidor, idSupervisor, idVendedor, userDetails.getUsername());
                obtenerChips();
                cleanSearch();
                PFMessages.showMessageInfo("Se asignaron: " + noActualizados + " chips.");

            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error interno, si continua consulte a su administrador", e);
            PFMessages.showMessageError(e.getMessage());
        }
    }

    public void obtenerChips() {
        try {
            DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
            dc.add(Restrictions.eq("estatusId", Estatus.ID_ESTATUS_ASIGNADO_VENDEDOR));
            dc.add(Restrictions.eq("usuarioCoordinadorId", idDistribuidor));
            dc.add(Restrictions.eq("usuarioSupervisorId", idSupervisor));
            dc.add(Restrictions.eq("usuarioVendedorId", idVendedor));
            dc.addOrder(Order.asc("serie"));
            chips = chipService.findByCriteria(dc);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            PFMessages.showMessageError(e.getMessage());
        }
    }

    public void onDistribuidorChange() {
        if (idDistribuidor != null || idDistribuidor != 0) {
            subdistribuidores = usuarioService.findByRolIdAndPadreIdOrderByNombre(TypeCast.toLong(UsuarioRol.ID_ROL_SUPERVISOR), idDistribuidor);
        }
    }

    public void onSubDistribuidorChange() {
        if (idSupervisor != null || idSupervisor != 0) {
            promotores = usuarioService.findByRolIdAndPadreIdOrderByNombre(TypeCast.toLong(UsuarioRol.ID_ROL_VENDEDOR), idSupervisor);
        }
    }

    public void cleanSearch() {
        //idSupervisor = null;
        serie = null;
        serieFinal = null;
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
