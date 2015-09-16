/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @created Mar 31, 2013
 * @author awal
 */
public class SessionUtil<T> {

    private static final ApplicationContext appContext =
            new ClassPathXmlApplicationContext("applicationContext.xml");

    public T getAppContext(String beanName) {
        T t = (T) appContext.getBean(beanName);
        return t;
    }

}
