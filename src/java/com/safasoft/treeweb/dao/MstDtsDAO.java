/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.dao;

import com.safasoft.treeweb.bean.MstDts;
import com.safasoft.treeweb.util.BaseDAO;
import org.springframework.stereotype.Repository;

/**
 * @created Aug 26, 2015
 * @author awal
 */
@Repository("mstDts")
public class MstDtsDAO extends BaseDAO<MstDts> {

  public MstDts getByCode(String dtsCode) {
    return (MstDts) sessionFactory.getCurrentSession().createQuery("from " + domainClass.getName() +
            " where dtsCode = :dtsCode")
            .setString("dtsCode", dtsCode)
            .list()
            .get(0);
  }
}
