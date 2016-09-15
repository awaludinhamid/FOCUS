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
import java.util.Date;
import javax.servlet.http.HttpSession;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

/**
 * To do action when user logout from system
 * Currently used to record user logout time
 * @created Apr 17, 2016
 * @author awal
 */
@Component("customLogoutListener")
public class CustomLogoutListener implements ApplicationListener<HttpSessionDestroyedEvent> {

  @Override
  public void onApplicationEvent(HttpSessionDestroyedEvent e) {        
    //set logout time
    HttpSession session = e.getSession();
    if(session != null) {
      Object obj = session.getAttribute("logParentId");
      if(obj != null) {
        int logParentId = (Integer) obj;
        FfLogFocusService flfService = new SessionUtil<FfLogFocusService>().getAppContext("ffLogFocusService");
        FfLogFocus logFocus = flfService.getById(logParentId);
        DataConverter dc = new DataConverter();
        dc.setConverter(new Date(), "dd-MMM-yyyy kk:mm:ss");
        logFocus.setLogoutTime(dc.getConverter());
        flfService.save(logFocus);
      }
    }
  }

  
}
