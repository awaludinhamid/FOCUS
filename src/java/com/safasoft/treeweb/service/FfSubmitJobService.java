/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.service;

import com.safasoft.treeweb.bean.FfSubmitJob;
import java.util.List;

/**
 * Submit job model service
 * @created Dec 13, 2015
 * @author awal
 */
public interface FfSubmitJobService {

  List<FfSubmitJob> getByUser(String userName, int pageNo);
  int countByUser(String userName);
  String getJobByUserAndFile(String userName, String fileName);
}
