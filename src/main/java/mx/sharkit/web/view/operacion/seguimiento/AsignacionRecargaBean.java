package mx.sharkit.web.view.operacion.seguimiento;

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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.mail.util.ByteArrayDataSource;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.carga.CfgArchivoCarga;
import mx.sharkit.web.carga.FileData;
import mx.sharkit.web.carga.ResponseValidate;
import mx.sharkit.web.model.Chip;
import mx.sharkit.web.model.Estatus;
import mx.sharkit.web.model.Usuario;
import mx.sharkit.web.security.SSUserDetails;
import mx.sharkit.web.service.ArchivoCargaService;
import mx.sharkit.web.service.ChipService;
import mx.sharkit.web.service.DynScriptService;
import mx.sharkit.web.service.UsuarioRolService;
import mx.sharkit.web.service.UsuarioService;
import mx.sharkit.web.view.util.PFMessages;
import mx.sharkit.web.view.util.TypeCast;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author aalquisira
 */
@Setter
@Getter
@Named
@ViewScoped
public class AsignacionRecargaBean implements Serializable, Observer {

    private static final Logger LOGGER = Logger.getLogger(AsignacionRecargaBean.class.getName());
    private static final String CLAVE_PROCESO = "ARCHIVOS_CARGA";
    private static final String CLAVE_SCRIPT = "CARGA_RECARGAS";

    @Autowired
    ArchivoCargaService archivoCargaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRolService usuarioRolService;

    @Autowired
    private ChipService chipService;

    private List<Estatus> estatus;

    private List<Usuario> currents;
    private Usuario current;
    private Usuario selectedCurrent;

    private List<Chip> chips;
    private Chip chip;
    private Chip selectedChip;

    /**
     * Carga de archivo
     */
    private FileData fileData;
    private List<ResponseValidate> errores;
    private List<Map<String, Object>> lstRecargas;

    private SSUserDetails userDetails;

    @PostConstruct
    public void init() {
        userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        current = new Usuario();
        search();
    }

    public void search() {
        try {
            DetachedCriteria dc = DetachedCriteria.forClass(Chip.class);
            dc.add(Restrictions.eq("estatusPortabilidadId", Estatus.ID_ESTATUS_PORTABILIDAD_EXITOSA));
            dc.add(Restrictions.eq("estatusProcesoId", Estatus.ID_ESTATUS_PROCESO_FINALIZADO));
            //dc.add(Restrictions.isNull("montoRecarga"));
            dc.add(Restrictions.isNull("folio"));
            chips = chipService.findByCriteria(dc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            Chip chipTemporal = (Chip) event.getObject();
            chipService.update(chipTemporal);
            search();
            PFMessages.showMessageInfo("Estatus de portabilidad exitosa");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edición cancelada", ((Chip) event.getObject()).getDn());
        FacesContext.getCurrentInstance().addMessage(null, msg);
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
                PFMessages.showMessageError("Ya se cargó el archivo.");
            }

        } catch (Exception ex) {
            PFMessages.showMessageErrorBackEnd("Ocurrió un error al cargar el documento. " + ex.getMessage());
        }

    }

    public void doProcesaArchivo() {

        try {

            CfgArchivoCarga cfgArchivo = archivoCargaService.getConfiguracionArchivoCarga(DynScriptService.TIPO_PARAMETRO_PROCESOS, CLAVE_PROCESO, CLAVE_SCRIPT);

            errores = new ArrayList<>();
            lstRecargas = new ArrayList<>();

            archivoCargaService.addObserver(this);
            archivoCargaService.validarArchivoCarga(fileData, cfgArchivo);

            if (errores.isEmpty()) {
                if (!lstRecargas.isEmpty()) {

                    Integer actualizados = chipService.actualizaListaRecargas(lstRecargas, userDetails.getUsername());
                    RequestContext.getCurrentInstance().addCallbackParam("saved", true);
                    PFMessages.showMessageInfo(String.format("SE ACTUALIZARON %d REGISTROS.", actualizados));
                    search();

                } else {
                    PFMessages.showMessageError("NO SE IMPORTÓ NINGÚN REGISTRO.");
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
            Map<String, Object> mpDn = new HashMap<>();
            mpDn.put("dn", TypeCast.toString(response.getData().get("dn")));
            mpDn.put("serie", TypeCast.toString(response.getData().get("serie")));
//            mpDn.put("monto", TypeCast.toBigDecimal(response.getData().get("monto")));
            mpDn.put("folio", TypeCast.toInteger(response.getData().get("folio")));
            lstRecargas.add(mpDn);
        }

    }

}
