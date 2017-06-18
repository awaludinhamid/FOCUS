/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.service;

import com.safasoft.treeweb.bean.support.ListKpi;
import com.safasoft.treeweb.bean.support.TableContent;
import com.safasoft.treeweb.bean.Users;
import com.safasoft.treeweb.bean.support.TableValue;
import com.safasoft.treeweb.bean.support.UploadTableBean;
import com.safasoft.treeweb.bean.support.UserProfileBean;
import java.util.List;

/**
 * Users service model
 * @author awal
 */
public interface UsersService {

  Users getById(String id);
  List<Users> getAll();
  List<UserProfileBean> getUserProfile(String userName);
  List<ListKpi> getListKpi(String dept, String members, String coy, String lob, String dtsTable, String dtsTableLastMonth);
  List<ListKpi> getListKpi(String dept, String members, String coy, String lob, String kpi, String dtsTable, String dtsTableLastMonth);
  TableContent getListTableContentByPage(String tableName, int pageNo);
  void saveTable(String sql);
  List<TableValue> getListTableValue(String tableName, String columnsSerialExt, String orderByColumn, int currPageNoFind);
  List<TableValue> getListTableValue(String tableName, String columnsSerialExt, String orderByColumn, String searchText, int currPageNoFind);
  Integer getPageNo(String tableName, String id, String columnsSerialExt, String orderByColumn);
  List<UploadTableBean> getListUploadTableByUser(String userAccess);
}
