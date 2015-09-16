/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.service;

import com.safasoft.treeweb.bean.ListKpiTemp;
import com.safasoft.treeweb.bean.TableContentTemp;
import com.safasoft.treeweb.bean.Users;
import com.safasoft.treeweb.bean.support.ListBean;
import com.safasoft.treeweb.bean.support.UserProfileBean;
import java.util.List;

/**
 *
 * @author awal
 */
public interface UsersService {

  Users getById(String id);
  List<Users> getAll();
  List<UserProfileBean> getUserProfile(String userName);
  List<ListKpiTemp> getListKpi(String dept, String members, String coy, String lob, String dtsTable, String dtsTableLastMonth);
  List<ListKpiTemp> getListKpi(String dept, String members, String coy, String lob, String kpi, String dtsTable, String dtsTableLastMonth);
  TableContentTemp getListTableValue(String tableName, int pageNo);
  void saveTable(String sql);
  List<ListBean> getListDdl(String tableName, String code, String name);
}
