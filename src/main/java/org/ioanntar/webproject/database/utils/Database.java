package org.ioanntar.webproject.database.utils;

import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

public class Database {

    private Session session;
    private final Transaction transaction;

    public Database() {
        session = HibernateUtils.getSessionFactory().getCurrentSession();
        transaction = session.getTransaction();
        if (!transaction.isActive())
            transaction.begin();
    }

    public <T> T get(Class<?> entityClass, long id) {
        return (T) session.get(entityClass, id);
    }

    public <T> T merge(T entity) {
        return session.merge(entity);
    }

    public <T> List<T> getAll(Class<T> entityClass) {
        var criteriaQuery = session.getCriteriaBuilder().createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        return session.createQuery(criteriaQuery.select(root)).getResultList();
    }

    public void commit() {
        try {
            transaction.commit();
        } catch(Exception re) {
            transaction.rollback();
        }
    }

    public <T> void delete(T entity) {
        session.remove(entity);
    }
}