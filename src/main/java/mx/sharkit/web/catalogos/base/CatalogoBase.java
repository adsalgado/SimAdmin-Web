package mx.sharkit.web.catalogos.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import mx.sharkit.web.dao.BaseService;
import mx.sharkit.web.security.SSUserDetails;
import mx.sharkit.web.view.util.PFMessages;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Adrián Salgado D.
 * @param <T>
 * @param <ID>
 */
@Getter
@Setter
public class CatalogoBase<T, ID extends Serializable> implements Serializable {

    @Autowired
    private BaseService<T, ID> baseService;

    protected T current;
    protected T selectedCurrent;
    protected T rowSelectCurrent;
    protected T filterCurrent;
    private Class<T> clazz;
    protected List<T> currents;
    private LazyDataModel<T> lazyData;
    protected boolean showPanelDatos;
    protected String tipoActualizacion;
    protected SSUserDetails userDetails;


    protected List<Criterion> conditions;
    protected DetachedCriteria criteria;

    @PostConstruct
    public void init() {
        try {
            userDetails = (SSUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            current = getTClass().newInstance();
            filterCurrent = getTClass().newInstance();
            //currents = baseService.findAll();
            conditions = new ArrayList<>();
            setShowPanelDatos(true);
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(CatalogoBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void search() {
        if (criteria != null) {
            currents = (List<T>) baseService.findByCriteria(criteria);
        } else if (conditions != null) {
            DetachedCriteria dc = DetachedCriteria.forClass(getTClass());
            for (Criterion condition : conditions) {
                dc.add(condition);
            }
            currents = (List<T>) baseService.findByCriteria(dc);
        } else {
            DetachedCriteria dc = DetachedCriteria.forClass(getTClass());
            currents = (List<T>) baseService.findByCriteria(dc);
        }
    }

    public void cleanObject() {
        try {
            current = getTClass().newInstance();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void doBeforeCreate() throws Exception {

    }

    public void doAfterCreate() throws Exception {

    }

    public void create() {
        try {

            doBeforeCreate();
            baseService.save(current);
            doAfterCreate();

            PFMessages.showMessageInfo("LA OPERACIÓN SE REALIZÓ CORRECTAMENTE.");

        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            PFMessages.showMessageErrorBackEnd("Ocurrió un error al actualizar la información. " + e.getMessage());
        }
    }

    public void doBeforeUpdate() throws Exception {

    }

    public void doAfterUpdate() throws Exception {

    }

    public void update() {
        try {

            doBeforeUpdate();
            baseService.update(current);
            doAfterUpdate();
            PFMessages.showMessageInfo("LA OPERACIÓN SE REALIZÓ CORRECTAMENTE.");

        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            PFMessages.showMessageErrorBackEnd("Ocurrió un error al actualizar la información. " + e.getMessage());
        }
    }

    public void doBeforeDelete() throws Exception {

    }

    public void doAfterDelete() throws Exception {

    }

    public void delete() {
        try {

            doBeforeDelete();
            baseService.delete(selectedCurrent);
            doAfterDelete();
            PFMessages.showMessageInfo("LA OPERACIÓN SE REALIZÓ CORRECTAMENTE.");

        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            PFMessages.showMessageErrorBackEnd("Ocurrió un error al eliminar la información. " + e.getMessage());
        }
    }

    public void doBeforeRowEdit() throws Exception {

    }

    public void doAfterRowEdit() throws Exception {

    }

    public void onRowEdit(RowEditEvent event) {
        try {

            doBeforeRowEdit();
            T currentEdit = (T) event.getObject();
            baseService.update(currentEdit);

            PFMessages.showMessageInfo("LA OPERACIÓN SE REALIZÓ CORRECTAMENTE.");
            doAfterRowEdit();

        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            PFMessages.showMessageErrorBackEnd("Ocurrió un error al eliminar la información. " + e.getMessage());
        }
    }

    public void onRowCancel(RowEditEvent event) {

    }

    public Class<T> getTClass() {
        if (clazz == null) {
            ParameterizedType absDaoType = (ParameterizedType) this.getClass().getGenericSuperclass();
            clazz = (Class<T>) absDaoType.getActualTypeArguments()[0];
        }
        return clazz;
    }

}
