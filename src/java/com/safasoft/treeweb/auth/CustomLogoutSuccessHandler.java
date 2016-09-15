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
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * @created Apr 16, 2016
 * @author awal
 */
@Component("customLogoutSuccessHandler")
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
          throws IOException, ServletException {
    if(auth != null) {  
      //next implementation
    }
    //redirect
    setDefaultTargetUrl("/apps/auth/login");
    super.onLogoutSuccess(request, response, auth);
  }
}
