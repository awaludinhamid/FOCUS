/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.dao;


import com.safasoft.treeweb.bean.support.ColumnProp;
import com.safasoft.treeweb.bean.support.ListKpi;
import com.safasoft.treeweb.bean.support.TableContent;
import com.safasoft.treeweb.bean.Users;
import com.safasoft.treeweb.bean.support.ColumnCons;
import com.safasoft.treeweb.bean.support.ListBean;
import com.safasoft.treeweb.bean.support.TableValue;
import com.safasoft.treeweb.bean.support.UploadTableBean;
import com.safasoft.treeweb.bean.support.UserProfileBean;
import com.safasoft.treeweb.util.BaseDAO;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * Users DAO
 * A custom DAO for accessing data from the database.
 * @created Jun 22, 2015
 * @author awal
 */
@Repository("usersDAO")
public class UsersDAO extends BaseDAO<Users> {

  
  private final String DELIMITER_COL = ",";
  private final int RESULT_PER_PAGE = 10;
  /**
   * Generate user profile (access to view data: layer, level, etc)
   * @param userName
   * @return list of UserProfileBean
   */
  public List<UserProfileBean> getUserProfile(String userName) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            /*"SELECT ROWNUM id, user_name, layer_code dept_code, layer_name dept_name, layer_code||'-'||level_code member_code, level_desc member_name, " +
              "fifapps_code member_list_code, node_name member_list_name, parent_level_code parent_member_code " +
              "FROM ( " +
              "SELECT DISTINCT user_name, layer_code, layer_name, level_code, level_desc, " +
                "fifapps_code, node_name, " +
                "LEVEL lvl, CONNECT_BY_ROOT level_code parent_level_code " +
                "FROM ( " +
                "SELECT lum.user_name, ml.layer_code, ml.layer_name, mlv.level_code, mlv.level_desc, " +
                  "DECODE(mlv.level_id,4,mn1.fifapps_code||'p',mn1.fifapps_code) fifapps_code, mn1.node_name, " +
                  "nna.node_child, nna.node_parent, mn.level_id mn_level_id, mn1.level_id mn1_level_id " +
                  "FROM mst_layer ml, mst_level mlv, mst_node mn, node_node_assc nna, ff_layer_user_map lum, mst_node mn1 " +
                  "WHERE lum.user_name = :userName " +
                    "AND ml.layer_id = lum.layer_id " +
                    "AND ml.layer_id = nna.layer_id " +
                    "AND mlv.level_id = mn.level_id " +
                    "AND mn.node_id = nna.node_child " +
                    "AND lum.node_id = mn1.node_id) " +
                "CONNECT BY PRIOR node_child = node_parent " +
                "START WITH mn_level_id = mn1_level_id " +
		"ORDER BY layer_name, lvl)"*/
            "SELECT ROWNUM id, user_name, dept_code, dept_name, member_code, member_name, member_list_code, member_list_name, parent_member_code " +
              "FROM ( " +
              "SELECT tree.user_name, ml.layer_code dept_code, ml.layer_name dept_name, " +
                "ml.layer_code||'-'||mlv.level_code member_code, level_desc member_name, " +
                "DECODE(mlv.level_id,4,fifapps_code||'p',fifapps_code) member_list_code, " +
                "node_name member_list_name, " +
                "tree.level_code parent_member_code " +
                "FROM ( " +
                "SELECT DISTINCT user_name, child_level, parent_level, layer_id, node_id, level_code, " +
                  "fifapps_code, node_name, lvl FROM ( " +
                  "SELECT user_name, node_id, child_level,parent_level,layer_id, fifapps_code, node_name, " +
                    "level_code, LEVEL lvl " +
                    "FROM ( " +
                    "SELECT lum.user_name, lum.node_id, ml.level_code, ml.level_id, lla.child_level, lla.parent_level, lla.layer_id, " +
                      "mn.fifapps_code, mn.node_name " + 
                      "FROM ff_layer_user_map lum " +
                          ",level_level_assc lla " +
                          ",mst_level ml " +
                          ",mst_node mn " +
                      "WHERE lum.user_name = :userName " +
                        "AND lum.layer_id = lla.layer_id " +
                        "AND ml.level_id = mn.level_id " +
                        "AND mn.node_id = lum.node_id) " +
                    "CONNECT BY PRIOR child_level = parent_level " +
                    "START WITH child_level = 1 " +
                ")) tree " +
                ",mst_layer ml " +
                ",mst_level mlv " +
                "WHERE ml.layer_id = tree.layer_id " +
                  "AND mlv.level_id = tree.child_level " +
                "ORDER BY ml.layer_name, tree.lvl)"
            )
            .addEntity(UserProfileBean.class)
            .setString("userName", userName)
            .list();
  }

  /**
   * Generate list of KPI data, breakdown by KPI hierarchy
   * @param dept
   * @param members
   * @param coy
   * @param lob
   * @param dtsTable
   * @param dtsTableLastMonth
   * @return list of ListKpi object
   */
  public List<ListKpi> getListKpi(String dept, String members, String coy, String lob, String dtsTable, String dtsTableLastMonth) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT name, id, parent, kpi_type, kpi, members, coy, lob, url, dept, type_kpi, satuan, " +
              "target, actual, last_month, achieve, growth, batas_atas, batas_bawah, system_id, " +
              "CASE " +
                "WHEN INSTR('"+dtsTable+"','_R') > 0 THEN TO_CHAR(date_populate,'DD-MON-YYYY HH24:MI:SS') " +//applied to realtime, aware of table name changed
                "ELSE TO_CHAR(date_populate,'DD-MON-YYYY') " +
              "END date_populate, " +
              "CASE " +
                "WHEN parent = -1 THEN 'purple' " +
                "WHEN (data_kpi IS NULL OR actual IS NULL OR type_kpi = 'N') THEN 'grey' " +
                "WHEN ((target IS NULL AND type_kpi IN ('D','I')) OR batas_atas IS NULL OR batas_bawah IS NULL) THEN 'blue' " +
                "WHEN type_kpi = 'X' THEN " +
                  "CASE " +
                    "WHEN actual <= batas_bawah THEN 'green' " +
                    "WHEN actual > batas_atas THEN 'red' " +
                    "ELSE 'yellow' " +
                  "END " +
                "WHEN type_kpi = 'Y' THEN " +
                  "CASE " +
                    "WHEN actual >= batas_atas THEN 'green' " +
                    "WHEN actual < batas_bawah THEN 'red' " +
                    "ELSE 'yellow' " +
                  "END " +
                "ELSE " +
                  "CASE " +
                    "WHEN achieve >= batas_atas THEN 'green' " +
                    "WHEN achieve < batas_bawah THEN 'red' " +
                    "ELSE 'yellow' " +
                  "END " +
              "END color, " +
              "CASE " +
                "WHEN (data_kpi IS NULL OR actual IS NULL OR last_month IS NULL) THEN 'btn btn-default btn-sm' " +
                "WHEN type_growth IN ('B','C') THEN " +
                  "DECODE(SIGN(growth),-1,'btn btn-success btn-sm',1,'btn btn-danger btn-sm','btn btn-warning btn-sm') " +
                "ELSE " +
                  "DECODE(SIGN(growth),1,'btn btn-success btn-sm',-1,'btn btn-danger btn-sm','btn btn-warning btn-sm') " +
              "END button, " +
              "CASE " +
                "WHEN (data_kpi IS NULL OR actual IS NULL OR last_month IS NULL) THEN 'glyphicon glyphicon-minus-sign' " +
                "WHEN type_growth IN ('B','C') THEN " +
                  "DECODE(SIGN(growth),-1,'glyphicon glyphicon-circle-arrow-up',1,'glyphicon glyphicon-circle-arrow-down'," +
                    "'glyphicon glyphicon-circle-arrow-right') " +
                "ELSE " +
                  "DECODE(SIGN(growth),1,'glyphicon glyphicon-circle-arrow-up',-1,'glyphicon glyphicon-circle-arrow-down'," +
                    "'glyphicon glyphicon-circle-arrow-right') " +
              "END icon " +
              "FROM ( " +
              "SELECT " +
                "kpi.child_id id, kpi.parent_id parent, kpi.child_id kpi, kpi.type_kpi, kpi.satuan, kpi.lvl, " +
                ":members members, :coy coy, :lob lob, '#' url, :dept dept, " +
                "data.kpi data_kpi, target, actual, " +
                "last_month, batas_atas, batas_bawah, date_populate, " +
                "'kpi_'||:members||'_'||:coy||'_'||:lob kpi_type, " +
                "kpi.system_id, kpi.type_growth, " +
                "CASE " +
                  "WHEN (kpi.parent_id IS NULL OR kpi.parent_id = -1) THEN 'KPI' " + 
                  /*
                  "WHEN (kpi.parent_id IS NULL OR kpi.parent_id = -1) THEN 'KPI '|| " +
                    "(SELECT node_name FROM mst_node WHERE fifapps_code = :members AND ROWNUM = 1)||' '|| " +
                    "(SELECT name FROM (SELECT '99' code, 'ALL COY' name from dual UNION ALL SELECT coy_id code, coy_short_name name FROM fs_mst_company_vw) WHERE code = :coy AND ROWNUM = 1)||' '|| " +
                    "(SELECT name FROM (SELECT 'alb' code, 'ALL BU' name from dual UNION ALL SELECT LOWER(buss_unit) code, DECODE(buss_unit,'REFI','UFI',buss_unit) name FROM fs_mst_business_unit_vw) WHERE code = :lob AND ROWNUM = 1) " +
                  */
                  "ELSE kpi.kpi_name " +
                "END name, " +
                "CASE " +
                  "WHEN data.kpi IS NULL OR kpi.type_kpi IN ('X','Y','N') THEN NULL " +
                  "WHEN kpi.type_kpi = 'D' THEN " +
                    "CASE " +
                      "WHEN data.actual IS NULL THEN NULL " +
                      "WHEN data.actual = 0 THEN 0 " +
                      "ELSE ROUND(data.target/data.actual*100,2) " +
                    "END " +
                  "ELSE " +
                    "CASE " +
                      "WHEN data.target IS NULL THEN NULL " +
                      "WHEN data.target = 0 THEN 0 " +
                      "ELSE ROUND(data.actual/data.target*100,2) " +
                    "END " +
                "END achieve, " +
                "CASE " +
                  "WHEN data.kpi IS NULL OR data.last_month IS NULL THEN NULL " +
                  "WHEN kpi.type_growth IN ('C','D') THEN data.actual-data.last_month " +
                  "ELSE DECODE(data.last_month,0,0,ROUND((data.actual-data.last_month)/data.last_month*100,2)) " +
                "END growth " +
                "FROM ( " +
                "SELECT " +
                  "abd.kpi_id kpi, " +
                  "target, " +
                  "actual, " +
                  "batas_atas, " +
                  "batas_bawah, " +
                  "date_populate, " +
                    "(SELECT actual FROM "+dtsTableLastMonth+" " +
                      "WHERE kpi_id = abd.kpi_id " +
                      "AND node_id = abd.node_id  " +
                      "AND coy_id = abd.coy_id  " +
                      "AND buss_unit = abd.buss_unit  " +
                      "AND ROWNUM = 1) last_month, " +
                  "mn.node_name " +
                  "FROM "+dtsTable+" abd " +
                      ",mst_node mn " +
                      ",node_node_assc nna " +
                      ",mst_layer ml " +
                  "WHERE ml.layer_code = :dept " +
                    "AND DECODE(mn.level_id,4,mn.fifapps_code||'p',mn.fifapps_code) = :members " +
                    "AND abd.coy_id = :coy " +
                    "AND abd.buss_unit = UPPER(:lob) " +
                    "AND mn.node_id = abd.node_id " +
                    "AND mn.node_id = nna.node_child " +
                    "AND nna.layer_id = ml.layer_id) data, ( " +
                    "SELECT 0 child_id, -1 parent_id, 'KPI NASIONAL' kpi_name, NULL type_kpi, NULL satuan, 0 system_id, NULL type_growth, 0 lvl " +
                      "FROM DUAL " +
                    "UNION ALL " +
                    "SELECT child_id, parent_id, kpi_name, type_kpi, satuan, system_id, type_growth, LEVEL lvl " +
                      "FROM ( " +
                      "SELECT kka.parent_id, kka.child_id, mk.kpi_name, mk.type_kpi, mk.satuan, mk.system_id, mk.type_growth " +
                        "FROM kpi_kpi_assc kka " +
                            ",mst_kpi mk " +
                        "WHERE kka.child_id = mk.kpi_id " +
                          "AND mk.system_id IN (2,3)) " +
                    "START WITH parent_id = 0 " +
                    "CONNECT BY PRIOR child_id = parent_id) kpi " +
                "WHERE data.kpi (+) = kpi.child_id) " +
              "ORDER BY lvl DESC, parent, name")
            .addEntity(ListKpi.class)
            .setString("dept", dept)
            .setString("members", members)
            .setString("coy", coy)
            .setString("lob", lob)
            .list();
  }

  /**
   * Generate list of KPI data, breakdown by level member (area, branch, marketing head, etc)
   * @param dept
   * @param members
   * @param coy
   * @param kpi
   * @param lob
   * @param dtsTable
   * @param dtsTableLastMonth
   * @return list of ListKpi object
   */

  public List<ListKpi> getListKpi(String dept, String members, String coy, String lob, String kpi, String dtsTable, String dtsTableLastMonth) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT name, id, parent, kpi_type, kpi, members, coy, lob, url, dept, type_kpi, satuan, " +
              "target, actual, last_month, achieve, growth, batas_atas, batas_bawah, 0 system_id, " +
              "CASE " +
                "WHEN INSTR('"+dtsTable+"','_R') > 0 THEN TO_CHAR(date_populate,'DD-MON-YYYY HH24:MI:SS') " +//applied to realtime, aware of table name changed
                "ELSE TO_CHAR(date_populate,'DD-MON-YYYY') " +
              "END date_populate, " +
              "CASE " +
                "WHEN parent = -1 THEN 'purple' " +
                "WHEN (data_id IS NULL OR actual IS NULL OR type_kpi = 'N') THEN 'grey' " +
                "WHEN ((target IS NULL AND type_kpi IN ('D','I')) OR batas_atas IS NULL OR batas_bawah IS NULL) THEN 'blue' " +
                "WHEN type_kpi = 'X' THEN " +
                  "CASE " +
                    "WHEN actual <= batas_bawah THEN 'green' " +
                    "WHEN actual > batas_atas THEN 'red' " +
                    "ELSE 'yellow' " +
                  "END " +
                "WHEN type_kpi = 'Y' THEN " +
                  "CASE " +
                    "WHEN actual >= batas_atas THEN 'green' " +
                    "WHEN actual < batas_bawah THEN 'red' " +
                    "ELSE 'yellow' " +
                  "END " +
                "ELSE " +
                  "CASE " +
                    "WHEN achieve >= batas_atas THEN 'green' " +
                    "WHEN achieve < batas_bawah THEN 'red' " +
                    "ELSE 'yellow' " +
                  "END " +
              "END color, " +
              "CASE " +
                "WHEN (data_id IS NULL OR actual IS NULL OR last_month IS NULL) THEN 'btn btn-default btn-sm' " +
                "WHEN type_growth IN ('B','C') THEN " +
                  "DECODE(SIGN(growth),-1,'btn btn-success btn-sm',1,'btn btn-danger btn-sm','btn btn-warning btn-sm') " +
                "ELSE " +
                  "DECODE(SIGN(growth),1,'btn btn-success btn-sm',-1,'btn btn-danger btn-sm','btn btn-warning btn-sm') " +
              "END button, " +
              "CASE " +
                "WHEN (data_id IS NULL OR actual IS NULL OR last_month IS NULL) THEN 'glyphicon glyphicon-minus-sign' " +
                "WHEN type_growth IN ('B','C') THEN " +
                  "DECODE(SIGN(growth),-1,'glyphicon glyphicon-circle-arrow-up',1,'glyphicon glyphicon-circle-arrow-down'," +
                    "'glyphicon glyphicon-circle-arrow-right') " +
                "ELSE " +
                  "DECODE(SIGN(growth),1,'glyphicon glyphicon-circle-arrow-up',-1,'glyphicon glyphicon-circle-arrow-down'," +
                    "'glyphicon glyphicon-circle-arrow-right') " +
              "END icon " +
              "FROM ( " +
              "SELECT mst.name, mst.id, mst.parent, :kpi kpi, :members members, :coy coy, :lob lob, :dept dept, " +
                "'kpi_'||:members||'_'||:kpi||'_'||:coy||'_'||:lob kpi_type, " +
                "'#' url, data.id data_id, data.type_kpi, data.satuan, data.type_growth, data.last_month, data.date_populate, " +
                "target, actual, " +
                "batas_atas, batas_bawah, " +
                "CASE " +
                  "WHEN data.id IS NULL OR data.type_kpi IN ('X','Y','N') THEN NULL " +
                  "WHEN data.type_kpi = 'D' THEN " +
                    "CASE " +
                      "WHEN data.actual IS NULL THEN NULL " +
                      "WHEN data.actual = 0 THEN 0 " +
                      "ELSE ROUND(data.target/data.actual*100,2) " +
                    "END " +
                  "ELSE " +
                    "CASE " +
                      "WHEN data.target IS NULL THEN NULL " +
                      "WHEN data.target = 0 THEN 0 " +
                      "ELSE ROUND(data.actual/data.target*100,2) " +
                    "END " +
                "END achieve, " +
                "CASE " +
                  "WHEN data.id IS NULL OR data.last_month IS NULL THEN NULL " +
                  "WHEN data.type_growth IN ('C','D') THEN data.actual-data.last_month " +
                  "ELSE DECODE(data.last_month,0,0,ROUND((data.actual-data.last_month)/data.last_month*100,2)) " +
                "END growth " +
                "FROM ( " +
                "SELECT DISTINCT parent, name, id, child_code, LEVEL lvl " +
                  "FROM ( " +
                  "SELECT nna.node_parent parent, mn.node_name name, mn.node_id id, " +
                    "DECODE(mn.level_id,4,mn.fifapps_code||'p',mn.fifapps_code) child_code " +
                    "FROM node_node_assc nna " +
                        ",mst_node mn " +
                        ",mst_layer ml " +
                    "WHERE ml.layer_code = :dept " +
                      "AND nna.node_child = mn.node_id " +
                      "AND nna.layer_id = ml.layer_id) " +
                  "START WITH child_code = :members " +
                  "CONNECT BY PRIOR id = parent " +
                ") mst, ( " +
                "SELECT abd.node_id id, abd.target, abd.actual, abd.batas_atas, abd.batas_bawah, abd.date_populate, " +
                  "mk.type_kpi, mk.satuan, mk.type_growth, " +
                  "(SELECT actual FROM "+dtsTableLastMonth+" " +
                    "WHERE kpi_id = abd.kpi_id " +
                    "AND node_id = abd.node_id  " +
                    "AND coy_id = abd.coy_id  " +
                    "AND buss_unit = abd.buss_unit  " +
                    "AND ROWNUM = 1) last_month " +
                  "FROM mst_kpi mk " +
                      ","+dtsTable+" abd " +
                  "WHERE mk.kpi_id = :kpi " +
                    "AND abd.coy_id = :coy " +
                    "AND abd.buss_unit = UPPER(:lob) " +
                    "AND mk.kpi_id = abd.kpi_id " +
                ") data " +
                "WHERE data.id (+) = mst.id " +
                "ORDER BY lvl DESC, mst.parent, mst.name)")
            .addEntity(ListKpi.class)
            .setString("dept", dept)
            .setString("members", members)
            .setString("coy", coy)
            .setString("lob", lob)
            .setString("kpi", kpi)
            .list();
  }
  
  /**
   * Generate list of table properties (column, constraint, etc) and value (data, maximum id, etc) by current page no
   * @param tableName
   * @param pageNo
   * @return TableContent object
   */
  public TableContent getListTableContentByPage(String tableName, int pageNo) {
    //
    int firstResult = (pageNo - 1) * RESULT_PER_PAGE;
    int maxId = 0;
    Map<String,List<ListBean>> dropDownListMap = new HashMap<String,List<ListBean>>();
    Map<String,List<ColumnCons>> columnConsMap = new HashMap<String,List<ColumnCons>>();
    List<ColumnProp> columnsProp = getListColumnProp(tableName);
    ColumnProp columnPK = getPKColumn(tableName,columnsProp);
    Map<String,String> columnSerialMap = getColumnsSerial(columnsProp,columnPK);
    String orderByColumn = getOrderByColumn(tableName,columnsProp,columnPK);
    // generate column constraint list and its dropdown list (specific to constraint type = R)
    for(ColumnProp cp : columnsProp) {
      List<ColumnCons> columnCons = getListColumnCons(tableName,cp.getColumnName());
      columnConsMap.put(cp.getColumnName(),columnCons);
      for(ColumnCons cc : columnCons) {
        if(cc.getConstraintType().equals("R")) 
          dropDownListMap.put(cp.getColumnName(),getListDdl(cc.getTableRef(),cc.getColumn1Ref(),cc.getColumn2Ref()));
      }
    }
    // generate list of data
    List<TableValue> contents = sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT " + columnSerialMap.get("columnsSerialExt") + " " +
              "FROM " + tableName + " " +
              "ORDER BY " + orderByColumn)
            .addEntity(TableValue.class)
            .setFirstResult(firstResult)
            .setMaxResults(RESULT_PER_PAGE)
            .list();
    // generate maximum page and maximum id (specific to PK with NUMBER datatype)
    BigDecimal bd = (BigDecimal) sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT COUNT(*) cnt FROM " + tableName)
            .list()
            .get(0);
    int recordCount = bd.intValue();
    int maxPage = recordCount == 0 ? 1 : (recordCount/RESULT_PER_PAGE + (recordCount%RESULT_PER_PAGE == 0 ? 0 : 1));
    if(columnPK != null && columnPK.getDataType().equals("NUMBER")) {
      BigDecimal bdm = (BigDecimal) sessionFactory.getCurrentSession().createSQLQuery(
              "SELECT NVL(MAX("+columnPK.getColumnName()+"),0)+1 max_id FROM " + tableName)
              .list()
              .get(0);
      maxId = bdm.intValue();
    }
    // filling object into table
    TableContent tct = new TableContent();
    tct.setColumns(columnsProp);
    tct.setContents(contents);
    tct.setMaxPage(maxPage);
    tct.setMaxId(maxId);
    tct.setDropDownList(dropDownListMap);
    tct.setColumnsCons(columnConsMap);
    tct.setColumnsSerial(columnSerialMap.get("columnsSerial"));
    tct.setColumnsSerialExt(columnSerialMap.get("columnsSerialExt"));
    tct.setOrderByColumn(orderByColumn);
    tct.setIdColumn(columnPK == null ? "" : columnPK.getColumnName());
    return tct;
  }

  /**
   * Execute SQL statement into database
   * @param sql 
   */
  public void saveTable(String sql) {
    sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
  }
  
  /**
   * Generate list of given table value 
   * @param tableName
   * @param columnsSerialExt
   * @param orderByColumn
   * @param currPageNoFind
   * @return list of TableValue object
   */
  public List<TableValue> getListTableValue(String tableName, String columnsSerialExt, String orderByColumn, int currPageNoFind) {
    // generate value
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT " + columnsSerialExt +
              " FROM " + tableName +
              " ORDER BY " + orderByColumn)
            .addEntity(TableValue.class)
            .setFirstResult((currPageNoFind - 1) * RESULT_PER_PAGE)
            .setMaxResults(RESULT_PER_PAGE + 1)
            .list();
  }
  
  /**
   * Generate list of given table value including filter
   * @param tableName
   * @param columnsSerialExt
   * @param orderByColumn
   * @param searchText
   * @param currPageNoFind
   * @return list of TableValue object
   */
  public List<TableValue> getListTableValue(String tableName, String columnsSerialExt, String orderByColumn, String searchText, int currPageNoFind) {
    String[] arrColumn = columnsSerialExt.split(",");
    StringBuilder sb = new StringBuilder();
    for(String str : arrColumn) {
      if(str.contains("NULL"))
        break;
      sb.append("UPPER(").append(str.trim().substring(0,str.trim().indexOf(" "))).append(")")
              .append(" LIKE '%").append(searchText.toUpperCase()).append("%' OR ");
    }
    // generate value
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT " + columnsSerialExt +
              " FROM " + tableName +
              " WHERE " + sb.substring(0,sb.lastIndexOf("OR ")) +
              " ORDER BY " + orderByColumn)
            .addEntity(TableValue.class)
            .setFirstResult((currPageNoFind - 1) * RESULT_PER_PAGE)
            .setMaxResults(RESULT_PER_PAGE + 1)
            .list();
  }
  
  /**
   * Generate page no of current record (id)
   * @param tableName
   * @param id
   * @param columnsSerialExt
   * @param orderByColumn
   * @return Integer value
   */
  public Integer getPageNo(String tableName, String id, String columnsSerialExt, String orderByColumn) {
    BigDecimal bdm = (BigDecimal) sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT CEIL(rnum/10) page_no " +
              "FROM ( " +
                "SELECT ROWNUM rnum, id " +
                "FROM ( " +
                "SELECT " + columnsSerialExt + " " +
                  "FROM " + tableName + " " +
                  "ORDER BY " + orderByColumn + ")) " +
              "WHERE id = :id")
            .setString("id",id)
            .list()
            .get(0);
    return bdm.intValue();
  }

  /**
   * List upload table of given user
   * @param userAccess
   * @return 
   */
  public List<UploadTableBean> getListUploadTableByUser(String userAccess) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT id, name, table_name, description, user_access, schema_name, db_link " +
              "FROM ff_upload_table " +
              "WHERE user_access = :userAccess " +
              "OR :userAccess IN ( " +
                "SELECT user_name " +
                  "FROM users " +
                  "WHERE user_access = 1)")
            .addEntity(UploadTableBean.class)
            .setString("userAccess",userAccess)
            .list();
  }

  // Generate drop down list value (code and name) of given table
  private List<ListBean> getListDdl(String tableName, String code, String name) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT " + code + " code, " + name + " name " +
              "FROM " + tableName + " " +
              "ORDER BY 1")
            .addEntity(ListBean.class)
            .list();
  }

  // Generate column property list of given table
  private List<ColumnProp> getListColumnProp(String tableName) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT column_id, column_name, data_type " +
"              FROM user_tab_cols " +
"              WHERE table_name = :tableName " +
"              ORDER BY column_id")
            .addEntity(ColumnProp.class)
            .setString("tableName", tableName)
            .list();
  }

  // Generate column constraint list of given table and column
  private List<ColumnCons> getListColumnCons(String tableName, String columnName) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT uc.constraint_name, uc.constraint_type, " +
              "CASE WHEN uc.constraint_type = 'R' THEN ( " +
                "SELECT ucc1.table_name " +
                  "FROM user_cons_columns ucc1 " +
                  "WHERE ucc1.constraint_name = uc.r_constraint_name " +
                    "AND ROWNUM = 1) " +
                "ELSE 'N/A' " +
              "END table_ref, " +
              "CASE WHEN uc.constraint_type = 'R' THEN ( " +
                "SELECT utc1.column_name " +
                  "FROM user_tab_cols utc1, user_cons_columns ucc2 " +
                  "WHERE utc1.column_id = 1 " +
                    "AND ucc2.constraint_name = uc.r_constraint_name " +
                    "AND utc1.table_name = ucc2.table_name " +
                    "AND ROWNUM = 1) " +
                "ELSE 'N/A' " +
              "END column1_ref, " +
              "CASE WHEN uc.constraint_type = 'R' THEN ( " +
                "SELECT utc2.column_name " +
                  "FROM user_tab_cols utc2, user_cons_columns ucc3 " +
                  "WHERE utc2.column_id = 2 " +
                    "AND ucc3.constraint_name = uc.r_constraint_name " +
                    "AND uc.constraint_type = 'R' " +
                    "AND utc2.table_name = ucc3.table_name " +
                    "AND ROWNUM = 1) " +
                "ELSE 'N/A' " +
              "END column2_ref " +
              "FROM user_constraints uc " +
                  ",user_cons_columns ucc " +
              "WHERE ucc.table_name = :tableName " +
                "AND ucc.column_name = :columnName " +
                "AND ucc.table_name = uc.table_name " +
                "AND ucc.constraint_name = uc.constraint_name")
            .addEntity(ColumnCons.class)
            .setString("tableName", tableName)
            .setString("columnName", columnName)
            .list();
  }
  
  // Generate column serial used in ORDER BY clause
  // (i.e. this method return 'column1, column2', append to ORDER BY, its became 'ORDER BY column1, column2')
  private String getOrderByColumn(String tableName, List<ColumnProp> columnsProp, ColumnProp columnPK) {
    if(columnsProp.size() > 0) {
      StringBuilder orderByColumn = new StringBuilder("");
      for(ColumnProp cp : columnsProp)
        orderByColumn.append(cp.getColumnName()).append(DELIMITER_COL);
      return (columnPK == null ? orderByColumn.substring(0, orderByColumn.lastIndexOf(DELIMITER_COL)) : tableName+"."+columnPK.getColumnName());
    }
    return "1";
  }
  
  // Generate column serial used in SELECT statement
  // (i.e. this method return 'column1, column2', append to SELECT, so its became 'SELECT column1, column2')  
  private Map<String,String> getColumnsSerial(List<ColumnProp> columnsProp, ColumnProp columnPK) {
    int maxCol = 20;//maximum available column per table
    Map<String,String> columnSerialMap = new HashMap<String,String>();
    StringBuilder sbExt = new StringBuilder();
    StringBuilder sb = new StringBuilder();
    for(int idx = 0; idx < columnsProp.size() && idx < maxCol; idx++) {
      if(columnsProp.get(idx).getDataType().equals("NUMBER"))
        sbExt.append("TO_CHAR(").append(columnsProp.get(idx).getColumnName()).append(")");
      else if(columnsProp.get(idx).getDataType().equals("DATE"))
        sbExt.append("TO_CHAR(").append(columnsProp.get(idx).getColumnName()).append(",'DD-MON-YY')");
      else
        sbExt.append(columnsProp.get(idx).getColumnName());
      sbExt.append(" col").append(idx+1).append(DELIMITER_COL);
      sb.append(columnsProp.get(idx).getColumnName()).append(DELIMITER_COL);
    }
    for(int idx = columnsProp.size(); idx < maxCol; idx++)
      sbExt.append(" NULL col").append(idx+1).append(DELIMITER_COL);
    columnSerialMap.put("columnsSerialExt",(columnPK == null ? "ROWID" : 
              (columnPK.getDataType().equals("NUMBER") ? "TO_CHAR(" + columnPK.getColumnName() + ")" :
              columnPK.getColumnName())) +
            " id, " +
            sbExt.substring(0, sbExt.lastIndexOf(DELIMITER_COL)));
    columnSerialMap.put("columnsSerial",sb.substring(0, sb.lastIndexOf(DELIMITER_COL)));
    return columnSerialMap;
  }

  // Generate PK column of given table
  private ColumnProp getPKColumn(String tableName, List<ColumnProp> columnsProp) {
    for(ColumnProp cp : columnsProp) {
      List<ColumnCons> columnCons = getListColumnCons(tableName,cp.getColumnName());
      for(ColumnCons cc : columnCons) {
        if(cc.getConstraintType().equals("P"))
          return cp;
      }
    }
    return null;
  }
}