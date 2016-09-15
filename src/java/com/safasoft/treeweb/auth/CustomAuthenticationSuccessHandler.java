/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.auth;

import com.safasoft.treeweb.bean.FfLogFocus;
import com.safasoft.treeweb.service.FfLogFocusService;
import com.safasoft.treeweb.util.DataConverter;
import com.safasoft.treeweb.util.SessionUtil;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * To do action when user have logged successfully
 * Currently used to create user session info and record user login
 * @created Apr 16, 2016
 * @author awal
 */
@Component("customAuthenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
 
  private final Logger authLogger = Logger.getLogger("auth");

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
          throws IOException, ServletException {    
    //login info
    HttpSession session = request.getSession();
    String cnname = (String) session.getAttribute("cnname");
    if(cnname == null || cnname.equals("")) {
      try {
        String principal = auth.getPrincipal().toString();
        int start = principal.indexOf("cn=");
        String tmp = principal.substring(start + 3);
        int end = tmp.indexOf(",");
        cnname = tmp.substring(0,end);
        session.setAttribute("cnname", cnname);
        session.setAttribute("uid", auth.getName());
        session.setAttribute("sessionid", session.getId());
      } catch(Exception ex) {
        authLogger.error(ex);
      }
    }
    //logging login
    FfLogFocus logFocus = new FfLogFocus();
    logFocus.setUserName(auth.getName());
    DataConverter dc = new DataConverter();
    dc.setConverter(new Date(), "dd-MMM-yyyy kk:mm:ss");
    logFocus.setLoginTime(dc.getConverter());
    FfLogFocus logFocusSave = new SessionUtil<FfLogFocusService>().getAppContext("ffLogFocusService").save(logFocus);
    session.setAttribute("logParentId", logFocusSave.getId());
    //redirect
    setDefaultTargetUrl("/apps/main/home");
    super.onAuthenticationSuccess(request, response, auth);
  }
}
