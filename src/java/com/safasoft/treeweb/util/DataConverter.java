/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.safasoft.treeweb.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Convert String, Number and Date object into readable/writable text
 * Include conversion from text into SQL database readable statement
 * @created Oct 1, 2015
 * @author awal
 */
public class DataConverter {
    private String converter = "";

    /**
     * @return the converter
     */
    public String getConverter() {
        return converter;
    }

    /**
     * String into text
     * @param converter the converter to set
     */
    public void setConverter(String converter) {
        this.converter = converter;
    }

    /**
     * String into SQL statement
     * @param converter the converter to set
     * @param charAround
     */
    public void setConverter(String converter, String charAround) {
        this.converter = charAround + converter + charAround;
    }

    /**
     * Integer into text
     * @param converter the converter to set
     */
    public void setConverter(int converter) {
        this.converter = Integer.toString(converter);
    }

    /**
     * Double into text
     * @param converter the converter to set
     * @param format
     */
    public void setConverter(double converter,String format) {
        NumberFormat formatter = new DecimalFormat(format);
        this.converter = formatter.format(converter);
    }

    /**
     * Date into text
     * @param converter the converter to set
     * @param format
     */
    public void setConverter(Date converter, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        this.converter = formatter.format(converter);
    }

    /**
     * Date into SQL statement
     * @param converter the converter to set
     * @param format
     * @param charAround
     */
    public void setConverter(Date converter, String format, String charAround) {
        DateFormat formatter = new SimpleDateFormat(format);
        this.converter = "TO_DATE("+ charAround + formatter.format(converter) + charAround + ","+ charAround + format + charAround + ")";
    }
}
