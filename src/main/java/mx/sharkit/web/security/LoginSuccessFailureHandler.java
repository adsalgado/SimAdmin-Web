package mx.sharkit.web.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.service.UsuarioService;
import mx.sharkit.web.view.util.TypeCast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 *
 * @author asalgado
 */
@Component
public class LoginSuccessFailureHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    private static final Logger LOGGER = Logger.getLogger(LoginSuccessFailureHandler.class.getName());

    @Autowired
    UsuarioService usuarioService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        
        SSUserDetails ud = (SSUserDetails) authentication.getPrincipal();

        /**
         * * TRANSACTION LOGS **
         */
        StringBuilder transactionLogs = new StringBuilder("[");
        transactionLogs.append("Date: ").append(new Date());
        transactionLogs.append("]");
        ud.saveTransaccion("INICIO_SESION", transactionLogs.toString());

        String targetUrl = "/inicio.xhtml";
        if (response.isCommitted()) {
            LOGGER.log(Level.SEVERE, "Response has already been committed. Unable to redirect to {0}", targetUrl);
            return;
        }

        Usuario user = ud.getUser();

        Integer noAccesos = user.getNoAccesos();
        if (noAccesos == null) {
            noAccesos = 0;
        }
        user.setNoAccesos(++noAccesos);
        user.setNoIntentos(0);
        user.setCuentaBloqueada("F");

        usuarioService.save(user);

        String XRequestedWith = request.getHeader("X-Requested-With");
        if (!TypeCast.isBlank(XRequestedWith) && (XRequestedWith.equals("XMLHttpRequest"))) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("Content-Type", "text/html");
            response.setHeader("Expires", "Mon, 01 Jan 1900 01:00:00 GMT");
            response.setHeader("Cache-Control", "must-revalidate");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Access-Control-Allow-Origin", "*");
            try (PrintWriter writer = response.getWriter()) {
                writer.print("{message:\"" + authentication.getPrincipal() + "\",success:true}");
                writer.flush();
            }
        } else {
            RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
            redirectStrategy.sendRedirect(request, response, targetUrl);
        }

    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ae) throws IOException, ServletException {
        if (!TypeCast.isBlank(obtainUsername(request))) {
            Usuario usuario = usuarioService.findUserByUserName(obtainUsername(request));
            if (usuario != null) {
                usuario.setNoIntentos(usuario.getNoIntentos() + 1);
                if (usuario.getNoIntentos() >= 3) {
                    usuario.setCuentaBloqueada("V");
                }
                usuarioService.save(usuario);
            }
        }
        String XRequestedWith = request.getHeader("X-Requested-With");
        if (!TypeCast.isBlank(XRequestedWith) && (XRequestedWith.equals("XMLHttpRequest"))) {
            SecurityContextHolder.clearContext();
            response.setHeader("Content-Type", "text/html");
            response.setHeader("Expires", "Mon, 01 Jan 1900 01:00:00 GMT");
            response.setHeader("Cache-Control", "must-revalidate");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Access-Control-Allow-Origin", "*");
            try (PrintWriter writer = response.getWriter()) {
                writer.print("{message:\"" + ae.getMessage() + "\",success:false}");
                writer.flush();
            }
        } else {
            String targetUrl = String.format("/login.xhtml?error=true&message=%s", ae.getMessage());
//            response.sendRedirect(request.getContextPath() + targetUrl);
            RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
            redirectStrategy.sendRedirect(request, response, targetUrl);
        }

    }

    private String obtainUsername(HttpServletRequest request) {
        String username = request.getParameter("j_username");
        if (username == null) {
            username = request.getParameter("username");
        }
        return username;
    }

}
