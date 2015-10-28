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
 * DAO template used as parent class
 * Containing basic DAO statement
 * Sub-class extend this class to gather its field/method
 * @created Aug 26, 2014
 * @author awal
 * @param <T>
 */
public class BaseDAO<T> {
  protected Class domainClass;
  @Autowired
  protected SessionFactory sessionFactory;

  /**
   * Using java reflection to get current bean class which used this class
   */
  public BaseDAO() {
    this.domainClass = (Class) ((ParameterizedType) getClass()
            .getGenericSuperclass())
            .getActualTypeArguments()[0];
  }

  /**
   * Save current record into database
   * @param domain
   * @return class bean
   */
  public T save(T domain) {
    sessionFactory.getCurrentSession().saveOrUpdate(domain);
    return domain;
  }

  /**
   * Delete current record from database
   * @param domain
   * @return class bean
   */
  public T delete(T domain) {
    sessionFactory.getCurrentSession().delete(domain);
    return domain;
  }

  /**
   * Retrieve record from database by integer id
   * @param id
   * @return class bean
   */
  public T getById(int id) {
    return (T) sessionFactory.getCurrentSession().get(domainClass, id);
  }

  /**
   * Retrieve record from database by String id
   * @param id
   * @return class bean
   */
  public T getById(String id) {
    return (T) sessionFactory.getCurrentSession().get(domainClass, id);
  }

  /**
   * Retrieve list of record by given id range
   * @param start
   * @param num
   * @return List of class bean
   */
  public List<T> getByRange(int start, int num) {
    return sessionFactory.getCurrentSession().createQuery("from " + domainClass.getName() +
            SupportUtil.getOrderColumns(domainClass))
            .setFirstResult(start)
            .setMaxResults(num)
            .list();
  }

  /**
   * Retrieve all of records
   * @return List of class bean
   */
  public List<T> getAll() {
    return sessionFactory.getCurrentSession().createQuery("from " + domainClass.getName()).list();
  }

  /**
   * Get records count
   * @return integer
   */
  public int count() {
    List list = sessionFactory.getCurrentSession().createQuery(
            "select count(*) from " + domainClass.getName() + " x").list();
    Long longVal = (Long) list.get(0);
    return longVal.intValue();
  }
}
