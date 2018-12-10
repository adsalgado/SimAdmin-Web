package mx.sharkit.web.service.impl;

import java.io.Serializable;
import java.util.List;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Sucursal;
import mx.sharkit.web.repository.SucursalRepository;
import mx.sharkit.web.service.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author aalquisira
 */
@Service
@Transactional
public class SucursalServiceImpl extends BaseServiceImpl<Sucursal, Integer> implements SucursalService, Serializable {

    @Autowired
    private SucursalRepository sucursalRepository;

    @Override
    public List<Sucursal> findByEmpresaIdOrderByNombre(Integer empresaId) {
        return sucursalRepository.findByEmpresaIdOrderByNombre(empresaId);
    }

}
