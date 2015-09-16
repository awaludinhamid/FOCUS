/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.service;

import com.safasoft.treeweb.bean.MstDts;
import java.util.List;

/**
 *
 * @author awal
 */
public interface MstDtsService {

  MstDts getByCode(String dtsCode);
  List<MstDts> getAll();
}
