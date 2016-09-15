/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.service;

import com.safasoft.treeweb.bean.FfLogFocus;

/**
 * Log Focus Detail service model
 * @created Apr 15, 2016
 * @author awal
 */
public interface FfLogFocusService {

  FfLogFocus save(FfLogFocus ffLogFocus);
  FfLogFocus getById(int id);
}
