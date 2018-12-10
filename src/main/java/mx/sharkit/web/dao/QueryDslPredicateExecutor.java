package mx.sharkit.web.dao;

import javax.persistence.criteria.Predicate;

/**
 *
 * @author Adrián Salgado
 */
public interface QueryDslPredicateExecutor<T> {

    T findOne(Predicate predicate);             
    Iterable<T> findAll(Predicate predicate);   
    long count(Predicate predicate);            
    boolean exists(Predicate predicate);
    
}
