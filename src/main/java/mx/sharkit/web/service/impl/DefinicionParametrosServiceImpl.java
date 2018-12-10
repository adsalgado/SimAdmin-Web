package mx.sharkit.web.service.impl;

import java.io.Serializable;
import java.util.List;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.DefinicionParametros;
import mx.sharkit.web.repository.DefinicionParametrosRepository;
import mx.sharkit.web.service.DefinicionParametrosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adrián Salgado D.
 */
@Service
@Transactional
public class DefinicionParametrosServiceImpl extends BaseServiceImpl<DefinicionParametros, Integer> 
        implements DefinicionParametrosService, Serializable  {

    @Autowired
    DefinicionParametrosRepository parametrosRepository;
    
    @Override
    public List<DefinicionParametros> findByTipoParametroId(Integer tipoParametroId) {
        return parametrosRepository.findByTipoParametroId(tipoParametroId);
    }

    @Override
    public DefinicionParametros getParametroByTipoAndSeccionAndClave(Integer tipoParametro, String sCveScript, String sOperacion) {
        return parametrosRepository.getParametroByTipoAndSeccionAndClave(tipoParametro, sCveScript, sOperacion);
    }
    
}
