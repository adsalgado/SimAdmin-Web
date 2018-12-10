package mx.sharkit.web.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import mx.sharkit.web.dao.BaseRepository;
import mx.sharkit.web.dao.BaseService;
import mx.sharkit.web.dao.SearchCriteria;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Adrián Salgado
 * @param <T>
 * @param <ID>
 */
public abstract class BaseServiceImpl<T, ID extends Serializable>
        implements Serializable, BaseService<T, ID> {

    private Class<T> entityClass;

    @Autowired
    protected BaseRepository<T, ID> repository;

    @Override
    public T findById(ID id) {
        return repository.findOne(id);
    }

    @Override
    public T findOne(Example<T> example) {
        return repository.findOne(example);
    }

    @Override
    public void save(T entity) {
        repository.save(entity);
    }

    @Override
    public List<T> save(Iterable<T> itrbl) {
        return repository.save(itrbl);
    }

    @Override
    public void update(T entity) {
        repository.save(entity);
    }

    @Override
    public List<T> update(Iterable<T> itrbl) {
        return repository.save(itrbl);
    }

    @Override
    public void deleteById(ID id) {
        repository.delete(id);
    }

    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }

    @Override
    public void delete(Iterable<T> itrbl) {
        repository.delete(itrbl);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<T> itrbl) {
        repository.delete(itrbl);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();

    }

    @Override
    public List<T> findAll(Example<T> example) {
        return repository.findAll(example);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<T> findAll(Iterable<ID> id) {
        return repository.findAll(id);
    }

    @Override
    public List<T> findAll(Example<T> example, Sort sort) {
        return repository.findAll(example, sort);
    }

    @Override
    public List<T> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<T> findAll(Example<T> example, Pageable pageable) {
        return repository.findAll(example, pageable);
    }

    @Override
    public List<T> search(List<SearchCriteria> params) {
        CriteriaBuilder builder = repository.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(getTClass());
        Root r = query.from(getTClass());

        Predicate predicate = builder.conjunction();
        for (SearchCriteria param : params) {
            if (param.getOperation().equalsIgnoreCase(">")) {
                predicate = builder.and(predicate,
                        builder.greaterThanOrEqualTo(r.get(param.getKey()),
                                param.getValue().toString()));
            } else if (param.getOperation().equalsIgnoreCase("<")) {
                predicate = builder.and(predicate,
                        builder.lessThanOrEqualTo(r.get(param.getKey()),
                                param.getValue().toString()));
            } else if (param.getOperation().equalsIgnoreCase(":")) {
                if (r.get(param.getKey()).getJavaType() == String.class) {
                    predicate = builder.and(predicate,
                            builder.like(r.get(param.getKey()),
                                    "%" + param.getValue() + "%"));
                } else {
                    predicate = builder.and(predicate,
                            builder.equal(r.get(param.getKey()), param.getValue()));
                }
            }
        }
        query.where(predicate);

        List<T> result = repository
                .getEntityManager().createQuery(query).getResultList();
        return result;

    }

    @Override
    public <TEntity> List<TEntity> findAllByQueryNativeToEntity(final Class<TEntity> eClazz, String queryNative, Object... params) {
        Query query = repository.getEntityManager().createNativeQuery(queryNative, eClazz);
        int noParam = 1;
        for (Object param : params) {
            query.setParameter(noParam, param);
            noParam++;
        }
        return query.getResultList();
    }

    @Override
    public List<Object[]> findAllByQueryNative(String queryNative, Object... params) {
        Query query = repository.getEntityManager().createNativeQuery(queryNative);
        int noParam = 1;
        for (Object param : params) {
            query.setParameter(noParam, param);
            noParam++;
        }
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> findAllByQueryNativeToMap(String queryNative, Object... params) {

        Session session = (Session) repository.getEntityManager().unwrap(Session.class);
        SQLQuery query = session.createSQLQuery(queryNative);
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

        int noParam = 0;
        for (Object param : params) {
            query.setParameter(noParam, param);
            noParam++;
        }

        return query.list();

    }

    @Override
    @Transactional(readOnly = true)
    public <TEntity> List<TEntity> findByCriteria(DetachedCriteria criteria) {
        return findByCriteria(criteria, null, null);
    }

    @Override
    @Transactional(readOnly = true)
    public <TEntity> List<TEntity> findByCriteria(DetachedCriteria criteria, Integer firstResult, Integer maxResults) {
        Session session = (Session) repository.getEntityManager().unwrap(Session.class);
        Criteria executableCriteria = criteria.getExecutableCriteria(session);
        if (firstResult != null) {
            executableCriteria.setFirstResult(firstResult);
        }
        if (maxResults != null) {
            executableCriteria.setMaxResults(maxResults);
        }
        return executableCriteria.list();
    }

    @SuppressWarnings("unchecked")
    public Class<T> getTClass() {
        if (entityClass == null) {
            ParameterizedType absDaoType = (ParameterizedType) this.getClass().getGenericSuperclass();
            entityClass = (Class<T>) absDaoType.getActualTypeArguments()[0];
        }
        return entityClass;
    }

}
