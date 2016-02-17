/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.dao;

import com.safasoft.treeweb.bean.FfSubmitJob;
import com.safasoft.treeweb.util.BaseDAO;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 * FF Submit Job DAO
 * @created Dec 13, 2015
 * @author awal
 */
@Repository("ffSubmitJob")
public class FfSubmitJobDAO extends BaseDAO<FfSubmitJob> {

  protected static Logger logger = Logger.getLogger("controller");
  private static final int RECORDS_PER_PAGE = 10; //records amount per page

  /**
   * Get job list based on given user
   * @param userName
   * @param pageNo
   * @return list of job
   */
  public List<FfSubmitJob> getByUser(String userName, int pageNo) {
    return sessionFactory.getCurrentSession().createQuery(
            "from " + domainClass.getName() + " " +
            "where userName = :userName " +
            "order by to_date(startingTime,'DD-MON-YYYY HH24:MI:SS') desc")
            .setString("userName", userName)
            .setFirstResult((pageNo - 1) * RECORDS_PER_PAGE)
            .setMaxResults(RECORDS_PER_PAGE)
            .list();
  }
  
  /**
   * Get amount of job records based on given user
   * @param userName
   * @return amount of job
   */
  public int countByUser(String userName) {
    Long longVal = (Long) sessionFactory.getCurrentSession().createQuery(
            "select count(*) from " + domainClass.getName() + " x " +
            "where userName = :userName")
            .setString("userName", userName)
            .list().get(0);
    return longVal.intValue();
  }
  
  /**
   * Get current job id based on given user and file
   * @param userName
   * @param fileName
   * @return job id
   */
  public String getJobByUserAndFile(String userName, String fileName) {
    return sessionFactory.getCurrentSession().createQuery(
            "select jobId from " + domainClass.getName() + " x " +
            "where userName = :userName " +
            "and fileName = :fileName")
            .setString("userName", userName)
            .setString("fileName", fileName)
            .list().get(0) + "";
  }
}
