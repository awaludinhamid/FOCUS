/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.controller;

import com.safasoft.treeweb.util.SupportUtil;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @created Dec 9, 2015
 * @author awal
 */
@Controller
@RequestMapping("/keypro")
public class MainControllerKeypro {

 protected static Logger logger = Logger.getLogger("controller");

  /**
   * Handles and retrieve KEYPRO page
   * @param httpRequest
   * @return
   */
  @RequestMapping(value="/application", method=RequestMethod.GET)
  public String getKeyproPage(HttpServletRequest httpRequest) {
    logger.debug("Received request to show keypro page");
    //retrieve current user info
    SupportUtil.setSessionVariable(httpRequest);
    return "keypro/application";
  }

  /**
   * Handles and retrieve job view page
   * @param httpRequest
   * @return 
   */
  @RequestMapping(value="/jobview", method=RequestMethod.GET)
  public String getJobviewPage(HttpServletRequest httpRequest) {
    logger.debug("Received request to show jobview page");
     //retrieve current user info
     SupportUtil.setSessionVariable(httpRequest);
    return "keypro/jobview";
  }
}
