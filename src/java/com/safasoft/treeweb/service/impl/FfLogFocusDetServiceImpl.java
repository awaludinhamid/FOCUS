/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.service.impl;

import com.safasoft.treeweb.bean.FfLogFocusDet;
import com.safasoft.treeweb.dao.FfLogFocusDetDAO;
import com.safasoft.treeweb.service.FfLogFocusDetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Log Focus Detail service model implementation
 * @created Apr 15, 2016
 * @author awal
 */
@Service("ffLogFocusDetService")
@Transactional(readOnly=true)
public class FfLogFocusDetServiceImpl implements FfLogFocusDetService {

  @Autowired
  private FfLogFocusDetDAO ffLogFocusDetDAO;

  @Override
@Transactional(readOnly=false)
  public FfLogFocusDet save(FfLogFocusDet ffLogFocusDet) {
    return ffLogFocusDetDAO.save(ffLogFocusDet);
  }
}
