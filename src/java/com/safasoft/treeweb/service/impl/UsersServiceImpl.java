/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.service.impl;

import com.safasoft.treeweb.bean.ListKpiTemp;
import com.safasoft.treeweb.bean.TableContentTemp;
import com.safasoft.treeweb.bean.Users;
import com.safasoft.treeweb.bean.support.ListBean;
import com.safasoft.treeweb.bean.support.UserProfileBean;
import com.safasoft.treeweb.dao.UsersDAO;
import com.safasoft.treeweb.service.UsersService;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @created Jun 23, 2015
 * @author awal
 */
@Service("usersService")
@Transactional
public class UsersServiceImpl implements UsersService {

  @Autowired
  private UsersDAO usersDAO;

  protected static Logger logger = Logger.getLogger("service");

  public List<Users> getAll() {
    return usersDAO.getAll();
  }

  public Users getById(String id) {
    return usersDAO.getById(id);
  }

  public List<UserProfileBean> getUserProfile(String userName) {
    return usersDAO.getUserProfile(userName);
  }

  public List<ListKpiTemp> getListKpi(String dept, String members, String coy, String lob, String dtsTable, String dtsTableLastMonth) {
    return usersDAO.getListKpi(dept,members,coy,lob,dtsTable,dtsTableLastMonth);
  }

  public List<ListKpiTemp> getListKpi(String dept, String members, String coy, String lob, String kpi, String dtsTable, String dtsTableLastMonth) {
    return usersDAO.getListKpi(dept,members,coy,lob,kpi,dtsTable,dtsTableLastMonth);
  }

  public TableContentTemp getListTableValue(String tableName, int pageNo) {
    return usersDAO.getListTableValue(tableName, pageNo);
  }

  public void saveTable(String sql) {
    usersDAO.saveTable(sql);
  }

  public List<ListBean> getListDdl(String tableName, String code, String name) {
    return usersDAO.getListDdl(tableName, code, name);
  }
}
