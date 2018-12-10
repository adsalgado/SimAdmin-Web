package mx.sharkit.web.view.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author yfigueredo
 */
public abstract class PFMessages {
    public static void showMessageInfo(String details){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", details);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public static void showMessageError(String details){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", details);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public static void showMessageValidacion(String details){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Validación", details);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public static void showMessageGlobal(FacesMessage.Severity severity, String title, String details){
        FacesMessage message = new FacesMessage(severity, title, details);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public static void showMessageGeneralBackEnd(FacesMessage.Severity severity, String title, String details){
        FacesMessage message = new FacesMessage(severity, title, details);
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
    public static void showMessageInfoBackEnd(String details){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", details);
        RequestContext.getCurrentInstance().showMessageInDialog(message);        
    }
    
    public static void showMessageErrorBackEnd(String details){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", details);
        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
    
    public static void showMessageValidacionBackEnd(String details){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Validación", details);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    
}
