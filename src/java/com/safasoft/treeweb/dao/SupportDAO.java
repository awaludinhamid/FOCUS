/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.dao;

import com.safasoft.treeweb.bean.support.ColumnProp;
import com.safasoft.treeweb.bean.support.ListKpiKeypro;
import com.safasoft.treeweb.bean.support.KpiColumn;
import com.safasoft.treeweb.bean.support.ListOption;
import com.safasoft.treeweb.bean.support.TableValueKeypro;
import com.safasoft.treeweb.util.BaseDAO;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * A custom DAO for accessing data from the database
 * @created Dec 9, 2015
 * @author awal
 */
@Repository("supportDAO")
public class SupportDAO extends BaseDAO<Object> {

  private static final int RECORDS_PER_PAGE = 10; //amount records per page
  private static final int RECORDS_OPTION_PER_PAGE = 20; //amount records searched option per page
  
  /**
   * Get list of KPI data
   * @param parentId
   * @return list of KPI
   */
  public List<ListKpiKeypro> getListKpi(int parentId) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT kpi_id, parent_id, kpi_name, fact_name,  " +
              "DECODE(fact_name,NULL,'red',DECODE(table_name,NULL,'grey','green')) color  " +
              "FROM (  " +
              "SELECT kpi.kpi_id, kpi.parent_id, kpi.kpi_name, kpi.lvl, fact.fact_name, fact.table_name  " +
                "FROM (  " +
                "SELECT 0 kpi_id, -1 parent_id, 'KPI NASIONAL' kpi_name, -1 lvl " +
                  "FROM DUAL " +
                "UNION ALL " +
                "SELECT kpi_id, 0, UPPER(kpi_name) kpi_name, 0 " +
                  "FROM mst_kpi " +
                  "WHERE kpi_id = DECODE(:parentId,0,-1,:parentId) " +
                "UNION ALL " +
                "SELECT kpi_id, parent_id, UPPER(kpi_name) kpi_name, LEVEL lvl  " +
                  "FROM (  " +
                  "SELECT kkak.parent_id, mk.kpi_id, mk.kpi_name  " +
                    "FROM mst_kpi mk " +
                        ",kpi_kpi_assc_keypro kkak  " +
                    "WHERE mk.kpi_id = kkak.child_id)  " +
                "START WITH parent_id = :parentId  " +
                "CONNECT BY PRIOR kpi_id = parent_id) kpi, (  " +
              "SELECT kfa.kpi_id, mf.fact_name,  " +
                "(SELECT ut.table_name FROM user_tables ut WHERE ut.table_name = mf.fact_name AND ROWNUM = 1) table_name  " +
                "FROM mst_fact mf  " +
                    ",kpi_fact_assc kfa  " +
                "WHERE mf.fact_id = kfa.fact_id) fact  " +
              "WHERE kpi.kpi_id = fact.kpi_id (+))  " +
              "ORDER BY lvl DESC, parent_id, kpi_name")
            .addEntity(ListKpiKeypro.class)
            .setInteger("parentId",parentId)
            .list();
  }
  
  /**
   * Get list of option data based on given SQL statement and page no
   * @param sql
   * @param pageNo
   * @return list of option data
   */
  public List<ListOption> getListOption(String sql, int pageNo) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            sql + " ORDER BY name")
            .addEntity(ListOption.class)
            .setFirstResult((pageNo - 1) * RECORDS_OPTION_PER_PAGE)
            .setMaxResults(RECORDS_OPTION_PER_PAGE)
            .list();
  }
  
  /**
   * Get list of option data based on given SQL statement, page no, searching text and records to be excluded
   * @param sql
   * @param pageNo
   * @param searchText
   * @param excludeText
   * @return list of option data
   */
  public List<ListOption> getListOption(String sql, int pageNo, String searchText, String excludeText) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT code, name " +
              "FROM (" + sql + ") " +
              "WHERE UPPER(name) LIKE '%" + searchText.toUpperCase() + "%' " +
                excludeText +
              "ORDER BY name")
            .addEntity(ListOption.class)
            .setFirstResult((pageNo - 1) * RECORDS_OPTION_PER_PAGE)
            .setMaxResults(RECORDS_OPTION_PER_PAGE)
            .list();
  }
  
  /**
   * Get list of table columns property based on given table
   * @param tableName
   * @return list of table columns property
   */
  public List<ColumnProp> getListColumn(String tableName) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT column_id, column_name, data_type " +
              "FROM user_tab_cols " +
              "WHERE table_name = :tableName " +
              "ORDER BY column_id")
            .addEntity(ColumnProp.class)
            .setString("tableName", tableName)
            .list();
  }
  
  /**
   * Get list of table value based on given table, selected columns, where clause statement, order by clause and page no
   * @param tableName
   * @param columnSelect
   * @param whereClause
   * @param orderBy
   * @param pageNo
   * @return list of table value
   */
  public List<TableValueKeypro> getListTableValue(String tableName, String columnSelect, String whereClause, String orderBy, int pageNo) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT ROWID id," + columnSelect + " " +
              "FROM " + tableName + " " +
              "WHERE " + whereClause + " " +
              "ORDER BY " + orderBy)
            .addEntity(TableValueKeypro.class)
            .setFirstResult((pageNo - 1) * RECORDS_PER_PAGE)
            .setMaxResults(RECORDS_PER_PAGE)
            .list();
  }

  /**
   * Get list of KPI column options based on given KPI id
   * @param kpiId
   * @return list of KPI column options
   */
  public List<KpiColumn> getByKpiId(int kpiId) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT kolom_id id, kolom_name name, kolom_code code, type_kolom type, list_query, data_type " +
              "FROM mst_kolom " +            
              "WHERE (kolom_id IN ( " +
                "SELECT kolom_id " +
                  "FROM kpi_kolom_assc " +
                  "WHERE kpi_id = :kpiId) " +
                "OR type_kolom = 1) " +
                "AND list_query IS NOT NULL " +
              "ORDER BY id")
            .addEntity(KpiColumn.class)
            .setInteger("kpiId", kpiId)
            .list();
  }
  
  /**
   * Get amount of table records based on given table and where clause statement
   * @param tableName
   * @param whereClause
   * @return amount of table records
   */
  public Integer getRecordCount(String tableName, String whereClause) {
    BigDecimal bd = (BigDecimal) sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT count(*) " +
              "FROM " + tableName + " " +
              "WHERE " + whereClause)
            .list().get(0);
    return bd.intValue();
  }
  
  /**
   * Get amount of table record options based on given SQL statement, searched text and records to be excluded
   * @param sql
   * @param searchText
   * @param excludeText
   * @return amount of table record options
   */
  public Integer getRecordCountList(String sql, String  searchText, String excludeText) {
    BigDecimal bd = (BigDecimal) sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT count(*) " +
              "FROM (" + sql + ") " +
              "WHERE UPPER(name) LIKE '%" + searchText.toUpperCase() + "%' " +
              excludeText)
            .list().get(0);
    return bd.intValue();
  }
  
  /**
   * Execute submitted job
   * @param userName
   * @param fileName
   * @param query
   * @param columns
   * @param receipt 
   */
  public void execSubmitJob(String userName, String fileName, String query, String columns, String receipt) {
    sessionFactory.getCurrentSession().createSQLQuery(
        " { call fif_focus.keypro_submit_job_proc(:userName,:fileName,:query,:columns,:receipt,:email) } ")
            .setString("userName", userName)
            .setString("fileName", fileName)
            .setString("query", query)
            .setString("columns", columns)
            .setString("receipt",receipt)
            .setString("email","")
            .executeUpdate();
  }
  
  /**
   * Get list of SQL query based on given column
   * @param columnCode
   * @return list of SQL query
   */
  public String getListQueryByColumn(String columnCode) {
    return (String) sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT list_query " +
              "FROM fif_focus.mst_kolom " +
              "WHERE kolom_code = :columnCode")
            .setString("columnCode",columnCode)
            .list().get(0);
  }
}
