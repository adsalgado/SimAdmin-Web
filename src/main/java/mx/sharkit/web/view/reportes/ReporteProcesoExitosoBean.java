package mx.sharkit.web.view.reportes;

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
import mx.sharkit.web.model.ChipHistoricoEstatus;
import mx.sharkit.web.model.Estatus;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.security.SSUserDetails;
import mx.sharkit.web.service.ChipHistoricoService;
import mx.sharkit.web.service.ChipService;
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
public class ReporteProcesoExitosoBean {

    @Autowired
    private ChipService chipService;

    @Autowired
    private ChipHistoricoService chipHistoricoService;

    private List<Usuario> currents;
    private Usuario current;
    private Usuario selectedCurrent;

    private List<Chip> chips;
    private Chip chip;
    private Chip selectedChip;

    private SSUserDetails userDetails;

    private List<Estatus> estatus;
    private Estatus estatusLiberado;
    private Integer estatusInicial;

    private ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();

    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        current = new Usuario();
        obtenerEstatusInicial();
        search();
    }

    public void search() {
        try {
            DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
            dc.add(Restrictions.eq("estatusProcesoId", Estatus.ID_ESTATUS_PROCESO_FINALIZADO));
            dc.add(Restrictions.ne("estatusId", Estatus.ID_ESTATUS_LIBERADO));
            chips = chipService.findByCriteria(dc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void obtenerEstatusInicial() {
        DetachedCriteria dc = DetachedCriteria.forClass(Estatus.class);
//        dc.add(Restrictions.eq("tipoEstatusId", Estatus.TIPO_ESTATUS_INICIAL));
        dc.add(Restrictions.eq("id", Estatus.ID_ESTATUS_LIBERADO));
        estatus = chipService.findByCriteria(dc);
    }

    public void obtenerChips() {
        try {
            DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
            if (estatusInicial != null && estatusInicial > 0) {
                dc.add(Restrictions.eq("estatusId", estatusInicial));
                chips = chipService.findByCriteria(dc);
            } else {
                search();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            historicoEstatus = new ChipHistoricoEstatus();
            Chip chipTemporal = (Chip) event.getObject();
            chipTemporal.setFechaUltModificacion(new Date());
            if (estatusLiberado != null) {
                chipTemporal.setEstatusId(estatusLiberado.getId());
                historicoEstatus.setObservaciones("Liberacion desde la WEB");
            }else {
                historicoEstatus.setObservaciones("Observacion desde la WEB");
            }            
            historicoEstatus.setEstatusId(Estatus.ID_ESTATUS_LIBERADO);
            historicoEstatus.setFechaEstatus(new Date());

            historicoEstatus.setUsuarioEstatus(userDetails.getUser().getUserName());
            if (chipHistoricoService.saveChipAndHistory(chipTemporal, historicoEstatus)) {
                PFMessages.showMessageInfo("Datos actualizados correctamente");
                estatusLiberado = null;
                search();
            } else {
                PFMessages.showMessageError("Error interno, si continua consulte a su administrador");
                System.out.println("El chip ya fue asignado");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edición cancelada", ((Chip) event.getObject()).getSerie());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        estatusLiberado = null;
    }
}
