/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.service.impl;

import com.safasoft.treeweb.bean.UserAccess;
import com.safasoft.treeweb.dao.UserAccessDAO;
import com.safasoft.treeweb.service.UserAccessService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User access service model implementation
 * @created Aug 31, 2015
 * @author awal
 */
@Service("userAccessService")
@Transactional(readOnly=true)
public class UserAccessServiceImpl implements UserAccessService {

  @Autowired
  private UserAccessDAO userAccessDAO;

  @Override
  public List<UserAccess> getAll() {
    return userAccessDAO.getAll();
  }
}
