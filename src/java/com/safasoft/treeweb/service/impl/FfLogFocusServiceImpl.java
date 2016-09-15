/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.service.impl;

import com.safasoft.treeweb.bean.FfLogFocus;
import com.safasoft.treeweb.dao.FfLogFocusDAO;
import com.safasoft.treeweb.service.FfLogFocusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Log Focus service model
 * @created Apr 15, 2016
 * @author awal
 */
@Service("ffLogFocusService")
@Transactional(readOnly=true)
public class FfLogFocusServiceImpl implements FfLogFocusService {

  @Autowired
  private FfLogFocusDAO ffLogFocusDAO;

  @Override
  @Transactional(readOnly=false)
  public FfLogFocus save(FfLogFocus ffLogFocus) {
    return ffLogFocusDAO.save(ffLogFocus);
  }

  @Override
  public FfLogFocus getById(int id) {
    return ffLogFocusDAO.getById(id);
  }
}
