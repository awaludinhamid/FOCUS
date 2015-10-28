/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.service.impl;

import com.safasoft.treeweb.bean.MstDts;
import com.safasoft.treeweb.dao.MstDtsDAO;
import com.safasoft.treeweb.service.MstDtsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Master data time series service model implementation
 * @created Aug 26, 2015
 * @author awal
 */
@Service("mstDtsService")
@Transactional(readOnly=true)
public class MstDtsServiceImpl implements MstDtsService {

  @Autowired
  private MstDtsDAO mstDtsDAO;

  @Override
  public MstDts getByCode(String dtsCode) {
    return mstDtsDAO.getByCode(dtsCode);
  }

  @Override
  public List<MstDts> getAll() {
    return mstDtsDAO.getAll();
  }

}
