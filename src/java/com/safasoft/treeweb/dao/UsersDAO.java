/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.dao;


import com.safasoft.treeweb.bean.ColumnPropTemp;
import com.safasoft.treeweb.bean.ListDataTemp;
import com.safasoft.treeweb.bean.ListKpiTemp;
import com.safasoft.treeweb.bean.TableContentTemp;
import com.safasoft.treeweb.bean.Users;
import com.safasoft.treeweb.bean.support.ListBean;
import com.safasoft.treeweb.bean.support.UserProfileBean;
import com.safasoft.treeweb.util.BaseDAO;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @created Jun 22, 2015
 * @author awal
 */

/**
 * A custom DAO for accessing data from the database.
 *
 */
@Repository("usersDAO")
public class UsersDAO extends BaseDAO<Users> {

  public List<UserProfileBean> getUserProfile(String userName) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT ROWNUM id, user_name, layer_code dept_code, layer_name dept_name, layer_code||'-'||level_code member_code, level_desc member_name, " +
              "fifapps_code member_list_code, node_name member_list_name, parent_level_code parent_member_code " +
              "FROM ( " +
              "SELECT DISTINCT user_name, layer_code, layer_name, level_code, level_desc, " +
                "fifapps_code, node_name, " +
                "LEVEL lvl, CONNECT_BY_ROOT level_code parent_level_code " +
                "FROM ( " +
                "SELECT lum.user_name, ml.layer_code, ml.layer_name, mlv.level_code, mlv.level_desc, " +
                  "DECODE(mlv.level_id,4,mn1.fifapps_code||'p',mn1.fifapps_code) fifapps_code, mn1.node_name, " +
                  "nna.node_child, nna.node_parent, mn.level_id mn_level_id, lum.level_id lum_level_id " +
                  "FROM mst_layer ml, mst_level mlv, mst_node mn, node_node_assc nna, layer_user_map lum, mst_node mn1 " +
                  "WHERE lum.user_name = :userName " +
                    "AND ml.layer_id = lum.layer_id " +
                    "AND ml.layer_id = nna.layer_id " +
                    "AND mlv.level_id = mn.level_id " +
                    "AND mn.node_id = nna.node_child " +
                    "AND lum.node_id = mn1.node_id) " +
                "CONNECT BY PRIOR node_child = node_parent " +
                "START WITH mn_level_id = lum_level_id " +
		"ORDER BY layer_name, lvl)")
            .addEntity(UserProfileBean.class)
            .setString("userName", userName)
            .list();
  }

  public List<ListKpiTemp> getListKpi(String dept, String members, String coy, String lob, String dtsTable, String dtsTableLastMonth) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT name, id, parent, kpi_type, kpi, members, coy, lob, target, actual, last_month, achieve, growth, url, dept, " +
              "CASE " +
                "WHEN parent = -1 THEN 'cyan' " +
                "WHEN (data_kpi IS NULL OR actual = 0 OR type_kpi = 'N') THEN 'grey' " +
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
                "WHEN (data_kpi IS NULL OR last_month = 0) THEN 'btn btn-default btn-sm' " +
                "WHEN type_kpi IN ('X','D') THEN " +
                  "CASE " +
                    "WHEN actual < last_month THEN 'btn btn-success btn-sm' " +
                    "WHEN actual > last_month THEN 'btn btn-danger btn-sm' " +
                    "ELSE 'btn btn-warning btn-sm' " +
                  "END " +
                "ELSE " +
                  "CASE " +
                    "WHEN actual > last_month THEN 'btn btn-success btn-sm' " +
                    "WHEN actual < last_month THEN 'btn btn-danger btn-sm' " +
                    "ELSE 'btn btn-warning btn-sm' " +
                  "END " +
              "END button, " +
              "CASE " +
                "WHEN (data_kpi IS NULL OR last_month = 0) THEN 'glyphicon glyphicon-minus-sign' " +
                "WHEN type_kpi IN ('X','D') THEN " +
                  "CASE " +
                    "WHEN actual < last_month THEN 'glyphicon glyphicon-circle-arrow-up' " +
                    "WHEN actual > last_month THEN 'glyphicon glyphicon-circle-arrow-down' " +
                    "ELSE 'glyphicon glyphicon-circle-arrow-right' " +
                  "END " +
                "ELSE " +
                  "CASE " +
                    "WHEN actual > last_month THEN 'glyphicon glyphicon-circle-arrow-up' " +
                    "WHEN actual < last_month THEN 'glyphicon glyphicon-circle-arrow-down' " +
                    "ELSE 'glyphicon glyphicon-circle-arrow-right' " +
                  "END " +
              "END icon " +
              "FROM ( " +
              "SELECT " +
                "kpi.child_id id, kpi.parent_id parent, kpi.child_id kpi, kpi.type_kpi, kpi.lvl, " +
                ":members members, :coy coy, :lob lob, '#' url, :dept dept, " +
                "data.kpi data_kpi, NVL(data.target,0) target, NVL(data.actual,0) actual, " +
                "NVL(data.last_month,0) last_month, NVL(data.batas_atas,0) batas_atas, NVL(data.batas_bawah,0) batas_bawah, " +
                "'kpi_'||:members||'_'||:coy||'_'||:lob kpi_type, " +
                "CASE " +
                  "WHEN (kpi.parent_id IS NULL OR kpi.parent_id = -1) THEN 'KPI '|| " +
                    "(SELECT node_name FROM mst_node WHERE fifapps_code = :members AND ROWNUM = 1)||' '|| " +
                    "(SELECT coy_short_name FROM fs_mst_company_vw WHERE coy_id = :coy AND ROWNUM = 1)||' '|| " +
                    "UPPER(:lob) " +
                  "ELSE kpi.kpi_name " +
                "END name, " +
                "CASE " +
                  "WHEN data.kpi IS NULL THEN 0 " +
                  "WHEN kpi.type_kpi IN ('X','Y','N') THEN 0 " +
                  "WHEN kpi.type_kpi = 'D' THEN " +
                    "CASE " +
                      "WHEN data.actual = 0 THEN 0 " +
                      "ELSE ROUND(data.target/data.actual*100,2) " +
                    "END " +
                  "ELSE " +
                    "CASE " +
                      "WHEN data.target = 0 THEN 0 " +
                      "ELSE ROUND(data.actual/data.target*100,2) " +
                    "END " +
                "END achieve, " +
                "CASE " +
                  "WHEN data.kpi IS NULL THEN 0 " +
                  "WHEN data.last_month = 0 THEN 0 " +
                  "ELSE ROUND((data.actual-data.last_month)/data.last_month*100,2) " +
                "END growth " +
                "FROM ( " +
                "SELECT " +
                  "abd.kpi_id kpi, " +
                  "NVL(abd.target,0) target, " +
                  "NVL(abd.actual,0) actual, " +
                  "NVL(abd.batas_atas,0) batas_atas, " +
                  "NVL(abd.batas_bawah,0) batas_bawah, " +
                    "NVL((SELECT actual FROM "+dtsTableLastMonth+" " +
                      "WHERE kpi_id = abd.kpi_id " +
                      "AND node_id = abd.node_id  " +
                      "AND coy_id = abd.coy_id  " +
                      "AND buss_unit = abd.buss_unit  " +
                      "AND ROWNUM = 1),0) last_month, " +
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
                    "SELECT 0 child_id, -1 parent_id, 'KPI NASIONAL' kpi_name, NULL type_kpi, 0 lvl " +
                      "FROM DUAL " +
                    "UNION ALL " +
                    "SELECT child_id, parent_id, kpi_name, type_kpi, LEVEL lvl " +
                      "FROM ( " +
                      "SELECT kka.parent_id, kka.child_id, mk.kpi_name, mk.type_kpi " +
                        "FROM kpi_kpi_assc kka " +
                            ",mst_kpi mk " +
                        "WHERE kka.child_id = mk.kpi_id " +
                          "AND mk.system_id IN (2,3)) " +
                    "START WITH parent_id = 0 " +
                    "CONNECT BY PRIOR child_id = parent_id) kpi " +
                "WHERE data.kpi (+) = kpi.child_id) " +
              "ORDER BY lvl DESC, parent, name")
            .addEntity(ListKpiTemp.class)
            .setString("dept", dept)
            .setString("members", members)
            .setString("coy", coy)
            .setString("lob", lob)
            .list();
  }

  public List<ListKpiTemp> getListKpi(String dept, String members, String coy, String lob, String kpi, String dtsTable, String dtsTableLastMonth) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT name, id, parent, kpi_type, kpi, members, coy, lob, target, actual, last_month, url, dept, achieve, growth, " +
              "CASE " +
                "WHEN parent = -1 THEN 'cyan' " +
                "WHEN (data_id IS NULL OR actual = 0 OR type_kpi = 'N') THEN 'grey' " +
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
                "WHEN (data_id IS NULL OR last_month = 0) THEN 'btn btn-default btn-sm' " +
                "WHEN type_kpi IN ('X','D') THEN " +
                  "CASE " +
                    "WHEN actual < last_month THEN 'btn btn-success btn-sm' " +
                    "WHEN actual > last_month THEN 'btn btn-danger btn-sm' " +
                    "ELSE 'btn btn-warning btn-sm' " +
                  "END " +
                "ELSE " +
                  "CASE " +
                    "WHEN actual > last_month THEN 'btn btn-success btn-sm' " +
                    "WHEN actual < last_month THEN 'btn btn-danger btn-sm' " +
                    "ELSE 'btn btn-warning btn-sm' " +
                  "END " +
              "END button, " +
              "CASE " +
                "WHEN (data_id IS NULL OR last_month = 0) THEN 'glyphicon glyphicon-minus-sign' " +
                "WHEN type_kpi IN ('X','D') THEN " +
                  "CASE " +
                    "WHEN actual < last_month THEN 'glyphicon glyphicon-circle-arrow-up' " +
                    "WHEN actual > last_month THEN 'glyphicon glyphicon-circle-arrow-down' " +
                    "ELSE 'glyphicon glyphicon-circle-arrow-right' " +
                  "END " +
                "ELSE " +
                  "CASE " +
                    "WHEN actual > last_month THEN 'glyphicon glyphicon-circle-arrow-up' " +
                    "WHEN actual < last_month THEN 'glyphicon glyphicon-circle-arrow-down' " +
                    "ELSE 'glyphicon glyphicon-circle-arrow-right' " +
                  "END " +
              "END icon " +
              "FROM ( " +
              "SELECT mst.name, mst.id, mst.parent, :kpi kpi, :members members, :coy coy, :lob lob, :dept dept, " +
                "'kpi_'||:members||'_'||:kpi||'_'||:coy||'_'||:lob kpi_type, " +
                "'#' url, data.id data_id, data.type_kpi, NVL(data.last_month,0) last_month,  " +
                "NVL(data.target,0) target, NVL(data.actual,0) actual, " +
                "NVL(data.batas_atas,0) batas_atas, NVL(data.batas_bawah,0) batas_bawah, " +
                "CASE " +
                  "WHEN data.id IS NULL THEN 0 " +
                  "WHEN data.type_kpi IN ('X','Y','N') THEN 0 " +
                  "WHEN data.type_kpi = 'D' THEN " +
                    "CASE " +
                      "WHEN data.actual = 0 THEN 0 " +
                      "ELSE ROUND(data.target/data.actual*100,2) " +
                    "END " +
                  "ELSE " +
                  "CASE " +
                    "WHEN data.target = 0 THEN 0 " +
                    "ELSE ROUND(data.actual/data.target*100,2) " +
                  "END " +
                "END achieve, " +
                "CASE " +
                  "WHEN data.id IS NULL THEN 0 " +
                  "WHEN data.last_month = 0 THEN 0 " +
                  "ELSE ROUND((data.actual-data.last_month)/data.last_month*100,2) " +
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
                "SELECT abd.node_id id, abd.target, abd.actual, abd.batas_atas, abd.batas_bawah, mk.type_kpi, " +
                  "NVL((SELECT actual FROM "+dtsTableLastMonth+" " +
                    "WHERE kpi_id = abd.kpi_id " +
                    "AND node_id = abd.node_id  " +
                    "AND coy_id = abd.coy_id  " +
                    "AND buss_unit = abd.buss_unit  " +
                    "AND ROWNUM = 1),0) last_month " +
                  "FROM mst_kpi mk " +
                      ","+dtsTable+" abd " +
                  "WHERE mk.kpi_id = :kpi " +
                    "AND abd.coy_id = :coy " +
                    "AND abd.buss_unit = UPPER(:lob) " +
                    "AND mk.kpi_id = abd.kpi_id " +
                ") data " +
                "WHERE data.id (+) = mst.id " +
                "ORDER BY lvl DESC, mst.parent, mst.name)")
            .addEntity(ListKpiTemp.class)
            .setString("dept", dept)
            .setString("members", members)
            .setString("coy", coy)
            .setString("lob", lob)
            .setString("kpi", kpi)
            .list();
  }
  
  public TableContentTemp getListTableValue(String tableName, int pageNo) {
    int resultPerPage = 10;
    int firstResult = (pageNo - 1) * resultPerPage;
    int maxId = 0;
    ColumnPropTemp cptId = null;
    String delimiter = "||'|'||";
    List<ColumnPropTemp> columnProp = getListColumn(tableName);
    StringBuilder columns = new StringBuilder("");
    for(ColumnPropTemp cpt : columnProp) {
      columns.append(cpt.getColumnName());
      columns.append(delimiter);
      if(cpt.getConstraintType().equals("P"))
        cptId = cpt;
    }
    List<ListDataTemp> contents = sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT " +
              (cptId == null ? "ROWID" : 
                (cptId.getDataType().equals("NUMBER") ? "TO_CHAR(" + cptId.getColumnName() + ")" : cptId.getColumnName())) + " id, " +
              columns.substring(0, columns.lastIndexOf(delimiter)) + " group_text " +
              "FROM " + tableName + " " +
              "ORDER BY " + (cptId == null ? "group_text" : cptId.getColumnName()))
            .addEntity(ListDataTemp.class)
            .setFirstResult(firstResult)
            .setMaxResults(resultPerPage)
            .list();
    BigDecimal bd = (BigDecimal) sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT COUNT(*) cnt FROM " + tableName)
            .list()
            .get(0);
    int recordCount = bd.intValue();
    int maxPage = recordCount == 0 ? 1 : (recordCount/resultPerPage + (recordCount%resultPerPage == 0 ? 0 : 1));
    if(cptId != null && cptId.getDataType().equals("NUMBER")) {
      BigDecimal bdm = (BigDecimal) sessionFactory.getCurrentSession().createSQLQuery(
              "SELECT MAX("+cptId.getColumnName()+")+1 max_id FROM " + tableName)
              .list()
              .get(0);
      maxId = bdm.intValue();
    }
    TableContentTemp tct = new TableContentTemp();
    tct.setColumns(columnProp);
    tct.setContents(contents);
    tct.setMaxPage(maxPage);
    tct.setMaxId(maxId);
    return tct;
  }

  public void saveTable(String sql) {
    sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();

  }

  public List<ListBean> getListDdl(String tableName, String code, String name) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT " + code + " code, " + name + " name " +
              "FROM " + tableName + " " +
              "ORDER BY 1")
            .addEntity(ListBean.class)
            .list();
  }

  private List<ColumnPropTemp> getListColumn(String tableName) {
    return sessionFactory.getCurrentSession().createSQLQuery(
            "SELECT utc.column_id, utc.column_name, utc.data_type, NVL(uc.constraint_type,'N') constraint_type, NVL(( " +
              "SELECT ucc1.table_name " +
                "FROM user_cons_columns ucc1 " +
                "WHERE ucc1.constraint_name = uc.r_constraint_name " +
                  "AND ROWNUM = 1),'N/A') table_ref, NVL(( " +
              "SELECT utc1.column_name " +
                "FROM user_tab_cols utc1, user_cons_columns ucc2 " +
                "WHERE utc1.column_id = 1 " +
                  "AND ucc2.constraint_name = uc.r_constraint_name " +
                  "AND utc1.table_name = ucc2.table_name " +
                  "AND ROWNUM = 1),'N/A') column1_ref, NVL(( " +
              "SELECT utc2.column_name " +
                "FROM user_tab_cols utc2, user_cons_columns ucc3 " +
                "WHERE utc2.column_id = 2 " +
                  "AND ucc3.constraint_name = uc.r_constraint_name " +
                  "AND utc2.table_name = ucc3.table_name " +
                  "AND ROWNUM = 1),'N/A') column2_ref " +
              "FROM user_tab_cols utc " +
                  ",user_cons_columns ucc " +
                  ",user_constraints uc " +
              "WHERE utc.table_name = :tableName " +
                "AND utc.table_name = ucc.table_name (+) " +
                "AND utc.column_name = ucc.column_name (+) " +
                "AND uc.table_name (+) = ucc.table_name " +
                "AND uc.constraint_name (+) = ucc.constraint_name " +
              "ORDER BY utc.column_id")
            .addEntity(ColumnPropTemp.class)
            .setString("tableName", tableName)
            .list();
  }

}