package mx.sharkit.web.view.administracion;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.security.CustomPasswordEncoder;
import mx.sharkit.web.security.SSUserDetails;
import mx.sharkit.web.service.UsuarioService;
import mx.sharkit.web.view.util.PFMessages;
import mx.sharkit.web.view.util.TypeCast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Adrián Salgado D.
 */
@Setter
@Getter
@Named
@ViewScoped
public class ResetPasswordBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ConfOpcionesBean.class.getName());

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private String username;
    private Usuario current;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
    private String tipoActualizacion;

    protected SSUserDetails userDetails;

    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tipoActualizacion = "create";
    }

    public void search() {
        current = usuarioService.findUserByUserName(username);
        if (current == null) {
            PFMessages.showMessageError("No se encontró el usuario con clave: '" + username + "'");
            cleanSearch();
        }

    }

    public void cleanSearch() {
        username = null;
        current = null;
    }

    public void update() {
        try {

            if (TypeCast.isBlank(newPassword)) {
                throw new Exception("Field password is required.");
            }
            if (!newPassword.equals(confirmPassword)) {
                throw new Exception("Passwords do not match.");
            }

            CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();
            current.setPassword(passwordEncoder.encode(newPassword));
//            current.setPassword(bCryptPasswordEncoder.encode(newPassword));
            current.setNoIntentos(0);
            current.setCuentaBloqueada("F");
            current.setFechaUltModificacion(new Date());

            usuarioService.save(current);
            cleanSearch();
            PFMessages.showMessageInfo("LA OPERACION SE REALIZO CORRECTAMENTE.");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ocurri\u00f3 un error al guardar el registro. {0}", e);
            PFMessages.showMessageErrorBackEnd("Ocurrió un error al guardar el registro. ");

        }

    }

}
