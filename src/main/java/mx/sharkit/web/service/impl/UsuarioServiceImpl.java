package mx.sharkit.web.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.model.UsuarioRol;
import mx.sharkit.web.repository.UsuarioRepository;
import mx.sharkit.web.repository.UsuarioRolRepository;
import mx.sharkit.web.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adrián Salgado
 */
@Service
@Transactional
public class UsuarioServiceImpl extends BaseServiceImpl<Usuario, Long> implements UsuarioService, Serializable {

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    @Override
    public Usuario findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public void save(Usuario user) {
        userRepository.save(user);
    }

    @Override
    public boolean isUserExist(Usuario user) {
        return findUserByUserName(user.getUserName()) != null;
    }

    @Override
    public Usuario findByEmailAddress(String emailAddress) {
        return userRepository.findByEmailAddress(emailAddress);
    }

    @Override
    public List<UsuarioRol> getRolesByUsername(String userName) {
        return usuarioRolRepository.findByUserName(userName);
    }

    @Override
    public void saveUsuarioPerfiles(Usuario usuario, List<Long> roles) {
        userRepository.save(usuario);
        List<UsuarioRol> ur = getRolesByUsername(usuario.getUserName());
        for (UsuarioRol usuarioRol : ur) {
            usuarioRolRepository.deleteById(usuarioRol.getId());
        }
        List<UsuarioRol> usuarioRoles = new ArrayList<>();
        for (Long rolId : roles) {
            UsuarioRol usuarioRol = new UsuarioRol();
            usuarioRol.setUsuarioId(usuario.getId());
            usuarioRol.setRolId(rolId);
            usuarioRoles.add(usuarioRol);
        }
        usuarioRolRepository.save(usuarioRoles);
    }

    @Override
    public List<UsuarioRol> getRolesByRolKey(String rolName) {
        return usuarioRolRepository.findByRolKey(rolName);
    }

    @Override
    public List<Usuario> findByRolIdOrderByNombre(Long rolId) {
        return userRepository.findByRolIdOrderByNombre(rolId);
    }

    @Override
    public List<Usuario> findByRolIdAndPadreIdOrderByNombre(Long rolId, Long padreId) {
        return userRepository.findByRolIdAndPadreIdOrderByNombre(rolId, padreId);
    }

}
