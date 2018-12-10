package mx.sharkit.web.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 *
 * @author Adrián Salgado
 * @param <T>
 * @param <ID>
 */
public interface BaseService<T, ID extends Serializable> {
    T findById(ID id);
    T findOne(Example<T> example);
    void save(T entity);
    List<T> save(Iterable<T> itrbl);
    void update(T entity);
    List<T> update(Iterable<T> itrbl);
    void deleteById(ID id);
    void delete(Iterable<T> itrbl);
    void delete(T entity);
    void deleteAll();
    void deleteAll(Iterable<T> itrbl);
    List<T> findAll();
    List<T> findAll(Example<T> example);
    List<T> findAll(Iterable<ID> id);
    Page<T> findAll(Pageable pageable);
    List<T> findAll(Sort sort);
    List<T> findAll(Example<T> example, Sort sort);
    Page<T> findAll(Example<T> example, Pageable pageable);
    List<T> search(List<SearchCriteria> params);
    <TEntity> List<TEntity> findAllByQueryNativeToEntity(final Class<TEntity> eClazz, final String queryNative, final Object... params);
    List<Object[]> findAllByQueryNative(final String queryNative, final Object... params);
    List<Map<String, Object>> findAllByQueryNativeToMap(final String queryNative, final Object... params);
    <TEntity> List<TEntity> findByCriteria(final DetachedCriteria criteria);
    <TEntity> List<TEntity> findByCriteria(final DetachedCriteria criteria, final Integer firstResult, final Integer maxResults);
}
