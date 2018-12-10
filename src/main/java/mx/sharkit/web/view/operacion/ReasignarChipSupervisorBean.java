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
import mx.sharkit.web.service.ChipService;
import mx.sharkit.web.service.UsuarioRolService;
import mx.sharkit.web.service.UsuarioService;
import mx.sharkit.web.service.ChipHistoricoService;
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
public class ReasignarChipSupervisorBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(ReasignarChipSupervisorBean.class.getName());

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
    private List<Usuario> subdistribuidoresNuevos;

    private List<Chip> chips;
    private List<Chip> chipsAsignados;
    private Chip chip;
    private Chip selectedChip;

    private Long idSupervisor;
    private Long idDistribuidor;
    private Long idSupervisorNuevo;

    private SSUserDetails userDetails;

    private String serie;
    private String serieFinal;
    private String tipoAsignacion;
    private Boolean distribuidorDisabled;
    private Boolean subdistribuidorDisabled;

    private ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();

    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        distribuidorDisabled = false;
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
            }

        }

//        obtenerChips();
    }

    public void search() {
        distribuidores = usuarioService.findByRolIdOrderByNombre(TypeCast.toLong(UsuarioRol.ID_ROL_COORDINADOR));
    }

    public void onCreateAsignacion() {
        try {
            if (Objects.equals(idSupervisor, idSupervisorNuevo)) {
                PFMessages.showMessageErrorBackEnd("Seleccione un Subdistribuidor diferente.");
                return;
            }
            if (StringUtils.equals("SSID", tipoAsignacion)) {
                Date fechaAsignacion = new Date();
                chip = chipService.findBySerie(serie);
                if (chip != null) {

                    if (Objects.equals(chip.getEstatusId(), Estatus.ID_ESTATUS_ASIGNADO_SUPERVISOR)
                            || (Objects.equals(chip.getEstatusId(), Estatus.ID_ESTATUS_EN_ALMACEN))) {

                        if (Objects.equals(chip.getUsuarioSupervisorId(), idSupervisor)) {

                            chip.setUsuarioSupervisorId(idSupervisorNuevo);
                            chip.setFechaAsignacionSupervisor(fechaAsignacion);
                            chip.setUsuarioVendedorId(null);
                            chip.setFechaAsignacionVendedor(null);
//                            chip.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_SUPERVISOR);
                            chip.setFechaEstatus(fechaAsignacion);
                            chip.setFechaUltModificacion(fechaAsignacion);

                            historicoEstatus = new ChipHistoricoEstatus();
                            historicoEstatus.setEstatusId(chip.getEstatusId());
                            historicoEstatus.setFechaEstatus(fechaAsignacion);
                            historicoEstatus.setObservaciones("Reasignación a subdistribuidor: " + idSupervisorNuevo);
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
                        PFMessages.showMessageError("El chip con numero: " + serie + " no se encuentra en estatus: [Asignado a Subdistribuidor, En Almacén]");
                        System.out.println("El chip con numero: " + serie + " no se encuentra en estatus: [Asignado a Subdistribuidor, En Almacén]");
                    }

                } else {
                    PFMessages.showMessageError("El chip con numero: " + serie + " no ha sido registrado");
                    System.out.println("El chip no existe");
                }
            } else {
                Integer noActualizados = chipService.reasignarChipsSubdistribuidorByRango(serie, serieFinal,
                        idSupervisor, idSupervisorNuevo, userDetails.getUsername());
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
            dc.add(Restrictions.in("estatusId", new Integer[] {Estatus.ID_ESTATUS_ASIGNADO_SUPERVISOR, Estatus.ID_ESTATUS_EN_ALMACEN}));
            dc.add(Restrictions.eq("usuarioCoordinadorId", idDistribuidor));
            dc.add(Restrictions.eq("usuarioSupervisorId", idSupervisorNuevo));
            dc.addOrder(Order.asc("serie"));
            chips = chipService.findByCriteria(dc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDistribuidorChange() {
        if (idDistribuidor != null || idDistribuidor != 0) {
            subdistribuidores = usuarioService.findByRolIdAndPadreIdOrderByNombre(TypeCast.toLong(UsuarioRol.ID_ROL_SUPERVISOR), idDistribuidor);
        }
    }

    public void cleanSearch() {
        //idSupervisor = null;
        serie = null;
        serieFinal = null;
        if (chips != null && !chips.isEmpty()) {
            chips.clear();
        }
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
