/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.service.impl;

import com.safasoft.treeweb.bean.support.ListKpi;
import com.safasoft.treeweb.dao.GenericDAO;
import com.safasoft.treeweb.service.GenericService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @created May 4, 2017
 * @author awal
 */
@Service("genericService")
@Transactional(readOnly=true)
public class GenericServiceImpl implements GenericService {

  @Autowired
  private GenericDAO genericDAO;

  @Override
  public List<ListKpi> getListKpi(String dept, String members, String coy, String lob, String dtsTable, String dtsTableLastMonth) {
    return genericDAO.getListKpi(dept, members, coy, lob, dtsTable, dtsTableLastMonth);
  }

  @Override
  public List<ListKpi> getListKpi(String dept, String members, String coy, String lob, String kpi, String dtsTable, String dtsTableLastMonth) {
    return genericDAO.getListKpi(dept,members,coy,lob,kpi,dtsTable,dtsTableLastMonth);
  }
}
