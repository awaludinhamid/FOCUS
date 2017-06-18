/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.dao;

import com.safasoft.treeweb.bean.support.ListKpi;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @created May 4, 2017
 * @author awal
 */
@Repository("genericDAO")
public class GenericDAO {
  
  @Autowired
  protected SessionFactory sessionFactory;

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
              "DECODE(parent,-1,'purple',DECODE(color_a,'R','red','G','green','Y','yellow','X','grey','B','blue','grey')) color, " +
              "DECODE(color_b,'R','red','G','green','Y','yellow','X','grey','B','blue','grey') color_square, " +
              "DECODE(color_e,'R','red','G','green','Y','yellow','X','grey','B','blue','grey') color_square_in, " +
              "DECODE(color_c,'R','red','G','green','Y','yellow','X','grey','B','blue','grey') color_icon, " +
              "DECODE(color_d,'R','red','G','green','Y','yellow','X','grey','B','blue','grey') color_icon_in, " +
              "target, actual, last_month, achieve, achieve_last_month, growth, batas_atas, batas_bawah, system_id, " +
              "CASE " +
                "WHEN INSTR('"+dtsTable+"','_R') > 0 THEN TO_CHAR(date_populate,'DD-MON-YYYY HH24:MI:SS') " +//applied to realtime, aware of table name changed
                "ELSE TO_CHAR(date_populate,'DD-MON-YYYY') " +
              "END date_populate, " +
              /*"CASE " +
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
              "END color, " +*/
              "CASE " +
                "WHEN (data_kpi IS NULL OR actual IS NULL OR last_month IS NULL) THEN 'btn btn-default btn-sm' " +
                "WHEN type_growth IN ('B','C') THEN " +
                  "DECODE(SIGN(growth),-1,'btn btn-success btn-sm',1,'btn btn-danger btn-sm','btn btn-warning btn-sm') " +
                "ELSE " +
                  "DECODE(SIGN(growth),1,'btn btn-success btn-sm',-1,'btn btn-danger btn-sm','btn btn-warning btn-sm') " +
              "END button, " +
              /*"CASE " +
                "WHEN (data_kpi IS NULL OR actual IS NULL OR last_month IS NULL) THEN 'glyphicon glyphicon-minus-sign' " +
                "WHEN type_growth IN ('B','C') THEN " +
                  "DECODE(SIGN(growth),-1,'glyphicon glyphicon-circle-arrow-up',1,'glyphicon glyphicon-circle-arrow-down'," +
                    "'glyphicon glyphicon-circle-arrow-right') " +
                "ELSE " +
                  "DECODE(SIGN(growth),1,'glyphicon glyphicon-circle-arrow-up',-1,'glyphicon glyphicon-circle-arrow-down'," +
                    "'glyphicon glyphicon-circle-arrow-right') " +
              "END icon " +*/
              "'glyphicon glyphicon-'||DECODE(color_c,'R','circle-arrow-down','G','circle-arrow-up','minus-sign') icon, " +
              "'glyphicon glyphicon-'||DECODE(color_d,'R','arrow-down','G','arrow-up','minus') icon_in, " +
              "color_a, color_b, color_c, color_d, color_e, speed_a, speed_b, " +
              "proyeksi, actual_before_yest, achieve_proyeksi " +
              "FROM ( " +
              "SELECT " +
                "kpi.child_id id, kpi.parent_id parent, kpi.child_id kpi, kpi.type_kpi, kpi.satuan, kpi.lvl, " +
                ":members members, :coy coy, :lob lob, '#' url, :dept dept, " +
                "data.kpi data_kpi, target, actual, " +
                "color_a, color_b, color_c, color_d, color_e, speed_a, speed_b, " +
                "proyeksi, actual_before_yest, achieve_proyeksi, " +
                "last_month, batas_atas, batas_bawah, date_populate, " +
                "'kpi_'||:members||'_'||:coy||'_'||:lob kpi_type, " +
                "kpi.system_id, kpi.type_growth, " +
                "CASE " +
                  "WHEN (kpi.parent_id IS NULL OR kpi.parent_id = -1) THEN 'KPI' " +
                  "ELSE kpi.kpi_name " +
                "END name, " +
                /*"CASE " +
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
                "END achieve, " +*/
                "achievement achieve, " +
                "achievement_m1 achieve_last_month, " +
                "CASE " +
                  "WHEN data.kpi IS NULL OR data.last_month IS NULL THEN NULL " +
                  "WHEN kpi.type_growth IN ('C','D') THEN data.actual-data.last_month " +
                  "ELSE DECODE(data.last_month,0,0,ROUND((data.actual-data.last_month)/data.last_month*100,2)) " +
                "END growth " +
                "FROM ( " +
                "SELECT " +
                  "a color_a, b color_b, c color_c, d color_d, e color_e, speed_a, speed_b, " +
                  "proyeksi, actual_d_m_2 actual_before_yest, achieve_proyeksi, " +
                  "achievement, achievement_m1, " +
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
              "target, actual, last_month, achieve, achieve_last_month, growth, batas_atas, batas_bawah, 0 system_id, " +
              "CASE " +
                "WHEN INSTR('"+dtsTable+"','_R') > 0 THEN TO_CHAR(date_populate,'DD-MON-YYYY HH24:MI:SS') " +//applied to realtime, aware of table name changed
                "ELSE TO_CHAR(date_populate,'DD-MON-YYYY') " +
              "END date_populate, " +
              "DECODE(parent,-1,'purple',DECODE(color_a,'R','red','G','green','Y','yellow','X','grey','B','blue','grey')) color, " +
              "DECODE(color_b,'R','red','G','green','Y','yellow','X','grey','B','blue','grey') color_square, " +
              "DECODE(color_e,'R','red','G','green','Y','yellow','X','grey','B','blue','grey') color_square_in, " +
              "DECODE(color_c,'R','red','G','green','Y','yellow','X','grey','B','blue','grey') color_icon, " +
              "DECODE(color_d,'R','red','G','green','Y','yellow','X','grey','B','blue','grey') color_icon_in, " +
              /*"CASE " +
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
              "END color, " +*/
              "CASE " +
                "WHEN (data_id IS NULL OR actual IS NULL OR last_month IS NULL) THEN 'btn btn-default btn-sm' " +
                "WHEN type_growth IN ('B','C') THEN " +
                  "DECODE(SIGN(growth),-1,'btn btn-success btn-sm',1,'btn btn-danger btn-sm','btn btn-warning btn-sm') " +
                "ELSE " +
                  "DECODE(SIGN(growth),1,'btn btn-success btn-sm',-1,'btn btn-danger btn-sm','btn btn-warning btn-sm') " +
              "END button, " +
              /*"CASE " +
                "WHEN (data_id IS NULL OR actual IS NULL OR last_month IS NULL) THEN 'glyphicon glyphicon-minus-sign' " +
                "WHEN type_growth IN ('B','C') THEN " +
                  "DECODE(SIGN(growth),-1,'glyphicon glyphicon-circle-arrow-up',1,'glyphicon glyphicon-circle-arrow-down'," +
                    "'glyphicon glyphicon-circle-arrow-right') " +
                "ELSE " +
                  "DECODE(SIGN(growth),1,'glyphicon glyphicon-circle-arrow-up',-1,'glyphicon glyphicon-circle-arrow-down'," +
                    "'glyphicon glyphicon-circle-arrow-right') " +
              "END icon " +*/
              "'glyphicon glyphicon-'||DECODE(color_c,'R','circle-arrow-down','G','circle-arrow-up','minus-sign') icon, " +
              "'glyphicon glyphicon-'||DECODE(color_d,'R','arrow-down','G','arrow-up','minus') icon_in, " +
              "color_a, color_b, color_c, color_d, color_e, speed_a, speed_b, " +
              "proyeksi, actual_before_yest, achieve_proyeksi " +
              "FROM ( " +
              "SELECT mst.name, mst.id, mst.parent, :kpi kpi, :members members, :coy coy, :lob lob, :dept dept, " +
                "'kpi_'||:members||'_'||:kpi||'_'||:coy||'_'||:lob kpi_type, " +
                "'#' url, data.id data_id, data.type_kpi, data.satuan, data.type_growth, data.last_month, data.date_populate, " +
                "target, actual, " +
                "batas_atas, batas_bawah, " +
                "data.color_a, data.color_b, data.color_c, data.color_d, data.color_e, data.speed_a, data.speed_b, " +
                "data.proyeksi, data.actual_before_yest, data.achieve_proyeksi, " +
                /*"CASE " +
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
                "END achieve, " +*/
                "data.achievement achieve, " +
                "data.achievement_m1 achieve_last_month, " +
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
                  "abd.a color_a, abd.b color_b, abd.c color_c, abd.d color_d, abd.e color_e, abd.speed_a, abd.speed_b, " +
                  "abd.proyeksi, abd.actual_d_m_2 actual_before_yest, abd.achieve_proyeksi, " +
                  "abd.achievement, abd.achievement_m1, " +
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

}
