package mx.sharkit.web.view.operacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.carga.CfgArchivoCarga;
import mx.sharkit.web.carga.FileData;
import mx.sharkit.web.carga.ResponseValidate;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.model.UsuarioRol;
import mx.sharkit.web.security.SSUserDetails;
import mx.sharkit.web.service.ArchivoCargaService;
import mx.sharkit.web.service.ChipService;
import mx.sharkit.web.service.DynScriptService;
import mx.sharkit.web.view.util.PFMessages;
import mx.sharkit.web.view.util.TypeCast;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Adrián Salgado D.
 */
@Named
@ViewScoped
@Setter
@Getter
public class CargaChipsVendidosBean implements Serializable, Observer {

    private static final Logger LOGGER = Logger.getLogger(CargaChipsVendidosBean.class.getName());
    private static final String CLAVE_PROCESO = "ARCHIVOS_CARGA";
    private static final String CLAVE_SCRIPT = "CARGA_CHIPS_VEND";

    @Autowired
    ArchivoCargaService archivoCargaService;

    @Autowired
    ChipService chipService;

    /**
     * Carga de archivo de chips
     */
    private FileData fileData;
    private List<ResponseValidate> errores;
    private List<Map<String, Object>> chips;
    private SSUserDetails userDetails;
    private boolean showPanelChips;
    private Map<String, Usuario> distribuidores;
    private Map<String, Usuario> subdistribuidores;

    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cargaUsuarios();
        ocultaPanelChips();
    }

    public void cleanCarga() {
        fileData = null;
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {

            UploadedFile file = event.getFile();

            if (fileData == null) {
                if ((file != null) && (file.getSize() > 0)) {

                    fileData = new FileData();
                    DataSource source = new ByteArrayDataSource(TypeCast.toBytes(file.getInputstream()), file.getContentType());
                    fileData.setFileName(file.getFileName());
                    fileData.setFileType(FilenameUtils.getExtension(file.getFileName()));
                    fileData.setContentType(file.getContentType());
                    fileData.setDataHandler(new DataHandler(source));
                    fileData.setFileSize(file.getSize());

                }
            } else {
//                PFMessages.showMessageError("Ya se cargó el archivo.");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Ya se cargó el archivo."));
            }

        } catch (Exception ex) {
//            PFMessages.showMessageErrorBackEnd("Ocurrió un error al cargar el documento. " + ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ocurrió un error al cargar el documento. ", ex.getMessage()));
        }

    }

    public void doProcesaArchivo() {

        try {
            ocultaPanelChips();

            CfgArchivoCarga cfgArchivo = archivoCargaService.getConfiguracionArchivoCarga(DynScriptService.TIPO_PARAMETRO_PROCESOS, CLAVE_PROCESO, CLAVE_SCRIPT);

            errores = new ArrayList<>();
            chips = new ArrayList<>();

            archivoCargaService.addObserver(this);
            archivoCargaService.validarArchivoCarga(fileData, cfgArchivo);

            if (errores.isEmpty()) {
                if (!chips.isEmpty()) {

                    Integer totalActualizados = chipService.actualizarChipsVendidos(chips, userDetails);
                    muestraPanelChips();
                    RequestContext.getCurrentInstance().addCallbackParam("saved", true);
                    PFMessages.showMessageInfo("LA OPERACIÓN SE REALIZÓ CORRECTAMENTE. "
                            + "SE ACTUALIZARON " + totalActualizados + " REGISTROS.");

                } else {
                    PFMessages.showMessageError("Error!, NO SE IMPORTÓ NINGÚN REGISTRO.");
                }

            } else {
                //Archivo con errores   idDevoluciones
                RequestContext.getCurrentInstance().execute("PF('dlgFile').show();");
                RequestContext.getCurrentInstance().execute("PF('idCarga').hide();");
                PFMessages.showMessageError("EXISTEN ERRORES EN EL ARCHIVO DE CARGA.");
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            if (ex.getCause() != null && ex.getCause().getCause() != null) {
                PFMessages.showMessageErrorBackEnd(ex.getCause().getCause().getMessage());
            } else {
                PFMessages.showMessageErrorBackEnd(ex.getMessage());
            }
        }

    }

    @Override
    public void update(Observable observable, Object arg) {
        LOGGER.log(Level.FINE, String.format("Observable: %s, Object: %s", observable, arg));

        ResponseValidate response = (ResponseValidate) arg;
        if (!response.isValid()) {
            errores.add(response);
        } else {
            String serie = TypeCast.toString(response.getData().get("serie"));
            String subdistribuidor = TypeCast.toString(response.getData().get("subdistribuidor"));
            
            if (!StringUtils.isBlank(subdistribuidor) && !subdistribuidores.containsKey(subdistribuidor)) {
                response.setValid(false);
                response.setMessage("El usuario subdistribuidor '" + subdistribuidor + "' es inválido");
                errores.add(response);
            } else {
                Usuario sd = subdistribuidores.get(subdistribuidor);
                response.getData().put("subdistribuidorId", sd.getId());
                chips.add(response.getData());
            }
        }

    }

    public void muestraPanelChips() {
        showPanelChips = true;
    }

    public void ocultaPanelChips() {
        showPanelChips = false;
    }

    private void cargaUsuarios() {

        DetachedCriteria dc = DetachedCriteria.forClass(UsuarioRol.class);
        dc.add(Restrictions.eq("rolId", new Long(UsuarioRol.ID_ROL_COORDINADOR)));
        dc.createAlias("usuario", "usuario");
        dc.add(Restrictions.eq("usuario.activo", 1));

        distribuidores = new HashMap<>();
        List<UsuarioRol> userRol = chipService.findByCriteria(dc);
        for (UsuarioRol usuarioRol : userRol) {
            distribuidores.put(usuarioRol.getUsuario().getUserName(), usuarioRol.getUsuario());
        }

        DetachedCriteria dc2 = DetachedCriteria.forClass(UsuarioRol.class);
        dc2.add(Restrictions.eq("rolId", new Long(UsuarioRol.ID_ROL_SUPERVISOR)));
        dc2.createAlias("usuario", "usuario");
        dc2.add(Restrictions.eq("usuario.activo", 1));

        subdistribuidores = new HashMap<>();
        userRol = chipService.findByCriteria(dc2);
        for (UsuarioRol usuarioRol : userRol) {
            subdistribuidores.put(usuarioRol.getUsuario().getUserName(), usuarioRol.getUsuario());
        }

    }

}
