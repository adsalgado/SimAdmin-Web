package mx.sharkit.web.service.impl;

import java.io.Serializable;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.UsuarioRol;
import mx.sharkit.web.service.UsuarioRolService;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adrián Salgado
 */
@Service
@Transactional
public class UsuarioRolServiceImpl extends BaseServiceImpl<UsuarioRol, Integer> 
        implements UsuarioRolService, Serializable {

}
