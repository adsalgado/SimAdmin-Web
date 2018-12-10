package mx.sharkit.web.converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import mx.sharkit.web.model.Estatus;
import mx.sharkit.web.service.EstatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Adrián Salgado D.
 */
@Component
@Scope("singleton")
public class EstatusConverter implements Converter {

    private static final Logger LOG = Logger.getLogger(EstatusConverter.class.getName());

    @Autowired
    EstatusService estatusService;
    
    Map<Integer, Estatus> mpEstatuss;
    
    @PostConstruct
    public void init() {
        mpEstatuss = new HashMap<>();
        List<Estatus> lstEstatus = estatusService.findAll();
        for (Estatus est : lstEstatus) {
            mpEstatuss.put(est.getId(), est);
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        if (value != null && value.trim().length() > 0) {

            Integer id;
            try {
                id = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                LOG.log(Level.SEVERE, e.getMessage());
                return null;
            }
            return mpEstatuss.get(id);

        } else {
            return null;
        }

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return String.valueOf(((Estatus) value).getId());
        } else {
            return null;
        }
    }

}
