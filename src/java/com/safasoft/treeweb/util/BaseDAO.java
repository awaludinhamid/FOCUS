/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.util;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author it
 */
public class BaseDAO<T> {
  protected Class domainClass;
  @Autowired
  protected SessionFactory sessionFactory;

  public BaseDAO() {
    this.domainClass = (Class) ((ParameterizedType) getClass()
            .getGenericSuperclass())
            .getActualTypeArguments()[0];
  }

  public T save(T domain) {
    sessionFactory.getCurrentSession().saveOrUpdate(domain);
    return domain;
  }

  public T delete(T domain) {
    sessionFactory.getCurrentSession().delete(domain);
    return domain;
  }

  public T getById(int id) {
    return (T) sessionFactory.getCurrentSession().get(domainClass, id);
  }

  public T getById(String id) {
    return (T) sessionFactory.getCurrentSession().get(domainClass, id);
  }

  public List<T> getByRange(int start, int num) {
    return sessionFactory.getCurrentSession().createQuery("from " + domainClass.getName() +
            SupportUtil.getOrderColumns(domainClass))
            .setFirstResult(start)
            .setMaxResults(num)
            .list();
  }

  public List<T> getAll() {
    return sessionFactory.getCurrentSession().createQuery("from " + domainClass.getName()).list();
  }

  public int count() {
    List list = sessionFactory.getCurrentSession().createQuery(
            "select count(*) from " + domainClass.getName() + " x").list();
    Long longVal = (Long) list.get(0);
    return longVal.intValue();
  }
}
