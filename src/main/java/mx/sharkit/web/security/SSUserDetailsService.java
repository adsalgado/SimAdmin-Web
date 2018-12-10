package mx.sharkit.web.security;

import java.util.ArrayList;
import java.util.List;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.model.UsuarioRol;
import mx.sharkit.web.service.TransaccionService;
import mx.sharkit.web.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 *
 * @author Adrián Salgado
 */
@Component
public class SSUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SSUserDetailsService.class);

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TransaccionService transaccionService;

    public SSUserDetailsService() {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        try {
            Usuario usuario = usuarioService.findUserByUserName(username);
            if (usuario == null) {
                LOGGER.debug("user not found with the provided username");
                throw new UsernameNotFoundException("User not found");
            }
            LOGGER.debug(" user from username " + usuario.getUserName());

            boolean accountNonExpired = true;
            boolean accountNonLocked = true;
            boolean credentialsNonExpired = true;
            boolean enabled = true;

            return new SSUserDetails(
                    usuario,
                    getAuthorities(usuario),
                    transaccionService,
                    accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
            
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
        
    }

    private List<GrantedAuthority> getAuthorities(Usuario user) {
        List<UsuarioRol> lur = usuarioService.getRolesByUsername(user.getUserName());
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (UsuarioRol ur : lur) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(ur.getRol().getClaveRol());
            authorities.add(grantedAuthority);
        }
        LOGGER.debug("user authorities are " + authorities.toString());
        return authorities;
    }

}
