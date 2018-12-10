package mx.sharkit.web.service.impl;

import java.io.Serializable;
import java.util.List;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Permiso;
import mx.sharkit.web.repository.PermisoRepository;
import mx.sharkit.web.service.PermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adrián Salgado
 */
@Service
@Transactional
public class PermisoServiceImpl extends BaseServiceImpl<Permiso, Long> 
        implements PermisoService, Serializable {

    @Autowired
    private PermisoRepository permisoRepository;

    @Override
    public Permiso findByRolIdAndOpcionId(Long rolId, Long opcionId) {
        return permisoRepository.findByRolIdAndOpcionId(rolId, opcionId);
    }

    @Override
    public List<Permiso> findByOpcionId(Long opcionId) {
        return permisoRepository.findByOpcionId(opcionId);
    }

    @Override
    public List<Permiso> findByRolId(Long rolId) {
        return permisoRepository.findByRolId(rolId);
    }

}
