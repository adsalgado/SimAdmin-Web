package mx.sharkit.web.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Opcion;
import mx.sharkit.web.model.Permiso;
import mx.sharkit.web.repository.OpcionRepository;
import mx.sharkit.web.repository.PermisoRepository;
import mx.sharkit.web.service.OpcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adrián Salgado
 */
@Service
@Transactional
public class OpcionServiceImpl extends BaseServiceImpl<Opcion, Long> 
        implements OpcionService, Serializable {

    @Autowired
    private OpcionRepository opcionRepository;

    @Autowired
    private PermisoRepository permisoRepository;

    @Override
    public void deleteById(Long id) {
        List<Permiso> perm = permisoRepository.findByOpcionId(id);
        for (Permiso p : perm) {
            permisoRepository.deleteById(p.getId());
        }
        opcionRepository.delete(id);
    }

    @Override
    public List<Opcion> findOpcionesByRolesId(List<Long> roles) {
        return opcionRepository.findOpcionesByRolesId(roles);
    }

    @Override
    public List<Opcion> findByOpcionIdIsNullOrderByOrden() {
        return opcionRepository.findByOpcionIdIsNullOrderByOrden();
    }

    @Override
    public void saveOpcionPerfiles(Opcion opcion, List<Long> roles) throws Exception {
        opcionRepository.save(opcion);
        List<Permiso> perm = permisoRepository.findByOpcionId(opcion.getId());
        for (Permiso p : perm) {
            permisoRepository.deleteById(p.getId());
        }
//        System.out.println("tes trans:" + (1/0));
        List<Permiso> permisos = new ArrayList<>();
        for (Long rolId : roles) {
            Permiso permiso = new Permiso();
            permiso.setOpcionId(opcion.getId());
            permiso.setRolId(rolId);
            permiso.setConsultar("V");
            permiso.setCrear("V");
            permiso.setEliminar("V");
            permiso.setModificar("V");
            permisos.add(permiso);
        }
        permisoRepository.save(permisos);
    }

}
