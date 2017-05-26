package com.AlphaZ.dao;

import com.AlphaZ.entity.BaseDTO;
import com.AlphaZ.util.valid.ValideHelper;
import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Repository("BaseDAO")
public class BaseDAO<T extends BaseDTO, K extends Serializable> extends NamedParameterJdbcDaoSupport implements org.springframework.data.repository.Repository<T, K> {

    @PersistenceContext()
    protected EntityManager em;

    @Autowired
    public void setDS(DataSource ds) {
        this.setDataSource(ds);
    }

    protected Logger logger = Logger.getLogger(this.getClass());

    public T findById(Class<?> clazz, Serializable id, boolean lock) {
        Object entity;
        if (lock) {
            entity = em.find(clazz, id);
            em.lock(entity, LockModeType.WRITE);
        } else {
            entity = em.find(clazz, id);
        }
        return (T) entity;
    }

    public List<T> findAll(Class<?> clazz) {
        String jql = "from " + clazz.getName() + " where 1=1 ";
        Query q = this.em.createQuery(jql);
        return q.getResultList();
    }

    public List<T> findByField(Class<?> clazz, String filed, Object value) {
        if (ValideHelper.isNumericSpace(filed)) {
            throw new IllegalArgumentException("查询的属性不能包含数字和空格！");
        }
        String jql = "from " + clazz.getName() + "  where 1=1 and " + filed + " = ?1";
        Query q = this.em.createQuery(jql);
        q.setParameter(1, value);
        return q.getResultList();
    }

    public List<T> findByFields(Class<?> clazz, Map<String, Object> map) {
        String jql = "from " + clazz.getName() + " where 1=1 ";
        jql = jql + this.mapToJPQL(map);
        Query q = this.em.createQuery(jql);
        Map<String, Object> m = map;
        if (map != null) {
            for (String n : m.keySet()) {
                q.setParameter(n, m.get(n));
            }
        }
        return q.getResultList();
    }

    public Object findSingleByField(Class<?> clazz, String filed, Object value) {
        if (ValideHelper.isNumericSpace(filed)) {
            throw new IllegalArgumentException("查询的属性不能包含数字和空格！");
        }
        String jql = "from " + clazz.getName() + "  where 1=1 and " + filed + " = ?1";
        Query q = this.em.createQuery(jql);
        q.setParameter(1, value);
        return q.getSingleResult();
    }

    protected String mapToJPQL(Map<String, Object> map) {
        Set<String> props = map.keySet();
        Object value;
        String jql = "";
        for (String key : props) {
            value = map.get(key);
            if (value != null) {
                if (ValideHelper.isNullOrEmpty(value.toString()))
                    continue;
                jql = jql + " and " + key + "=:" + key + "";
            }
        }
        return jql;
    }

    public void makeTransient(Object entity) {
        if (entity == null)
            return;
        em.remove(entity);
    }

    public int removeByField(Class<?> clazz, String filed, Object value) {
        String jql = "delete from " + clazz.getName() + " where 1=1 and " + filed + " = ?1";

        Query dql = this.em.createQuery(jql);
        dql.setParameter(1, value);
        return dql.executeUpdate();
    }

    public List<T> executeNativeSQL(String sql, Map<String, Object> paramaters, RowMapper<T> mapper) {
        List<T> list = this.getNamedParameterJdbcTemplate().query(sql, paramaters, mapper);
        return list;
    }

    /***
     * Entity有两种编写模式 @Entity（name) 或者 @Table(name)
     * @param entity
     * @return
     */
    public String getTable(Object entity) {
        if (entity.getClass().getAnnotation(Entity.class).name() != null)
            return entity.getClass().getAnnotation(Entity.class).name();
        if (entity.getClass().getAnnotation(Table.class).name() != null)
            return entity.getClass().getAnnotation(Table.class).name();
        return "";
    }

    /***
     * Entity有两种编写模式 @Entity（name) 或者 @Table(name)
     * @param
     * @return
     */
    public String getTable(Class<?> clazz) {
        Annotation ann = clazz.getAnnotation(Table.class);
        if (ann == null) {
            ann = clazz.getAnnotation(Entity.class);
            if (ann == null)
                return null;
            else
                return ((Entity) ann).name();
        }
        return ((Table) ann).name();
    }


    public List<T> findIn(Class<?> clazz, String key, List<Object> values) {
        String jql = "from " + clazz.getName() + " where 1=1 ";
        if (ValideHelper.isNullOrEmpty(values)) {
            Query q = this.em.createQuery(jql);
            if (values != null)
                values.forEach(a -> q.setParameter("_" + a, a));
            return q.getResultList();
        }
        String startWith = " AND " + key + " IN (";

        String middle = values.stream().map(a -> ":_" + a).collect(Collectors.joining(","));
        String endWith = ")";
        jql = jql + startWith + middle + endWith;
        Query q = this.em.createQuery(jql);
        if (values != null)
            values.forEach(a -> q.setParameter("_" + a, a));
        return q.getResultList();
    }

    public void batchSave(List<T> entities) {
        for (T entity : entities) {
            this.em.persist(entity);
        }
        this.em.flush();
    }

    public void batchDelete(Class clazz, List<Object> ids) {
        for (Object id : ids) {
            em.remove(this.em.find(clazz, id));
        }
        this.em.flush();
    }

    public void save(T entity) {
        this.batchSave(new LinkedList<T>() {{
            add(0, entity);
        }});
    }

    public void delete(Class clazz, Object id) {
        this.batchDelete(clazz, new LinkedList<Object>() {{
            add(0, id);
        }});
    }

    public List<T> batchCreateOrUpdate(List<T> entities) {
        for (T entity : entities) {
            entity = this.em.merge(entity);
        }
        return entities;
    }

    public T createOrUpdate(T entity) {
        return this.batchCreateOrUpdate(new LinkedList<T>() {{
            add(0, entity);
        }}).get(0);
    }

    public List<T> createList(List<T> entity) {
        return this.batchCreateOrUpdate(entity);
    }

    public T create(T entity) {
        this.em.persist(entity);
        return entity;
    }

}
