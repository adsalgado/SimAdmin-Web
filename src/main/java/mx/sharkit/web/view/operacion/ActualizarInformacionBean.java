package mx.sharkit.web.view.operacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.Compania;
import mx.sharkit.web.model.Estatus;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.security.SSUserDetails;
import mx.sharkit.web.service.ChipService;
import mx.sharkit.web.service.UsuarioRolService;
import mx.sharkit.web.service.UsuarioService;
import mx.sharkit.web.view.util.PFMessages;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author aalquisira
 */
@Setter
@Getter
@Named
@ViewScoped
public class ActualizarInformacionBean implements Serializable {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRolService usuarioRolService;

    @Autowired
    private ChipService chipService;

    private List<Estatus> estatus;
    private List<Estatus> estatusPortabilidad;
    private List<Estatus> estatusProceso;

    private List<Compania> compania;

    private List<Usuario> currents;
    private Usuario current;
    private Usuario selectedCurrent;

    private List<Chip> chips;
    private Chip chip;
    private Chip selectedChip;

    private SSUserDetails userDetails;

    private String serie;
    private String dn;
    private String dnTemporal;
    private String nip;

    private Integer estatusActual;

    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        current = new Usuario();
        obtenerEstatusInicial();
        obtenerEstatusPortabilidad();
        obtenerEstatusProceso();
        obtenerCompania();
        search();
    }

    public void obtenerEstatusInicial() {
        List<Integer> estatusInicial = new ArrayList<Integer>();
        estatusInicial.add(Estatus.ID_ESTATUS_PERDIDO);
        estatusInicial.add(Estatus.ID_ESTATUS_CHIP_NUEVO);
        estatusInicial.add(Estatus.ID_ESTATUS_VENDIDO);
        estatusInicial.add(Estatus.ID_ESTATUS_ASIGNADO_SUPERVISOR);
        DetachedCriteria dc = DetachedCriteria.forClass(Estatus.class);
        dc.add(Restrictions.eq("tipoEstatusId", Estatus.TIPO_ESTATUS_INICIAL));
        dc.add(Restrictions.in("id", estatusInicial));
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

    public void obtenerCompania() {
        DetachedCriteria dc = DetachedCriteria.forClass(Compania.class);
        compania = chipService.findByCriteria(dc);
    }

    public void search() {
        try {
            DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
            dc.add(Restrictions.ne("estatusId", Estatus.ID_ESTATUS_CHIP_NUEVO));
            chips = chipService.findByCriteria(dc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void obtenerChips() {
        try {
            DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
            if (!StringUtils.isBlank(serie)) {
                dc.add(Restrictions.eq("serie", serie));
            }
            if (!StringUtils.isBlank(dn)) {
                dc.add(Restrictions.eq("dn", dn));
            }
            if (!StringUtils.isBlank(dnTemporal)) {
                dc.add(Restrictions.eq("dnTemporal", dnTemporal));
            }
            if (!StringUtils.isBlank(nip)) {
                dc.add(Restrictions.eq("nip", nip));
            }
            dc.add(Restrictions.ne("estatusId", Estatus.ID_ESTATUS_CHIP_NUEVO));
            chips = chipService.findByCriteria(dc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            Chip chipTemporal = (Chip) event.getObject();
            estatusActual = chipTemporal.getEstatusId();
            chipTemporal.setCompaniaId(chipTemporal.getCompania().getId());
            chipTemporal.setFechaUltModificacion(new Date());

            if (chipTemporal.getEstatus() != null) {
                chipTemporal.setEstatusId(chipTemporal.getEstatus().getId());
            }
            if (chipTemporal.getEstatusPortabilidad() != null) {
                chipTemporal.setEstatusPortabilidadId(chipTemporal.getEstatusPortabilidad().getId());
            }
            if (chipTemporal.getEstatusProceso() != null) {
                chipTemporal.setEstatusProcesoId(chipTemporal.getEstatusProceso().getId());
            }

            //ESTATUS       
            if (Objects.equals(chipTemporal.getEstatusId(), Estatus.ID_ESTATUS_PERDIDO)) {

                chipTemporal.setEstatusPortabilidadId(null);
                chipTemporal.setEstatusPortabilidad(null);
                chipTemporal.setFechaEstPortabilidad(null);
                chipTemporal.setEstatusProcesoId(null);
                chipTemporal.setEstatusProceso(null);
                chipTemporal.setFechaEstProceso(null);
                // se asigna la fecha del cambio del estatus
                chipTemporal.setFechaEstatus(new Date());
            }

            if (Objects.equals(chipTemporal.getEstatusId(), Estatus.ID_ESTATUS_VENDIDO) && estatusActual > Estatus.ID_ESTATUS_VENDIDO) {
                //se borran los estatus del flujo queda el chip para asignacion a DN temporal
                chipTemporal.setDnTemporal(null);
                chipTemporal.setEstatusPortabilidadId(null);
                chipTemporal.setEstatusPortabilidad(null);
                chipTemporal.setFechaEstPortabilidad(null);
                chipTemporal.setEstatusProcesoId(null);
                chipTemporal.setEstatusProceso(null);
                chipTemporal.setFechaEstProceso(null);
                chipTemporal.setFolio(null);
                chipTemporal.setObservacionErrorProceso(null);
                //se asigna la fecha del cambio del estatus
                chipTemporal.setFechaEstatus(new Date());
            }
            
            if (Objects.equals(chipTemporal.getEstatusId(), Estatus.ID_ESTATUS_VENDIDO) && estatusActual < Estatus.ID_ESTATUS_VENDIDO) {
                PFMessages.showMessageError("No puede cambiar el estatus a vendido porque el chip no ha sido asignado");
                search();
                return;
            }

            if (Objects.equals(chipTemporal.getEstatusId(), Estatus.ID_ESTATUS_CHIP_NUEVO)) {
                //se borran datos de proceso inicial
                chipTemporal.setCompaniaId(null);
                chipTemporal.setNombreCliente(null);
                chipTemporal.setUsuarioVendedorId(null);
                chipTemporal.setUsuarioVendedor(null);
                chipTemporal.setUsuarioMayoristaId(null);
                chipTemporal.setUsuarioMayorista(null);
                chipTemporal.setUsuarioSupervisorId(null);
                chipTemporal.setUsuarioSupervisor(null);

                //se borran estatus anteriores
                chipTemporal.setDnTemporal(null);
                chipTemporal.setEstatusPortabilidadId(null);
                chipTemporal.setEstatusPortabilidad(null);
                chipTemporal.setEstatusProcesoId(null);
                chipTemporal.setEstatusProceso(null);
                chipTemporal.setFolio(null);
                chipTemporal.setObservacionErrorProceso(null);

                //se borran fechas de estatus
                chipTemporal.setFechaVenta(null);
                chipTemporal.setFechaAsignacionVendedor(null);
                chipTemporal.setFechaAsignacionMayorista(null);
                chipTemporal.setFechaAsignacionSupervisor(null);
                chipTemporal.setFechaEstPortabilidad(null);
                chipTemporal.setFechaEstProceso(null);

                //se borran datos de la venta
                chipTemporal.setLatitudVenta(null);
                chipTemporal.setLongitudVenta(null);
                chipTemporal.setMontoRecarga(null);

                //se asigna la fecha del cambio del estatus
                chipTemporal.setFechaEstatus(new Date());
            }

            if (Objects.equals(chipTemporal.getEstatusId(), Estatus.ID_ESTATUS_ASIGNADO_SUPERVISOR)) {
                //se borran los datos de la venta
                chipTemporal.setCompaniaId(null);
                chipTemporal.setNombreCliente(null);
                chipTemporal.setUsuarioVendedorId(null);
                chipTemporal.setUsuarioVendedor(null);
                chipTemporal.setUsuarioMayoristaId(null);
                chipTemporal.setUsuarioMayorista(null);

                //se borran estatus para que el chip quede en estatus de asignado a supervisor
                chipTemporal.setDnTemporal(null);
                chipTemporal.setEstatusPortabilidadId(null);
                chipTemporal.setEstatusPortabilidad(null);
                chipTemporal.setEstatusProcesoId(null);
                chipTemporal.setEstatusProceso(null);

                //se borran fechas de estatus
                chipTemporal.setFechaVenta(null);
                chipTemporal.setFechaAsignacionVendedor(null);
                chipTemporal.setFechaAsignacionMayorista(null);
                chipTemporal.setFechaEstPortabilidad(null);
                chipTemporal.setFechaEstProceso(null);

                //se borran datos de la venta
                chipTemporal.setLatitudVenta(null);
                chipTemporal.setLongitudVenta(null);
                chipTemporal.setMontoRecarga(null);

                //se asigna la fecha del cambio del estatus
                chipTemporal.setFechaEstatus(new Date());
            }
            chipService.update(chipTemporal);
            PFMessages.showMessageInfo("Datos actualizados correctamente");
            search();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRowCancel(RowEditEvent event) {
        Chip chipCancelado = (Chip) event.getObject();
        chipCancelado.setEstatusId(estatusActual);
        FacesMessage msg = new FacesMessage("Edición cancelada", ((Chip) event.getObject()).getDn());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
