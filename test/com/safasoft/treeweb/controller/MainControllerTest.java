/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safasoft.treeweb.controller;

import javax.servlet.http.HttpServletRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 *
 * @author awal
 */
public class MainControllerTest {
  
  public MainControllerTest() {
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
   * Test of getAppPage method, of class MainController.
   */
  @Test
  public void testGetAppPage() {
    System.out.println("getAppPage");
    MockHttpServletRequest httpRequest = new MockHttpServletRequest();
    MainController instance = new MainController();
    String expResult = "applicationpage";
    String result = instance.getAppPage(httpRequest);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of getHomePage method, of class MainController.
   */
  @Test
  public void testGetHomePage() {
    System.out.println("getHomePage");
    MockHttpServletRequest httpRequest = new MockHttpServletRequest();
    MainController instance = new MainController();
    String expResult = "homepage";
    String result = instance.getHomePage(httpRequest);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of getAdminPage method, of class MainController.
   */
  @Test
  public void testGetAdminPage() {
    System.out.println("getAdminPage");
    MockHttpServletRequest httpRequest = new MockHttpServletRequest();
    MainController instance = new MainController();
    String expResult = "adminpage";
    String result = instance.getAdminPage(httpRequest);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of getUploadPage method, of class MainController.
   */
  @Test
  public void testGetUploadPage() {
    System.out.println("getUploadPage");
    MockHttpServletRequest httpRequest = new MockHttpServletRequest();
    MainController instance = new MainController();
    String expResult = "uploadpage";
    String result = instance.getUploadPage(httpRequest);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of getErrorPage method, of class MainController.
   */
  @Test
  public void testGetErrorPage() {
    System.out.println("getErrorPage");
    MockHttpServletRequest httpRequest = new MockHttpServletRequest();
    MainController instance = new MainController();
    String expResult = "errorpage";
    String result = instance.getErrorPage(httpRequest);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of getAppRadPage method, of class MainController.
   */
  @Test
  public void testGetAppRadPage() {
    System.out.println("getAppRadPage");
    MockHttpServletRequest httpRequest = new MockHttpServletRequest();
    MainController instance = new MainController();
    String expResult = "applicationradialpage";
    String result = instance.getAppRadPage(httpRequest);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
  }
  
}
