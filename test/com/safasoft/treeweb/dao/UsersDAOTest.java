/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safasoft.treeweb.dao;

import com.safasoft.treeweb.bean.support.ListBean;
import com.safasoft.treeweb.bean.support.ListKpi;
import com.safasoft.treeweb.bean.support.TableValue;
import com.safasoft.treeweb.bean.support.UserProfileBean;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author awal
 */
public class UsersDAOTest {
  
  public UsersDAOTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of getUserProfile method, of class UsersDAO.
   * @throws java.io.IOException
   * @throws java.lang.ClassNotFoundException
   * @throws java.sql.SQLException
   */
  @Test
  public void testGetUserProfile() throws IOException, ClassNotFoundException, SQLException {
    System.out.println("getUserProfile");
    //expected
    List<UserProfileBean> expResult = new ArrayList<UserProfileBean>();
    String[] names = {"mkt-nas","mkt-reg","mkt-mdlr","mkt-mm","mkt2-nas","mkt2-mm","opr-nas","opr-area","opr-cab",
                      "opr-pos","opr2-nas","opr2-area","opr2-cab","opr2-pos","opr3-nas","opr3-area","opr3-cab","opr3-pos"};
    for(String name : names) {
      UserProfileBean upb = new UserProfileBean();
      upb.setMemberCode(name);
      expResult.add(upb);
    }
    //result
    String userName = "awal";
    Connection conn = getConnection();
    PreparedStatement pstmt = conn.prepareStatement(
            "SELECT ROWNUM id, user_name, layer_code dept_code, layer_name dept_name, layer_code||'-'||level_code member_code, level_desc member_name, " +
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
                  "WHERE lum.user_name = ? " +
                    "AND ml.layer_id = lum.layer_id " +
                    "AND ml.layer_id = nna.layer_id " +
                    "AND mlv.level_id = mn.level_id " +
                    "AND mn.node_id = nna.node_child " +
                    "AND lum.node_id = mn1.node_id) " +
                "CONNECT BY PRIOR node_child = node_parent " +
                "START WITH mn_level_id = mn1_level_id " +
		"ORDER BY layer_name, lvl)");
    pstmt.setString(1, userName);
    ResultSet rs = pstmt.executeQuery();
    List<UserProfileBean> result = new ArrayList<UserProfileBean>();
    while(rs.next()) {
      UserProfileBean upb = new UserProfileBean();
      upb.setMemberCode(rs.getString("member_code"));
      result.add(upb);
    }
    assertEquals(expResult.size(), result.size());
    for(int idxRes = 0; idxRes < expResult.size(); idxRes++) {
      assertEquals(expResult.get(idxRes).getMemberCode(),result.get(idxRes).getMemberCode());
    }
    rs.close();
    pstmt.close();
    conn.close();
    //List<UserProfileBean> result = instance.getUserProfile(userName);
    //assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    //fail("The test case is a prototype.");
  }

  /**
   * Test of getListKpi method, of class UsersDAO.
   * @throws java.io.IOException
   * @throws java.lang.ClassNotFoundException
   * @throws java.sql.SQLException
   */
  @Test
  public void testGetListKpi_6args() throws IOException, ClassNotFoundException, SQLException {
    System.out.println("getListKpi6args");
    //expected
    List<ListKpi> expResult = new ArrayList<ListKpi>();
    String[] names = {"FRP","Jumlah Create SKPC vs PC Existing","PC Active","Rekrutmen PC (Berdasarkan Jumlah MOU Aktif M vs M-1)",
                      "PPC Active","Entry Claim","Send to Cust","% Work","Assignment Kontrak C0","LKS"};
    for(String name : names) {
      ListKpi lk = new ListKpi();
      lk.setName(name);
      expResult.add(lk);
    }
    //result
    String dept = "opr";
    String members = "10100";
    String coy = "01";
    String lob = "nmc";
    String dtsTable = "agg_bucket_d";
    String dtsTableLastMonth = "agg_bucket_d_m_1";
    Connection conn = getConnection();
    PreparedStatement pstmt = conn.prepareStatement(
            "SELECT name, id, parent, kpi_type, kpi, members, coy, lob, url, dept, " +
              "NVL(target,0) target, NVL(actual,0) actual, NVL(last_month,0) last_month, NVL(achieve,0) achieve, NVL(growth,0) growth, " +
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
                ":1 members, :2 coy, :3 lob, '#' url, :4 dept, " +
                "data.kpi data_kpi, NVL(data.target,0) target, NVL(data.actual,0) actual, " +
                "NVL(data.last_month,0) last_month, NVL(data.batas_atas,0) batas_atas, NVL(data.batas_bawah,0) batas_bawah, " +
                "'kpi_'||:5||'_'||:6||'_'||:7 kpi_type, " +
                "CASE " +
                  "WHEN (kpi.parent_id IS NULL OR kpi.parent_id = -1) THEN 'KPI '|| " +
                    "(SELECT node_name FROM mst_node WHERE fifapps_code = :8 AND ROWNUM = 1)||' '|| " +
                    "(SELECT coy_short_name FROM fs_mst_company_vw WHERE coy_id = :9 AND ROWNUM = 1)||' '|| " +
                    "UPPER(:10) " +
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
                  "WHERE ml.layer_code = :11 " +
                    "AND DECODE(mn.level_id,4,mn.fifapps_code||'p',mn.fifapps_code) = :12 " +
                    "AND abd.coy_id = :13 " +
                    "AND abd.buss_unit = UPPER(:14) " +
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
              "ORDER BY lvl DESC, parent, name");
    pstmt.setString(1, members);
    pstmt.setString(2, coy);
    pstmt.setString(3, lob);
    pstmt.setString(4, dept);
    pstmt.setString(5, members);
    pstmt.setString(6, coy);
    pstmt.setString(7, lob);
    pstmt.setString(8, members);
    pstmt.setString(9, coy);
    pstmt.setString(10, lob);
    pstmt.setString(11, dept);
    pstmt.setString(12, members);
    pstmt.setString(13, coy);
    pstmt.setString(14, lob);
    ResultSet rs = pstmt.executeQuery();
    List<ListKpi> result = new ArrayList<ListKpi>();
    while(rs.next()) {
      ListKpi lk = new ListKpi();
      lk.setName(rs.getString("name"));
      result.add(lk);
    }
    for(int idxRes = 0; idxRes < expResult.size(); idxRes++) {
      assertEquals(expResult.get(idxRes).getName(),result.get(idxRes).getName());
    }
    rs.close();
    pstmt.close();
    conn.close();
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of getListKpi method, of class UsersDAO.
   * @throws java.io.IOException
   * @throws java.lang.ClassNotFoundException
   * @throws java.sql.SQLException
   */
  @Test
  public void testGetListKpi_7args() throws IOException, ClassNotFoundException, SQLException {
    System.out.println("getListKpi7args");
    //expected
    List<ListKpi> expResult = new ArrayList<ListKpi>();
    String[] names = {"JAKARTA - 1","KIOS  PISANGAN","KIOS  PONDOK KELAPA","KIOS  T JAKARTA 1","KIOS HALIM",
                      "KIOS MITRA PULO GADUNG","KIOS PULO MAS","KIOS T KALIMALANG","KIOS T SPEKTRA KALIMALANG",
                      "KIOS UF PULO JAHE","POS KALIMALANG","POS SPEKTRA KALIMALANG","JAKARTA 1"};
    for(String name : names) {
      ListKpi lk = new ListKpi();
      lk.setName(name);
      expResult.add(lk);
    }
    //result
    String dept = "opr";
    String members = "10100";
    String coy = "01";
    String lob = "nmc";
    String kpi = "151";
    String dtsTable = "agg_bucket_d";
    String dtsTableLastMonth = "agg_bucket_d_m_1";
    Connection conn = getConnection();
    PreparedStatement pstmt = conn.prepareStatement(
            "SELECT name, id, parent, kpi_type, kpi, members, coy, lob, url, dept, " +
              "NVL(target,0) target, NVL(actual,0) actual, NVL(last_month,0) last_month, NVL(achieve,0) achieve, NVL(growth,0) growth, " +
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
              "SELECT mst.name, mst.id, mst.parent, :1 kpi, :2 members, :3 coy, :4 lob, :5 dept, " +
                "'kpi_'||:6||'_'||:7||'_'||:8||'_'||:9 kpi_type, " +
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
                    "WHERE ml.layer_code = :10 " +
                      "AND nna.node_child = mn.node_id " +
                      "AND nna.layer_id = ml.layer_id) " +
                  "START WITH child_code = :11 " +
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
                  "WHERE mk.kpi_id = :12 " +
                    "AND abd.coy_id = :13" +
                    "AND abd.buss_unit = UPPER(:14) " +
                    "AND mk.kpi_id = abd.kpi_id " +
                ") data " +
                "WHERE data.id (+) = mst.id " +
                "ORDER BY lvl DESC, mst.parent, mst.name)");
    pstmt.setString(1, kpi);
    pstmt.setString(2, members);
    pstmt.setString(3, coy);
    pstmt.setString(4, lob);
    pstmt.setString(5, dept);
    pstmt.setString(6, members);
    pstmt.setString(7, kpi);
    pstmt.setString(8, coy);
    pstmt.setString(9, lob);
    pstmt.setString(10, dept);
    pstmt.setString(11, members);
    pstmt.setString(12, kpi);
    pstmt.setString(13, coy);
    pstmt.setString(14, lob);
    ResultSet rs = pstmt.executeQuery();
    List<ListKpi> result = new ArrayList<ListKpi>();
    while(rs.next()) {
      ListKpi lk = new ListKpi();
      lk.setName(rs.getString("name"));
      result.add(lk);
    }
    assertEquals(expResult.size(), result.size());
    for(int idxRes = 0; idxRes < expResult.size(); idxRes++) {
      assertEquals(expResult.get(idxRes).getName(),result.get(idxRes).getName());
    }
    rs.close();
    pstmt.close();
    conn.close();
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of getListTableContentByPage method, of class UsersDAO.
   * @throws java.io.IOException
   * @throws java.lang.ClassNotFoundException
   * @throws java.sql.SQLException
   */
  @Test
  public void testGetListTableContentByPage() throws IOException, ClassNotFoundException, SQLException {
    System.out.println("getListTableContentByPage");
    String tableName = "USER_ACCESS";
    Connection conn = getConnection();
    //expected max page no
    int maxPageExpect = 1;
    //result max page no
    PreparedStatement pstmt = conn.prepareStatement(
            "SELECT COUNT(*) cnt FROM " + tableName);
    ResultSet rs = pstmt.executeQuery();
    rs.next();
    int maxPageResult = rs.getInt("cnt");
    maxPageResult = maxPageResult == 0 ? 1 : (maxPageResult/10 + (maxPageResult%10 == 0 ? 0 : 1));
    assertEquals(maxPageExpect, maxPageResult);
    rs.close();
    pstmt.close();
    // expexted max id
    int maxIdExpect = 4;
    //result max id
    pstmt = conn.prepareStatement(
            "SELECT NVL(MAX(ID),0)+1 max_id FROM " + tableName);
    rs = pstmt.executeQuery();
    rs.next();
    int maxIdResult = rs.getInt("max_id");
    assertEquals(maxIdExpect, maxIdResult);
    rs.close();
    pstmt.close();
    //expected order by column
    String orderByColumnExpect = "USER_ACCESS.ID";
    //result order by column
    pstmt = conn.prepareStatement(
            "SELECT utc.column_name " +
              "FROM user_tab_cols utc " +
                  ",user_constraints uc " +
                  ",user_cons_columns ucc " +
              "WHERE utc.table_name = ? " +
                "AND uc.constraint_type = 'P' " +
                "AND utc.table_name = ucc.table_name " +
                "AND utc.column_name = ucc.column_name " +
                "AND ucc.table_name = uc.table_name " +
                "AND ucc.constraint_name = uc.constraint_name");
    pstmt.setString(1, tableName);    
    rs = pstmt.executeQuery();
    rs.next();
    String orderByColumnResult = tableName + "." + rs.getString("column_name");
    assertEquals(orderByColumnExpect, orderByColumnResult);    
    //
    rs.close();
    pstmt.close();
    conn.close();
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of saveTable method, of class UsersDAO.
   * @throws java.io.IOException
   * @throws java.lang.ClassNotFoundException
   * @throws java.sql.SQLException
   */
  @Test
  public void testSaveTable() throws IOException, ClassNotFoundException, SQLException {
    System.out.println("saveTable");
    String sql =
            "UPDATE upload_test " +
              "SET flag = DECODE(flag,'Y','N','Y')";
    Connection conn = getConnection();
    PreparedStatement pstmt = conn.prepareStatement(sql);
    int result = pstmt.executeUpdate();
    pstmt.close();
    conn.close();
    // TODO review the generated test code and remove the default call to fail.
    if(result == 0)
      fail("No record affected");
  }

  /**
   * Test of getListTableValue method, of class UsersDAO.
   * @throws java.io.IOException
   * @throws java.lang.ClassNotFoundException
   * @throws java.sql.SQLException
   */
  @Test
  public void testGetListTableValue() throws IOException, ClassNotFoundException, SQLException {
    System.out.println("getListTableValue");
    //expected
    List<TableValue> expResult = new ArrayList<TableValue>();
    String[] ids = {"1","2","3"};
    String[] descs = {"Admin","Member-Admin","Member-Umum"};
    for(int idxTbl = 0; idxTbl < ids.length; idxTbl++) {
      TableValue tb = new TableValue();
      tb.setId(ids[idxTbl]);
      tb.setCol1(descs[idxTbl]);
      expResult.add(tb);
    }
    //result
    String tableName = "USER_ACCESS";
    String columnsSerialExt = "id, description col1";
    String orderByColumn = "id";
    Connection conn = getConnection();
    PreparedStatement pstmt = conn.prepareStatement(
            "SELECT " + columnsSerialExt +
              " FROM " + tableName +
              " ORDER BY " + orderByColumn);
    ResultSet rs = pstmt.executeQuery();
    List<TableValue> result = new ArrayList<TableValue>();
    while(rs.next()) {
      TableValue tb = new TableValue();
      tb.setId(rs.getString("id"));
      tb.setCol1(rs.getString("col1"));
      result.add(tb);
    }
    assertEquals(expResult.size(), result.size());
    for(int idxRes = 0; idxRes < expResult.size(); idxRes++) {
      assertEquals(expResult.get(idxRes).getId(),result.get(idxRes).getId());
      assertEquals(expResult.get(idxRes).getCol1(),result.get(idxRes).getCol1());
    }
    rs.close();
    pstmt.close();
    conn.close();
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of getPageNo method, of class UsersDAO.
   * @throws java.io.IOException
   * @throws java.lang.ClassNotFoundException
   * @throws java.sql.SQLException
   */
  @Test
  public void testGetPageNo() throws IOException, ClassNotFoundException, SQLException {
    System.out.println("getPageNo");
    //expected
    Integer expResult = 1;
    //result
    String tableName = "MST_LAYER";
    String id = "1";
    String columnsSerialExt = "LAYER_ID id";
    String orderByColumn = "LAYER_ID";
    Connection conn = getConnection();
    PreparedStatement pstmt = conn.prepareStatement(
            "SELECT CEIL(rnum/10) page_no " +
              "FROM ( " +
                "SELECT ROWNUM rnum, id " +
                "FROM ( " +
                "SELECT " + columnsSerialExt + " " +
                  "FROM " + tableName + " " +
                  "ORDER BY " + orderByColumn + ")) " +
              "WHERE id = ?");
    pstmt.setString(1, id);
    ResultSet rs = pstmt.executeQuery();
    rs.next();
    Integer result = rs.getInt("page_no");
    assertEquals(expResult, result);
    rs.close();
    pstmt.close();
    conn.close();
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of getListUploadTableByUser method, of class UsersDAO.
   * @throws java.io.IOException
   * @throws java.lang.ClassNotFoundException
   * @throws java.sql.SQLException
   */
  @Test
  public void testGetListUploadTableByUser() throws IOException, ClassNotFoundException, SQLException {
    System.out.println("getListUploadTableByUser");
    String userAccess = "awal";
    //expected
    List<ListBean> expResult = new ArrayList<ListBean>();
    String[] codes = {"MST_KPI_TEMP","KPI_KPI_ASSC_TEMP","UPLOAD_TEST"};
    String[] names = {"Master Kpi","Kpi Assc","Upload Test"};
    for(int idxStr = 0; idxStr < codes.length; idxStr++) {
      ListBean lb = new ListBean();
      lb.setCode(codes[idxStr]);
      lb.setName(names[idxStr]);
      expResult.add(lb);
    }
    //result
    Connection conn = getConnection();
    PreparedStatement pstmt = conn.prepareStatement(
            "SELECT name, table_name code " +
              "FROM ff_upload_table " +
              "WHERE user_access = :userAccess " +
              "OR :userAccess IN ( " +
                "SELECT user_name " +
                  "FROM users " +
                  "WHERE user_access = 1)");
    pstmt.setString(1, userAccess);
    pstmt.setString(2, userAccess);
    ResultSet rs = pstmt.executeQuery();
    List<ListBean> result = new ArrayList<ListBean>();
    while(rs.next()) {
      ListBean lb = new ListBean();
      lb.setCode(rs.getString("code"));
      lb.setName(rs.getString("name"));
      result.add(lb);
    }
    assertEquals(expResult.size(), result.size());
    for(int idxRes = 0; idxRes < expResult.size(); idxRes++) {
      assertEquals(expResult.get(idxRes).getCode(),result.get(idxRes).getCode());
      assertEquals(expResult.get(idxRes).getName(),result.get(idxRes).getName());
    }
    rs.close();
    pstmt.close();
    conn.close();
    // TODO review the generated test code and remove the default call to fail.
  }
  
  private Connection getConnection() throws IOException, ClassNotFoundException, SQLException {
    Properties prop = new Properties();
    prop.load(UsersDAO.class.getClassLoader().getResourceAsStream("jdbc.properties"));
    Class.forName(prop.getProperty("jdbc.driver"));
    return DriverManager.getConnection(prop.getProperty("jdbc.url"),prop.getProperty("jdbc.username"),prop.getProperty("jdbc.password"));
  }
  
}
