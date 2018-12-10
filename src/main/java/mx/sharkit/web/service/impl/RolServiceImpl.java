package mx.sharkit.web.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;
import mx.sharkit.web.dao.impl.BaseServiceImpl;
import mx.sharkit.web.model.Opcion;
import mx.sharkit.web.model.Permiso;
import mx.sharkit.web.model.Rol;
import mx.sharkit.web.repository.OpcionRepository;
import mx.sharkit.web.repository.RolRepository;
import mx.sharkit.web.service.PermisoService;
import mx.sharkit.web.service.RolService;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adrián Salgado
 */
@Service
@Transactional
public class RolServiceImpl extends BaseServiceImpl<Rol, Long> 
        implements RolService, Serializable {

    @Autowired
    private RolRepository roleRepository;

    @Autowired
    private OpcionRepository opcionRepository;

    @Autowired
    private PermisoService permisoService;

    @Override
    public Rol findByClaveRol(String claveRol) {
        return roleRepository.findByClaveRol(claveRol);
    }

    @Override
    public List<Opcion> findOpcionesByRolId(Long rolId) {
        List<Long> roles = Arrays.asList(rolId);
        return opcionRepository.findOpcionesByRolesId(roles);
    }

    @Override
    public TreeNode getTreeOptionsByRolId(Long rolId) {

        List<Opcion> opciones = opcionRepository.findAll(new Sort("orden"));
        List<Opcion> permisos = findOpcionesByRolId(rolId);
        CheckboxTreeNode root = new CheckboxTreeNode(new Opcion(0L), null);
        root.setExpanded(true);

        for (Opcion opcion : opciones) {
            if (opcion.getOpcionId() == null) {
                buildMenuHtml(root, permisos, opciones, opcion, 1);
            }
        }
        return root;
    }

    private void buildMenuHtml(TreeNode root, List<Opcion> permisos, List<Opcion> opciones, Opcion opcion, int level) {
        List<Opcion> listSubMenu = getSubMenus(opciones, opcion);
        if (listSubMenu != null && !listSubMenu.isEmpty()) {
            TreeNode node = new CheckboxTreeNode(opcion, root);

            for (Opcion opc : listSubMenu) {
                node.setExpanded(true);
                buildMenuHtml(node, permisos, opciones, opc, level++);
            }

        } else {
            TreeNode node = new CheckboxTreeNode(opcion, root);
            if (permisos.contains(opcion)) {
                node.setSelected(true);
            }
        }
    }

    private List<Opcion> getSubMenus(List<Opcion> menus, Opcion opcion) {
        List<Opcion> submenu = new ArrayList<>();
        for (Opcion subopcion : menus) {
            Long parentId = (subopcion.getOpcionId() != null && subopcion.getOpcionId() != null)
                    ? subopcion.getOpcionId() : -1L;
            if (opcion.getId().intValue() == parentId.intValue() && (subopcion.getId().intValue() != parentId.intValue())) {
                submenu.add(subopcion);
            }
        }
        return submenu;
    }

    @Override
    public void savePermisosRol(Rol rol, List<Long> opciones) throws Exception {

        List<Permiso> perm = permisoService.findByRolId(rol.getId());
        for (Permiso p : perm) {
            permisoService.deleteById(p.getId());
        }

        for (Long opcionId : opciones) {
            Permiso permiso = new Permiso();
            permiso.setOpcionId(opcionId);
            permiso.setRolId(rol.getId());
            permiso.setConsultar("V");
            permiso.setCrear("V");
            permiso.setEliminar("V");
            permiso.setModificar("V");
            permisoService.save(permiso);
        }
        
    }

}
