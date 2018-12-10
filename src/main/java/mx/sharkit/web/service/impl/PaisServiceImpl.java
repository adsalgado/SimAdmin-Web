
package mx.sharkit.web.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Pais;
import mx.sharkit.web.model.Zona;
import mx.sharkit.web.repository.PaisRepository;
import mx.sharkit.web.repository.ZonaRepository;
import mx.sharkit.web.service.PaisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 *
 * @author malcantara
 */
@Service
@Transactional
public class PaisServiceImpl extends BaseServiceImpl<Pais, Integer> 
        implements PaisService, Serializable {
    
    @Autowired
    private PaisRepository paisRepository;
    
    @Autowired
    private ZonaRepository zonaRepository;
    
    @Override
    public Pais getPaisByPaisId(int paisId){
        return paisRepository.findByPaisId(paisId);
    } 
    
    public Zona getZonaById(int zonaId){
        return zonaRepository.findById(zonaId);
    }
    
}
