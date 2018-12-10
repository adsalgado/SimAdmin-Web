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
import mx.sharkit.web.model.Compania;
import mx.sharkit.web.service.CompaniaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Adrián Salgado D.
 */
@Component
@Scope("singleton")
public class CompaniaConverter implements Converter {

    private static final Logger LOG = Logger.getLogger(CompaniaConverter.class.getName());

    @Autowired
    CompaniaService companiaService;
    
    Map<Integer, Compania> mpCompanias;
    
    @PostConstruct
    public void init() {
        mpCompanias = new HashMap<>();
        List<Compania> companias = companiaService.findAll();
        for (Compania compania : companias) {
            mpCompanias.put(compania.getId(), compania);
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
            return mpCompanias.get(id);

        } else {
            return null;
        }

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return String.valueOf(((Compania) value).getId());
        } else {
            return null;
        }
    }

}
