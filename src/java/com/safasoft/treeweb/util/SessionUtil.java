/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Session template when accessing database via java bean
 * @param <T>
 * @created Mar 31, 2013
 * @author awal
 */
public class SessionUtil<T> {

  //spring context
  private static final ApplicationContext appContext =
    new ClassPathXmlApplicationContext("applicationContext.xml");

  /**
   * Get connection for current java bean
   * @param beanName
   * @return service class
   */
  public T getAppContext(String beanName) {
    T t = (T) appContext.getBean(beanName);
    return t;
  }

}
