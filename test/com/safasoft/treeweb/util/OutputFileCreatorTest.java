/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safasoft.treeweb.util;

import com.safasoft.treeweb.bean.support.ColumnHeader;
import com.safasoft.treeweb.bean.support.ListKpiHie;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author awal
 */
public class OutputFileCreatorTest {
  
  public OutputFileCreatorTest() {
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
   * Test of createFile method, of class OutputFileCreator.
   * @throws java.lang.Exception
   */
  @Test
  public void testCreateFile() throws Exception {
    System.out.println("createFile");
    //title
    List<String> titles = new ArrayList<String>();
    titles.add("Test Report");
    titles.add("First Title: My Title");
    //header
    List<ColumnHeader> headers = new ArrayList<ColumnHeader>();
    for(int idxCol = 0; idxCol < 10; idxCol++) {
      ColumnHeader ch = new ColumnHeader();
      ch.setName("col"+idxCol);
      ch.setWidth(5);
      headers.add(ch);
    }
    //content
    List<ListKpiHie> content = new ArrayList<ListKpiHie>();
    for(int idxRow = 0; idxRow < 2; idxRow++) {
      ListKpiHie lkh = new ListKpiHie();
      lkh.setAchieve(0d);
      lkh.setActual(0d);
      lkh.setGrowth(0d);
      lkh.setKpi("0");
      lkh.setId(idxRow);
      lkh.setLastMonth(0d);
      lkh.setName("name"+idxRow);
      lkh.setParent(0);
      lkh.setParentName("parent name0");
      lkh.setTarget(0d);
      content.add(lkh);
    }
    //file ext
    String fileExt = "xslx";
    //kpi breakdown flag
    String kpiBrkdwnFlag = "0";
    //file
    File file = new File("test."+fileExt);
    OutputFileCreator instance = new OutputFileCreator(file, titles, headers, content, fileExt, kpiBrkdwnFlag);
    instance.createFile();
    // TODO review the generated test code and remove the default call to fail.
  }
  
}
