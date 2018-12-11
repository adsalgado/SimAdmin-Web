package mx.sharkit.web.service.impl;

import java.io.Serializable;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Producto;
import mx.sharkit.web.service.ProductoService;
import org.springframework.stereotype.Service;

/**
 *
 * @author asalgado
 */
@Service
@Transactional
public class ProductoServiceImpl extends BaseServiceImpl<Producto, Integer> implements ProductoService, Serializable {

}
