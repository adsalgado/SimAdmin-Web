package mx.sharkit.web.repository;

import java.util.List;
import mx.sharkit.web.dao.BaseRepository;
import mx.sharkit.web.model.Sucursal;

/**
 *
 * @author asalgado
 */
public interface SucursalRepository extends BaseRepository<Sucursal, Integer>{
    List<Sucursal> findByEmpresaIdOrderByNombre(Integer empresaId);
}
