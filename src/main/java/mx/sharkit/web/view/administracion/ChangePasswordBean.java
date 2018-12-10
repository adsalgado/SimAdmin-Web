package mx.sharkit.web.view.administracion;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.security.CustomPasswordEncoder;
import mx.sharkit.web.service.UsuarioService;
import mx.sharkit.web.view.util.PFMessages;
import mx.sharkit.web.view.util.TypeCast;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author asalgado
 */
@Setter
@Getter
@Named
@ViewScoped
public class ChangePasswordBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(ChangePasswordBean.class.getName());

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    private String username;
    private Usuario instance;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    protected UserDetails userDetails;

    @PostConstruct
    public void init() {
        userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public void doChangePassword() {

        try {

            Usuario user = usuarioService.findUserByUserName(userDetails.getUsername());

            if (TypeCast.isBlank(newPassword)) {
                throw new Exception("Field password is required.");
            }
            if (!newPassword.equals(confirmPassword)) {
                throw new Exception("Passwords do not match.");
            }
            if (TypeCast.isBlank(oldPassword)) {
                throw new Exception("Field old password is required.");
            }
            if (oldPassword.equals(newPassword)) {
                throw new Exception("The old password is equal a new password.");
            }
            if (!user.getPassword().equals(customPasswordEncoder.encode(oldPassword))) {
                throw new Exception("The old password is incorrect.");
            }

            try {
                user.setPassword(customPasswordEncoder.encode(newPassword));
                user.setNoIntentos(0);
                user.setCuentaBloqueada("F");
                user.setFechaUltModificacion(new Date());
                usuarioService.save(user);

                PFMessages.showMessageInfo("LA OPERACION SE REALIZO CORRECTAMENTE.");

            } catch (Exception e) {
                throw new Exception("Ocurrió un error al guardar el registro. " + e.getMessage());
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ocurri\u00f3 un error al guardar el registro. {0}", e);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensajes del Sistema", e.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
        }

    }

}
