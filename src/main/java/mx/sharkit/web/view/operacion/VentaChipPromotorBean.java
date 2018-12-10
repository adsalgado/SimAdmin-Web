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
import mx.sharkit.web.model.Compania;
import mx.sharkit.web.model.Estatus;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.security.SSUserDetails;
import mx.sharkit.web.service.ChipHistoricoService;
import mx.sharkit.web.service.ChipService;
import mx.sharkit.web.service.CompaniaService;
import mx.sharkit.web.view.util.PFMessages;
import org.primefaces.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Adrián Salgado D.
 */
@Setter
@Getter
@Named
@ViewScoped
public class VentaChipPromotorBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(VentaChipPromotorBean.class.getName());

    @Autowired
    private ChipService chipService;
    @Autowired
    private CompaniaService companiaService;
    @Autowired
    private ChipHistoricoService chipHistoricoService;

    private List<Compania> companias;

    private String serie;
    private Chip current;
    private Compania compania;
    private Usuario vendedor;

    private SSUserDetails userDetails;

    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        companias = companiaService.findAll();

        if (SecurityUtils.ifGranted("ROLE_VENDEDOR")) {
            vendedor = userDetails.getUser();
        }

    }

    public void search() {
        current = chipService.findBySerie(serie);
        if (current != null) {
            if (Objects.equals(current.getEstatusId(), Estatus.ID_ESTATUS_ASIGNADO_VENDEDOR)) {
                if (vendedor != null && (!Objects.equals(current.getUsuarioVendedorId(), vendedor.getId()))) {
                    PFMessages.showMessageError("El chip '" + serie + "' no se asignado a este promotor. ");
                    cleanSearch();
                }
            } else {
                PFMessages.showMessageError("El chip '" + serie + "' no se encuentra en estatus asignado a promotor. ");
                cleanSearch();
            }
        } else {
            PFMessages.showMessageError("No se encontró el chip con serie: '" + serie + "'");
            cleanSearch();
        }

    }

    public void cleanSearch() {
        serie = null;
        current = null;
    }

    public void update() {
        try {
            current.setCompaniaId(compania.getId());
            current.setFechaVenta(new Date());
            current.setEstatusId(Estatus.ID_ESTATUS_VENDIDO);
            current.setFechaUltModificacion(new Date());

            ChipHistoricoEstatus che = new ChipHistoricoEstatus();
            che.setEstatusId(Estatus.ID_ESTATUS_VENDIDO);
            che.setFechaEstatus(new Date());
            che.setObservaciones("Actualizado desde app web");
            che.setUsuarioEstatus(userDetails.getUsername());

            chipHistoricoService.saveChipAndHistory(current, che);
            PFMessages.showMessageInfo("LA OPERACIÓN SE REALIZÓ CORRECTAMENTE.");
            cleanSearch();
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            PFMessages.showMessageError("Ocurrió un error al acutalizar: '" + e.getMessage() + "'");
        }
    }

}
