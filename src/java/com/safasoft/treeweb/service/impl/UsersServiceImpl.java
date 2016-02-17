/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.service.impl;

import com.safasoft.treeweb.bean.support.ListKpi;
import com.safasoft.treeweb.bean.support.TableContent;
import com.safasoft.treeweb.bean.Users;
import com.safasoft.treeweb.bean.support.ListBean;
import com.safasoft.treeweb.bean.support.TableValue;
import com.safasoft.treeweb.bean.support.UserProfileBean;
import com.safasoft.treeweb.dao.UsersDAO;
import com.safasoft.treeweb.service.UsersService;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Users service model implementation
 * @created Jun 23, 2015
 * @author awal
 */
@Service("usersService")
@Transactional
public class UsersServiceImpl implements UsersService {

  @Autowired
  private UsersDAO usersDAO;

  protected static Logger logger = Logger.getLogger("service");

  @Override
  public List<Users> getAll() {
    return usersDAO.getAll();
  }

  @Override
  public Users getById(String id) {
    return usersDAO.getById(id);
  }

  @Override
  public List<UserProfileBean> getUserProfile(String userName) {
    return usersDAO.getUserProfile(userName);
  }

  @Override
  public List<ListKpi> getListKpi(String dept, String members, String coy, String lob, String dtsTable, String dtsTableLastMonth) {
    return usersDAO.getListKpi(dept,members,coy,lob,dtsTable,dtsTableLastMonth);
  }

  @Override
  public List<ListKpi> getListKpi(String dept, String members, String coy, String lob, String kpi, String dtsTable, String dtsTableLastMonth) {
    return usersDAO.getListKpi(dept,members,coy,lob,kpi,dtsTable,dtsTableLastMonth);
  }

  @Override
  public TableContent getListTableContentByPage(String tableName, int pageNo) {
    return usersDAO.getListTableContentByPage(tableName, pageNo);
  }

  @Override
  public void saveTable(String sql) {
    usersDAO.saveTable(sql);
  }

  @Override
  public List<TableValue> getListTableValue(String tableName, String columnsSerialExt, String orderByColumn, int currPageNoFind) {
    return usersDAO.getListTableValue(tableName, columnsSerialExt, orderByColumn, currPageNoFind);
  }

  @Override
  public Integer getPageNo(String tableName, String id, String columnsSerialExt, String orderByColumn) {
    return usersDAO.getPageNo(tableName, id, columnsSerialExt, orderByColumn);
  }

  @Override
  public List<ListBean> getListUploadTableByUser(String userAccess) {
    return usersDAO.getListUploadTableByUser(userAccess);
  }

  @Override
  public List<TableValue> getListTableValue(String tableName, String columnsSerialExt, String orderByColumn, String searchText, int currPageNoFind) {
    return usersDAO.getListTableValue(tableName, columnsSerialExt, orderByColumn, searchText, currPageNoFind);
  }
}
