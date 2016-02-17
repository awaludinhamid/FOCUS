/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.service;

import com.safasoft.treeweb.bean.support.ColumnProp;
import com.safasoft.treeweb.bean.support.ListKpiKeypro;
import com.safasoft.treeweb.bean.support.KpiColumn;
import com.safasoft.treeweb.bean.support.ListOption;
import com.safasoft.treeweb.bean.support.TableValueKeypro;
import java.util.List;

/**
 * Support model service
 * @created Dec 9, 2015
 * @author awal
 */
public interface SupportService {

  List<ListKpiKeypro> getListKpi(int parentId);
  List<ListOption> getListOption(String sql, int pageNo);
  List<ListOption> getListOption(String sql, int pageNo, String searchText, String excludeText);
  List<ColumnProp> getListColumn(String tableName);
  List<TableValueKeypro> getListTableValue(String tableName, String columnSelect, String whereClause, String orderBy, int pageNo);
  List<KpiColumn> getByKpiId(int kpiId);
  Integer getRecordCount(String tableName, String whereClause);
  Integer getRecordCountList(String sql, String searchText, String excludeText);
  void execSubmitJob(String userName, String fileName, String query, String columns, String receipt);
  String getListQueryByColumn(String columnCode);
}
