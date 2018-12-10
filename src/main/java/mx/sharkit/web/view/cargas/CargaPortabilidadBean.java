package mx.sharkit.web.view.cargas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
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
import mx.sharkit.web.service.ArchivoCargaService;
import mx.sharkit.web.service.ChipPortabilidadService;
import mx.sharkit.web.service.DynScriptService;
import mx.sharkit.web.view.util.TypeCast;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Adrián Salgado D.
 */
@Setter
@Getter
@Named
@ViewScoped
public class CargaPortabilidadBean implements Serializable, Observer {

    private static final Logger LOGGER = Logger.getLogger(CargaPortabilidadBean.class.getName());
    private static final String CLAVE_PROCESO = "ARCHIVOS_CARGA";
    private static final String CLAVE_SCRIPT = "CARGA_PORTABILIDAD";

    @Autowired
    ArchivoCargaService archivoCargaService;

    @Autowired
    ChipPortabilidadService chipPortabilidadService;

    /**
     * Carga de archivo de chips
     */
    private FileData fileData;
    private List<ResponseValidate> errores;
    private List<Map<String, Object>> chips;

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

            CfgArchivoCarga cfgArchivo = archivoCargaService.getConfiguracionArchivoCarga(DynScriptService.TIPO_PARAMETRO_PROCESOS, CLAVE_PROCESO, CLAVE_SCRIPT);

            errores = new ArrayList<>();
            chips = new ArrayList<>();

            archivoCargaService.addObserver(this);
            archivoCargaService.validarArchivoCarga(fileData, cfgArchivo);

            if (errores.isEmpty()) {
                if (!chips.isEmpty()) {

                    Integer actualizados = chipPortabilidadService.savePortabilidad(chips);
                    RequestContext.getCurrentInstance().addCallbackParam("saved", true);
//                    PFMessages.showMessageInfo(String.format("SE ACTUALIZARON %d REGISTROS.", actualizados));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "SE ACTUALIZARON REGISTROS.", actualizados.toString()));

                } else {
//                    PFMessages.showMessageError("NO SE IMPORTÓ NINGÚN REGISTRO.");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "NO SE IMPORTÓ NINGÚN REGISTRO."));
                }

            } else {
                //Archivo con errores   idDevoluciones
                RequestContext.getCurrentInstance().execute("PF('dlgFile').show();");
                RequestContext.getCurrentInstance().execute("PF('idCarga').hide();");
//                PFMessages.showMessageError("EXISTEN ERRORES EN EL ARCHIVO DE CARGA.");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", "EXISTEN ERRORES EN EL ARCHIVO DE CARGA."));
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            if (ex.getCause() != null && ex.getCause().getCause() != null) {
//                PFMessages.showMessageErrorBackEnd(ex.getCause().getCause().getMessage());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal!", ex.getCause().getCause().getMessage()));
            } else {
//                PFMessages.showMessageErrorBackEnd(ex.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal!", ex.getCause().getCause().getMessage()));
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
            chips.add(response.getData());
        }

    }

}
