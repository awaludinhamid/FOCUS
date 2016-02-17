/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.service.impl;

import com.safasoft.treeweb.bean.support.ColumnProp;
import com.safasoft.treeweb.bean.support.ListKpiKeypro;
import com.safasoft.treeweb.bean.support.KpiColumn;
import com.safasoft.treeweb.bean.support.ListOption;
import com.safasoft.treeweb.bean.support.TableValueKeypro;
import com.safasoft.treeweb.dao.SupportDAO;
import com.safasoft.treeweb.service.SupportService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Support service model implementation
 * @created Dec 9, 2015
 * @author awal
 */
@Service("supportService")
@Transactional(readOnly=true)
public class SupportServiceImpl implements SupportService {

  @Autowired
  private SupportDAO supportDAO;

  @Override
  public List<ListKpiKeypro> getListKpi(int parentId) {
    return supportDAO.getListKpi(parentId);
  }

  @Override
  public List<ListOption> getListOption(String sql, int pageNo) {
    return supportDAO.getListOption(sql, pageNo);
  }

  @Override
  public List<ListOption> getListOption(String sql, int pageNo, String searchText, String excludeText) {
    return supportDAO.getListOption(sql, pageNo, searchText, excludeText);
  }

  @Override
  public List<ColumnProp> getListColumn(String tableName) {
    return supportDAO.getListColumn(tableName);
  }

  @Override
  public List<TableValueKeypro> getListTableValue(String tableName, String columnSelect, String whereClause, String orderBy, int pageNo) {
    return supportDAO.getListTableValue(tableName, columnSelect, whereClause, orderBy, pageNo);
  }

  @Override
  public List<KpiColumn> getByKpiId(int kpiId) {
    return supportDAO.getByKpiId(kpiId);
  }

  @Override
  public Integer getRecordCount(String tableName, String whereClause) {
    return supportDAO.getRecordCount(tableName, whereClause);
  }

  @Override
  public Integer getRecordCountList(String sql, String searchText, String excludeText) {
    return supportDAO.getRecordCountList(sql, searchText, excludeText);
  }

  @Override
  public void execSubmitJob(String userName, String fileName, String query, String columns, String receipt) {
    supportDAO.execSubmitJob(userName, fileName, query, columns, receipt);
  }

  @Override
  public String getListQueryByColumn(String columnCode) {
    return supportDAO.getListQueryByColumn(columnCode);
  }
}
