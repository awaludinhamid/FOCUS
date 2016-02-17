/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.service.impl;

import com.safasoft.treeweb.bean.FfSubmitJob;
import com.safasoft.treeweb.dao.FfSubmitJobDAO;
import com.safasoft.treeweb.service.FfSubmitJobService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Submit job service model implementation
 * @created Dec 13, 2015
 * @author awal
 */
@Service("ffSubmitJobService")
@Transactional(readOnly=true)
public class FfSubmitJobServiceImpl implements FfSubmitJobService {

  @Autowired
  private FfSubmitJobDAO ffSubmitJobDAO;

  @Override
  public List<FfSubmitJob> getByUser(String userName, int pageNo) {
    return ffSubmitJobDAO.getByUser(userName, pageNo);
  }

  @Override
  public int countByUser(String userName) {
    return ffSubmitJobDAO.countByUser(userName);
  }

  @Override
  public String getJobByUserAndFile(String userName, String fileName) {
    return ffSubmitJobDAO.getJobByUserAndFile(userName, fileName);
  }
}
