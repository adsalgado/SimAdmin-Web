package mx.sharkit.web.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.ChipHistoricoEstatus;
import mx.sharkit.web.model.CierreChip;
import mx.sharkit.web.model.Estatus;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.model.UsuarioRol;
import mx.sharkit.web.security.SSUserDetails;
import mx.sharkit.web.service.ChipHistoricoService;
import mx.sharkit.web.service.ChipService;
import mx.sharkit.web.service.CierreChipService;
import mx.sharkit.web.service.TransaccionService;
import mx.sharkit.web.service.UsuarioRolService;
import mx.sharkit.web.view.util.TypeCast;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author aalquisira
 */
@Service
@Transactional
public class ChipServiceImpl extends BaseServiceImpl<Chip, Integer> implements ChipService, Serializable {

    @Autowired
    private ChipHistoricoService chipHistoricoService;

    @Autowired
    private TransaccionService transaccionService;

    @Autowired
    private UsuarioRolService usuarioRolService;
    
    @Autowired
    private CierreChipService cierreChipService;

    @Override
    public Chip findBySerie(String serie) {
        try {
            DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
            dc.add(Restrictions.eq("serie", serie));
            List<Chip> chips = findByCriteria(dc);
            if (chips.isEmpty()) {
                return null;
            } else {
                return chips.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String saveChips(List<Chip> chips) {
        try {
            update(chips);
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public Chip findByDn(String telefono) {
        DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
        dc.add(Restrictions.eq("dn", telefono));
        List<Chip> chips = findByCriteria(dc);
        if (chips.isEmpty()) {
            return null;
        } else {
            return chips.get(0);
        }
    }

    @Override
    public Integer actualizaListaDn(List<Map<String, String>> lstDn, String user) throws Exception {
        int actualizados = 0;
        for (Map<String, String> map : lstDn) {
            String serie = map.get("serie");
            String dn = map.get("dn");

            Chip chip = findBySerie(serie);
            if (chip != null && Objects.equals(chip.getEstatusId(), Estatus.ID_ESTATUS_VENDIDO)) {
                chip.setDnTemporal(dn);
                chip.setEstatusId(Estatus.ID_ESTATUS_DN_TEMPORAL);
                chip.setFechaEstatus(new Date());
                chip.setFechaUltModificacion(new Date());
                ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();
                historicoEstatus.setEstatusId(Estatus.ID_ESTATUS_DN_TEMPORAL);
                historicoEstatus.setFechaEstatus(new Date());
                historicoEstatus.setObservaciones("DN Temporal asignado desde la WEB");
                historicoEstatus.setUsuarioEstatus(user);
                if (chipHistoricoService.saveChipAndHistory(chip, historicoEstatus)) {
                    save(chip);
                    actualizados++;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        return actualizados;
    }

    @Override
    public Integer asignarChipsSubdistribuidorByRango(String serie, String serieFinal, Long coordinadorId, Long supervisorId, String username) throws Exception {
        int actualizados = 0;
        Date fechaAsignacion = new Date();

        DetachedCriteria criteria = DetachedCriteria.forClass(Chip.class);
        criteria.add(Restrictions.eq("estatusId", Estatus.ID_ESTATUS_ASIGNADO_DISTRIBUIDOR));
        criteria.add(Restrictions.eq("usuarioCoordinadorId", coordinadorId));
        criteria.add(Restrictions.between("serie", serie, serieFinal));
        List<Chip> chips = findByCriteria(criteria);
        for (Chip chip : chips) {
            chip.setUsuarioSupervisorId(supervisorId);
            chip.setFechaAsignacionSupervisor(fechaAsignacion);
            chip.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_SUPERVISOR);
            chip.setFechaEstatus(fechaAsignacion);
            chip.setFechaUltModificacion(fechaAsignacion);

            ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();
            historicoEstatus.setSerieId(chip.getId());
            historicoEstatus.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_SUPERVISOR);
            historicoEstatus.setFechaEstatus(fechaAsignacion);
            historicoEstatus.setObservaciones("Asignación a supervisor desde la WEB");
            historicoEstatus.setUsuarioEstatus(username);

            save(chip);
            chipHistoricoService.save(historicoEstatus);
            actualizados++;
        }
        return actualizados;
    }

    @Override
    public Integer asignarChipsDistribuidorByRango(String serie, String serieFinal, Long coordinadorId, String username) throws Exception {
        int actualizados = 0;
        Date fechaAsignacion = new Date();

        DetachedCriteria criteria = DetachedCriteria.forClass(Chip.class);
        criteria.add(Restrictions.eq("estatusId", Estatus.ID_ESTATUS_CHIP_NUEVO));
        criteria.add(Restrictions.between("serie", serie, serieFinal));
        List<Chip> chips = findByCriteria(criteria);
        for (Chip chip : chips) {
            chip.setUsuarioCoordinadorId(coordinadorId);
            chip.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_DISTRIBUIDOR);
            chip.setFechaEstatus(fechaAsignacion);
            chip.setFechaUltModificacion(fechaAsignacion);

            ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();
            historicoEstatus.setSerieId(chip.getId());
            historicoEstatus.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_DISTRIBUIDOR);
            historicoEstatus.setFechaEstatus(fechaAsignacion);
            historicoEstatus.setObservaciones("Asignación a distribuidor desde la WEB");
            historicoEstatus.setUsuarioEstatus(username);

            save(chip);
            chipHistoricoService.save(historicoEstatus);
            actualizados++;
        }
        return actualizados;
    }

    @Override
    public Integer actualizaListaRecargas(List<Map<String, Object>> lstRecargas, String username) throws Exception {
        int actualizados = 0;
        for (Map<String, Object> map : lstRecargas) {
            String serie = TypeCast.toString(map.get("serie"));
            String dn = TypeCast.toString(map.get("dn"));
            //BigDecimal monto = TypeCast.toBigDecimal(map.get("monto"));
            Integer folio = TypeCast.toInteger(map.get("folio"));

            Chip chip = findBySerie(serie);
            if (chip != null
                    && Objects.equals(chip.getEstatusPortabilidadId(), Estatus.ID_ESTATUS_PORTABILIDAD_EXITOSA)
                    && Objects.equals(chip.getEstatusProcesoId(), Estatus.ID_ESTATUS_PROCESO_FINALIZADO)) {
                //chip.setMontoRecarga(monto);
                chip.setFolio(folio);

                save(chip);
                actualizados++;
            }

        }
        return actualizados;
    }

    @Override
    public void saveChipsDistribuidor(List<Map<String, Object>> chips, SSUserDetails userDetails) throws Exception {
        Date now = Calendar.getInstance().getTime();
        Map<String, Usuario> distribuidores = getUsuariosByRol(UsuarioRol.ID_ROL_COORDINADOR);
        Map<String, Usuario> subdistribuidores = getUsuariosByRol(UsuarioRol.ID_ROL_SUPERVISOR);

        for (Map<String, Object> map : chips) {
            String serie = TypeCast.toString(map.get("serie"));
            String distribuidor = TypeCast.toString(map.get("distribuidor"));
            String subdistribuidor = TypeCast.toString(map.get("subdistribuidor"));

            Chip chip = new Chip();
            chip.setSerie(serie + "F");
            chip.setEstatusId(Estatus.ID_ESTATUS_CHIP_NUEVO);
            chip.setFecha(now);
            chip.setDocumento(TypeCast.toString(map.get("compania")));
            chip.setReferencia("");
            chip.setArticulo("");
            chip.setDescripcion("");
            chip.setModelo("");
            chip.setCantidad(0);
            chip.setCostoCompra(BigDecimal.ZERO);
            save(chip);

            ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();
            historicoEstatus.setSerieId(chip.getId());
            historicoEstatus.setEstatusId(Estatus.ID_ESTATUS_CHIP_NUEVO);
            historicoEstatus.setFechaEstatus(now);
            historicoEstatus.setObservaciones("Carga de chips desde la WEB");
            historicoEstatus.setUsuarioEstatus(userDetails.getUser().getUserName());
            chipHistoricoService.save(historicoEstatus);

            if (distribuidores.containsKey(distribuidor)) {
                Usuario userDistribuidor = distribuidores.get(distribuidor);

                chip.setUsuarioCoordinadorId(userDistribuidor.getId());
                chip.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_DISTRIBUIDOR);
                chip.setFechaEstatus(now);
                chip.setFechaUltModificacion(now);
                save(chip);

                historicoEstatus = new ChipHistoricoEstatus();
                historicoEstatus.setSerieId(chip.getId());
                historicoEstatus.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_DISTRIBUIDOR);
                historicoEstatus.setFechaEstatus(now);
                historicoEstatus.setObservaciones("Asignación a distribuidor desde la WEB");
                historicoEstatus.setUsuarioEstatus(userDetails.getUser().getUserName());
                chipHistoricoService.save(historicoEstatus);

                if (subdistribuidores.containsKey(subdistribuidor)) {
                    Usuario userSubDistribuidor = subdistribuidores.get(subdistribuidor);
                    chip.setUsuarioSupervisorId(userSubDistribuidor.getId());
                    chip.setFechaAsignacionSupervisor(now);
                    chip.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_SUPERVISOR);
                    chip.setFechaEstatus(now);
                    chip.setFechaUltModificacion(now);
                    save(chip);

                    historicoEstatus = new ChipHistoricoEstatus();
                    historicoEstatus.setSerieId(chip.getId());
                    historicoEstatus.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_SUPERVISOR);
                    historicoEstatus.setFechaEstatus(now);
                    historicoEstatus.setObservaciones("Asignación a supervisor desde la WEB");
                    historicoEstatus.setUsuarioEstatus(userDetails.getUser().getUserName());
                    chipHistoricoService.save(historicoEstatus);

                }
            }

        }
    }

    @Override
    public Integer recepcionChipsAlmacenByRango(String serie, String serieFinal, Long coordinadorId, Long supervisorId, String username) throws Exception {
        int actualizados = 0;
        Date fechaAsignacion = new Date();

        DetachedCriteria criteria = DetachedCriteria.forClass(Chip.class);
        criteria.add(Restrictions.eq("estatusId", Estatus.ID_ESTATUS_ASIGNADO_SUPERVISOR));
        criteria.add(Restrictions.eq("usuarioCoordinadorId", coordinadorId));
        criteria.add(Restrictions.eq("usuarioSupervisorId", supervisorId));
        criteria.add(Restrictions.between("serie", serie, serieFinal));
        List<Chip> chips = findByCriteria(criteria);
        for (Chip chip : chips) {
            chip.setUsuarioSupervisorId(supervisorId);
            chip.setFechaAsignacionSupervisor(fechaAsignacion);
            chip.setEstatusId(Estatus.ID_ESTATUS_EN_ALMACEN);
            chip.setFechaEstatus(fechaAsignacion);
            chip.setFechaUltModificacion(fechaAsignacion);

            ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();
            historicoEstatus.setSerieId(chip.getId());
            historicoEstatus.setEstatusId(Estatus.ID_ESTATUS_EN_ALMACEN);
            historicoEstatus.setFechaEstatus(fechaAsignacion);
            historicoEstatus.setUsuarioEstatus(username);

            save(chip);
            chipHistoricoService.save(historicoEstatus);
            actualizados++;
        }
        return actualizados;
    }

    @Override
    public Integer asignarChipsVendedorByRango(String serie, String serieFinal, Long idDistribuidor, Long idSupervisor, Long idPromotor, String username) throws Exception {
        int actualizados = 0;
        Date fechaAsignacion = new Date();

        DetachedCriteria criteria = DetachedCriteria.forClass(Chip.class);
        criteria.add(Restrictions.eq("estatusId", Estatus.ID_ESTATUS_EN_ALMACEN));
        criteria.add(Restrictions.eq("usuarioCoordinadorId", idDistribuidor));
        criteria.add(Restrictions.eq("usuarioSupervisorId", idSupervisor));
        criteria.add(Restrictions.between("serie", serie, serieFinal));
        List<Chip> chips = findByCriteria(criteria);
        for (Chip chip : chips) {
            chip.setUsuarioVendedorId(idPromotor);
            chip.setFechaAsignacionVendedor(fechaAsignacion);
            chip.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_VENDEDOR);
            chip.setFechaEstatus(fechaAsignacion);
            chip.setFechaUltModificacion(fechaAsignacion);

            ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();
            historicoEstatus.setSerieId(chip.getId());
            historicoEstatus.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_VENDEDOR);
            historicoEstatus.setFechaEstatus(fechaAsignacion);
            historicoEstatus.setUsuarioEstatus(username);

            save(chip);
            chipHistoricoService.save(historicoEstatus);
            actualizados++;
        }
        return actualizados;
    }

    @Override
    public Integer regresarChipsAlmacenByRango(String serie, String serieFinal, Long idDistribuidor, Long idSupervisor, Long idPromotor, String username) throws Exception {
        int actualizados = 0;
        Date fechaAsignacion = new Date();

        DetachedCriteria criteria = DetachedCriteria.forClass(Chip.class);
        criteria.add(Restrictions.eq("estatusId", Estatus.ID_ESTATUS_ASIGNADO_VENDEDOR));
        criteria.add(Restrictions.eq("usuarioCoordinadorId", idDistribuidor));
        criteria.add(Restrictions.eq("usuarioSupervisorId", idSupervisor));
        criteria.add(Restrictions.eq("usuarioVendedorId", idPromotor));
        criteria.add(Restrictions.between("serie", serie, serieFinal));
        List<Chip> chips = findByCriteria(criteria);
        for (Chip chip : chips) {
            chip.setUsuarioSupervisorId(idSupervisor);
            chip.setFechaAsignacionSupervisor(fechaAsignacion);
            chip.setEstatusId(Estatus.ID_ESTATUS_EN_ALMACEN);
            chip.setFechaEstatus(fechaAsignacion);
            chip.setFechaUltModificacion(fechaAsignacion);

            ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();
            historicoEstatus.setSerieId(chip.getId());
            historicoEstatus.setEstatusId(Estatus.ID_ESTATUS_EN_ALMACEN);
            historicoEstatus.setFechaEstatus(fechaAsignacion);
            historicoEstatus.setUsuarioEstatus(username);

            save(chip);
            chipHistoricoService.save(historicoEstatus);
            actualizados++;
        }
        return actualizados;
    }

    private Map<String, Usuario> getUsuariosByRol(Integer idRol) {
        List<UsuarioRol> userRol;
        DetachedCriteria dc = DetachedCriteria.forClass(UsuarioRol.class);
        dc.add(Restrictions.eq("rolId", TypeCast.toLong(idRol)));
        dc.createAlias("usuario", "usuario");
        dc.add(Restrictions.eq("usuario.activo", 1));
        userRol = usuarioRolService.findByCriteria(dc);
        Map<String, Usuario> users = new HashMap<>();
        for (UsuarioRol usuarioRol : userRol) {
            users.put(usuarioRol.getUsuario().getUserName(), usuarioRol.getUsuario());
        }
        return users;
    }

    @Override
    public void limpiarBase() throws Exception {
        chipHistoricoService.deleteAll();
        transaccionService.deleteAll();
        deleteAll();
    }

    @Override
    public Integer cancelarChipsSubdistribuidorByRango(String serie, String serieFinal, Long supervisorId, String observaciones, String username) throws Exception {
        int actualizados = 0;
        Date fechaAsignacion = new Date();

        DetachedCriteria criteria = DetachedCriteria.forClass(Chip.class);
        criteria.add(Restrictions.eq("estatusId", Estatus.ID_ESTATUS_VENDIDO));
        criteria.add(Restrictions.eq("usuarioSupervisorId", supervisorId));
        criteria.add(Restrictions.between("serie", serie, serieFinal));
        List<Chip> chips = findByCriteria(criteria);
        for (Chip chip : chips) {
            chip.setEstatusId(Estatus.ID_ESTATUS_CANCELADO);
            chip.setFechaEstatus(fechaAsignacion);
            chip.setFechaUltModificacion(fechaAsignacion);

            ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();
            historicoEstatus.setSerieId(chip.getId());
            historicoEstatus.setEstatusId(Estatus.ID_ESTATUS_CANCELADO);
            historicoEstatus.setObservaciones(observaciones);
            historicoEstatus.setFechaEstatus(fechaAsignacion);
            historicoEstatus.setUsuarioEstatus(username);

            save(chip);
            chipHistoricoService.save(historicoEstatus);
            actualizados++;
        }
        return actualizados;
    }

    @Override
    public Integer reasignarChipsSubdistribuidorByRango(String serie, String serieFinal, Long supervisorId, Long supervisorNuevoId, String username) throws Exception {
        int actualizados = 0;
        Date fechaAsignacion = new Date();

        DetachedCriteria criteria = DetachedCriteria.forClass(Chip.class);
        criteria.add(Restrictions.in("estatusId",
                new Integer[]{Estatus.ID_ESTATUS_ASIGNADO_SUPERVISOR, Estatus.ID_ESTATUS_EN_ALMACEN}));
        criteria.add(Restrictions.eq("usuarioSupervisorId", supervisorId));
        criteria.add(Restrictions.between("serie", serie, serieFinal));
        List<Chip> chips = findByCriteria(criteria);

        for (Chip chip : chips) {
            chip.setUsuarioSupervisorId(supervisorNuevoId);
            chip.setFechaAsignacionSupervisor(fechaAsignacion);
            chip.setUsuarioVendedorId(null);
            chip.setFechaAsignacionVendedor(null);
//            chip.setEstatusId(Estatus.ID_ESTATUS_ASIGNADO_SUPERVISOR);
            chip.setFechaEstatus(fechaAsignacion);
            chip.setFechaUltModificacion(new Date());

            ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();
            historicoEstatus.setSerieId(chip.getId());
            historicoEstatus.setEstatusId(chip.getEstatusId());
            historicoEstatus.setFechaEstatus(fechaAsignacion);
            historicoEstatus.setUsuarioEstatus(username);
            historicoEstatus.setObservaciones("Reasignación a subdistribuidor: " + supervisorNuevoId);

            save(chip);
            chipHistoricoService.save(historicoEstatus);
            actualizados++;
        }
        return actualizados;
    }

    @Override
    public Integer retornoChipsAlmacenPromotor(List<String> series, Long idDistribuidor, Long idSupervisor, Long idPromotor, String username) throws Exception {
        int actualizados = 0;
        Date fechaAsignacion = new Date();
        
        for (String serie : series) {
            Chip chip = findBySerie(serie);
            chip.setUsuarioSupervisorId(idSupervisor);
            chip.setFechaAsignacionSupervisor(fechaAsignacion);
            chip.setEstatusId(Estatus.ID_ESTATUS_EN_ALMACEN);
            chip.setFechaEstatus(fechaAsignacion);
            chip.setFechaUltModificacion(fechaAsignacion);

            ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();
            historicoEstatus.setSerieId(chip.getId());
            historicoEstatus.setEstatusId(Estatus.ID_ESTATUS_EN_ALMACEN);
            historicoEstatus.setFechaEstatus(fechaAsignacion);
            historicoEstatus.setUsuarioEstatus(username);

            save(chip);
            chipHistoricoService.save(historicoEstatus);
            actualizados++;
        }

        return actualizados;
    }

    @Override
    public void cierreChipsVenta(List<Chip> selectedChips, BigDecimal totalVenta, Long idSupervisor, Long idVendedor, Date fechaIni, Date fechaFin, String username) throws Exception {
        Date now = new Date();
        CierreChip cierre = new  CierreChip(idVendedor, totalVenta, idSupervisor, now, fechaIni, fechaFin);
        cierreChipService.save(cierre);
        for (Chip selectedChip : selectedChips) {
            selectedChip.setCierreChipId(cierre.getId());
            selectedChip.setEstatusId(Estatus.ID_ESTATUS_CERRADO);
            selectedChip.setFechaEstatus(now);
            selectedChip.setFechaUltModificacion(now);

            ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();
            historicoEstatus.setSerieId(selectedChip.getId());
            historicoEstatus.setEstatusId(Estatus.ID_ESTATUS_EN_ALMACEN);
            historicoEstatus.setFechaEstatus(now);
            historicoEstatus.setUsuarioEstatus(username);

            save(selectedChip);
            chipHistoricoService.save(historicoEstatus);
        }
    }

    @Override
    public Integer actualizarChipsVendidos(List<Map<String, Object>> chips, SSUserDetails userDetails) throws Exception {
        int actualizados = 0;
        Date now = new Date();
        for (Map<String, Object> map : chips) {
            String serie = TypeCast.toString(map.get("serie"));
            String subdistribuidor = TypeCast.toString(map.get("subdistribuidor"));
            Long subdistribuidorId = TypeCast.toLong(map.get("subdistribuidorId"));

            Chip chip = findBySerie(serie);
            if (chip != null) {
                if (!Objects.equals(chip.getUsuarioSupervisorId(), subdistribuidorId)) {
                    throw new Exception("El chip '" + chip.getSerie() + "' no esta asignado al subdistribuidor '" + subdistribuidor + "'.");
                }
                chip.setEstatusId(Estatus.ID_ESTATUS_VENDIDO);
                chip.setFechaEstatus(now);
                chip.setFechaUltModificacion(now);
                
                ChipHistoricoEstatus historicoEstatus = new ChipHistoricoEstatus();
                historicoEstatus.setEstatusId(Estatus.ID_ESTATUS_VENDIDO);
                historicoEstatus.setFechaEstatus(now);
                historicoEstatus.setObservaciones("DN Temporal asignado desde la WEB");
                historicoEstatus.setUsuarioEstatus(userDetails.getUsername());
                if (chipHistoricoService.saveChipAndHistory(chip, historicoEstatus)) {
                    save(chip);
                    actualizados++;
                } 
            } 
        }
        return actualizados;
    }

}
