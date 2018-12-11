package mx.sharkit.web.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import mx.sharkit.web.dao.BaseService;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.security.SSUserDetails;

/**
 *
 * @author aalquisira
 */
public interface ChipService extends BaseService<Chip, Long>{
    Chip findBySerie(String serie);
    String saveChips(List<Chip> chips);
    void saveChipsDistribuidor(List<Map<String, Object>> chips, SSUserDetails userDetails) throws Exception;
    Chip findByDn(String telefono);
    Integer actualizaListaDn(List<Map<String, String>> lstDn, String user) throws Exception;
    Integer asignarChipsDistribuidorByRango(String serie, String serieFinal, Long coordinadorId, String username) throws Exception;
    Integer asignarChipsSubdistribuidorByRango(String serie, String serieFinal, Long coordinadorId, Long supervisorId, String username) throws Exception;
    Integer recepcionChipsAlmacenByRango(String serie, String serieFinal, Long coordinadorId, Long supervisorId, String username) throws Exception;
    Integer asignarChipsVendedorByRango(String serie, String serieFinal, Long idDistribuidor, Long idSupervisor, Long idPromotor, String username) throws Exception;
    Integer regresarChipsAlmacenByRango(String serie, String serieFinal, Long idDistribuidor, Long idSupervisor, Long idPromotor, String username) throws Exception;
    Integer actualizaListaRecargas(List<Map<String, Object>> lstRecargas, String username) throws Exception;
    void limpiarBase() throws Exception;
    Integer cancelarChipsSubdistribuidorByRango(String serie, String serieFinal, Long supervisorId, String observaciones, String username) throws Exception;
    Integer reasignarChipsSubdistribuidorByRango(String serie, String serieFinal, Long supervisorId, Long supervisorNuevoId, String username) throws Exception;
    Integer retornoChipsAlmacenPromotor(List<String> series, Long idDistribuidor, Long idSupervisor, Long idPromotor, String username) throws Exception;
    void cierreChipsVenta(List<Chip> selectedChips, BigDecimal totalVenta, Long idSupervisor, Long idVendedor, Date fechaIni, Date fechaFin, String username) throws Exception;
    Integer actualizarChipsVendidos(List<Map<String, Object>> chips, SSUserDetails userDetails) throws Exception;   
    void borraChipConDependencias(Long chipId) throws Exception;
}
