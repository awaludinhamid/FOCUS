/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.service;

import com.safasoft.treeweb.bean.support.ListKpi;
import java.util.List;

/**
 * @created May 4, 2017
 * @author awal
 */
public interface GenericService {

  List<ListKpi> getListKpi(String dept, String members, String coy, String lob, String dtsTable, String dtsTableLastMonth);
  List<ListKpi> getListKpi(String dept, String members, String coy, String lob, String kpi, String dtsTable, String dtsTableLastMonth);
}
