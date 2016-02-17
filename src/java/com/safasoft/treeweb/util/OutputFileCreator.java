package com.safasoft.treeweb.util;

import com.safasoft.treeweb.bean.support.ColumnHeader;
import com.safasoft.treeweb.bean.support.ListKpiHie;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * File creator
 * Contain excel, PDF and text extention
 * @created Oct 20, 2015
 * @author awal
 */
public class OutputFileCreator {
  
  private final File file;
  private final List<String> titles;
  private final List<ColumnHeader> headers;
  private final List<ListKpiHie> content;
  private final String fileExt;
  private final String kpiBrkdwnFlag;
  
  /**
   * Container with parameter only accepted
   * @param file
   * @param titles
   * @param headers
   * @param content
   * @param fileExt
   * @param kpiBrkdwnFlag 
   */
  public OutputFileCreator(File file, List<String> titles, List<ColumnHeader> headers, List<ListKpiHie> content, String fileExt, String kpiBrkdwnFlag) {
    this.file = file;
    this.titles = titles;
    this.headers = headers;
    this.content = content;
    this.fileExt = fileExt;
    this.kpiBrkdwnFlag = kpiBrkdwnFlag;
  }
  
  /**
   * Created file by extention given: excel, PDF, or text
   * @throws IOException
   * @throws COSVisitorException 
   */
  public void createFile() throws IOException, COSVisitorException {
    if(fileExt.equals("xlsx")) {
      createExcel();
    } else if(fileExt.equals("pdf")) {
      createPDF();
    } else if(fileExt.equals("txt")) {
      createText();
    }    
  }
  
  //PDF file creator
  private void createPDF() throws IOException, COSVisitorException {
    //object preparation
    PDDocument doc = new PDDocument();
    //landscape
    final int pageWidth = 842; //page width
    final int pageHeight = 595; //page height
    //
    final int downMargin = 10;//space between page contain and page bottom border
    final int leftMargin = 50;//space between page contain and page left border
    final int rowHeight = 10;//line height
    final int cellMargin = 2;//space between text and line
    final int cols = headers.size();
    final int maxRowsPerPage = (pageHeight/rowHeight) - (2*downMargin/rowHeight);//move to next page when reach this limit
    final int titleRows = titles.size() + 1;//plus space
    final int startLineY = (maxRowsPerPage - titleRows) * rowHeight;//top position where object start to draw
    final int recordsNum = content.size();
    int num = 0;
    //set table width
    int tableWidth = leftMargin;
    for(ColumnHeader hdr : headers)
      tableWidth += hdr.getWidth() + cellMargin;
    //loop through page
    while(recordsNum > num) {
      //set table height
      int tableRows = recordsNum - num + 1 < maxRowsPerPage - titleRows ? recordsNum - num + 1 : maxRowsPerPage - titleRows;//plus header
      //page preparation
      PDPage page = new PDPage();
      PDRectangle pdr = new PDRectangle(pageWidth, pageHeight);
      page.setMediaBox(pdr);
      doc.addPage(page);
      PDPageContentStream contentStream = new PDPageContentStream(doc, page);
      contentStream.setLineWidth(1);
      //draw table horizontal line
      int lineCordY = downMargin + startLineY;
      for (int i = 0; i <= tableRows; i++) {
        contentStream.drawLine(leftMargin,lineCordY,tableWidth,lineCordY);
        lineCordY -= rowHeight;
      }
      //draw table vertical line
      int lineCordX = leftMargin;
      lineCordY = downMargin + startLineY;
      int endLineY = downMargin + startLineY - (tableRows * rowHeight);
      for (int i = 0; i <= cols; i++) {
        contentStream.drawLine(lineCordX,endLineY,lineCordX,lineCordY);
        if(i < cols)
          lineCordX += headers.get(i).getWidth() + cellMargin;
      }

      //now add the text
      contentStream.setFont(PDType1Font.HELVETICA_BOLD,6);
      //add title
      int textx = leftMargin;
      int texty = downMargin + cellMargin + (maxRowsPerPage - 1) * rowHeight;    
      for(String title : titles) {
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(title);
        contentStream.endText();
        texty -= rowHeight;
      }
      //add header
      textx = leftMargin + cellMargin;
      texty -= rowHeight;
      for(ColumnHeader hdr : headers) {
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(hdr.getName());
        contentStream.endText();
        textx += hdr.getWidth() + cellMargin;
      }
      //add detail
      contentStream.setFont(PDType1Font.HELVETICA,5);
      textx = leftMargin + cellMargin;
      texty -= rowHeight;
      int idxHdr = 0;
      int maxRecord = num + tableRows - 1;
      for (int idxLkh = num; idxLkh < maxRecord; idxLkh++) {
        ListKpiHie lkh = content.get(idxLkh);
        String suffix = lkh.getSatuan() == null || !lkh.getSatuan().equals("P") ? "" : "%"; //satuan, mis. %
        String prefix = lkh.getSatuan() == null || !lkh.getSatuan().equals("A") ? "" : "Rp "; //simbol mata uang
        String suffix1 = lkh.getAchieve() == null ? suffix : "%"; //satuan, mis. % di batas atas/bawah
        String prefix1 = lkh.getAchieve() == null ? prefix : ""; //simbol mata uang di batas atas/bawah
        num++;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(lkh.getParent() + "");
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        //cut text if it is too long (more than 50)
        contentStream.drawString(lkh.getParentName().length() > 50 ? lkh.getParentName().substring(0,50) : lkh.getParentName());
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(kpiBrkdwnFlag.equals("1") ? lkh.getId()+"" : lkh.getKpi());
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(lkh.getName().length() > 50 ? lkh.getName().substring(0,50) : lkh.getName());
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(lkh.getBatasAtas() == null ? "" : prefix1 + lkh.getBatasAtas() + suffix1);
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(lkh.getBatasBawah() == null ? "" : prefix1 + lkh.getBatasBawah() + suffix1);
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(suffix.equals("") ? prefix : suffix);
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(lkh.getTarget() == null ? "" : lkh.getTarget() + "");
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(lkh.getSatuan() != null && lkh.getSatuan().equals("B") ? (lkh.getActual() == 1 ? "Sudah" : "Belum") :
                (lkh.getActual() == null ? "" : lkh.getActual() + ""));
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(lkh.getLastMonth() == null ? "" : lkh.getLastMonth() + "");
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(lkh.getAchieve() == null ? "" : lkh.getAchieve() + "");
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(lkh.getGrowth() == null ? "" : lkh.getGrowth() + "");
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(lkh.getDatePopulate() == null ? "" : lkh.getDatePopulate());
        contentStream.endText();
        texty -= rowHeight;
        textx = leftMargin + cellMargin;
        idxHdr = 0;
      }
      contentStream.close();
    }
    doc.save(file);
  } 
  
  //Excel file creator
  private void createExcel() throws IOException {
    //object preparation
    int rowIdx = 0;
    int cellIdx = 0;
    Workbook workbook = new XSSFWorkbook();//must be .xlsx extention
    Sheet sheet = workbook.createSheet("sheet1");
    Row row;
    Cell cell;
    CellStyle cellStyle = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
    cellStyle.setFont(font); 
    // add title
    for (String title : titles) {
      row = sheet.createRow(rowIdx++);
      cell = row.createCell(0);
      cell.setCellValue(title);
      cell.setCellStyle(cellStyle);
    }
    // add header
    rowIdx++;
    row = sheet.createRow(rowIdx++);
    for(ColumnHeader header : headers) {
      cell = row.createCell(cellIdx++);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(header.getName());
    }
    // add detail
    for (ListKpiHie lkh : content) {
      cellIdx = 0;
      String suffix = lkh.getSatuan() == null || !lkh.getSatuan().equals("P") ? "" : "%"; //satuan, mis. %
      String prefix = lkh.getSatuan() == null || !lkh.getSatuan().equals("A") ? "" : "Rp "; //simbol mata uang
      String suffix1 = lkh.getAchieve() == null ? suffix : "%"; //satuan, mis. % di batas atas/bawah
      String prefix1 = lkh.getAchieve() == null ? prefix : ""; //simbol mata uang di batas atas/bawah
      row = sheet.createRow(rowIdx++);
      cell = row.createCell(cellIdx++);
      setCellValue(cell,lkh.getParent());
      cell = row.createCell(cellIdx++);
      setCellValue(cell,lkh.getParentName());
      cell = row.createCell(cellIdx++);
      setCellValue(cell,kpiBrkdwnFlag.equals("1") ? lkh.getId()+"" : lkh.getKpi());
      cell = row.createCell(cellIdx++);
      setCellValue(cell,lkh.getName());
      cell = row.createCell(cellIdx++);
      setCellValue(cell,lkh.getBatasAtas() == null ? "" : prefix1+lkh.getBatasAtas()+suffix1);
      cell = row.createCell(cellIdx++);
      setCellValue(cell,lkh.getBatasBawah() == null ? "" : prefix1+lkh.getBatasBawah()+suffix1);
      cell = row.createCell(cellIdx++);
      setCellValue(cell,suffix.equals("") ? prefix : suffix);
      cell = row.createCell(cellIdx++);
      setCellValue(cell,lkh.getTarget());
      cell = row.createCell(cellIdx++);
      if(lkh.getSatuan() != null && lkh.getSatuan().equals("B"))
        setCellValue(cell,lkh.getActual() == 1 ? "Sudah" : "Belum");
      else
        setCellValue(cell,lkh.getActual());
      cell = row.createCell(cellIdx++);
      setCellValue(cell,lkh.getLastMonth());
      cell = row.createCell(cellIdx++);
      setCellValue(cell,lkh.getAchieve());
      cell = row.createCell(cellIdx++);
      setCellValue(cell,lkh.getGrowth());
      cell = row.createCell(cellIdx++);
      setCellValue(cell,lkh.getDatePopulate());
    }
    for(int colIdx = 1; colIdx < cellIdx; colIdx++)
      sheet.autoSizeColumn(colIdx);
    
    OutputStream outFile = new FileOutputStream(file);
    workbook.write(outFile);
    outFile.close();
  }
  
  //Text file creator
  private void createText() throws IOException {
    //object preparation
    FileWriter fileWriter = new FileWriter(file);
    String newline = "\r\n";//return key and newline
    String delimiter = "|";
    // add title
    for (String title : titles) {
      fileWriter.append(title).append(newline);
    }
    fileWriter.append(newline);
    // add header
    fileWriter.append(delimiter);
    for(ColumnHeader header : headers) {
      fileWriter.append(getStringWithSpace(header.getName(),header.getWidth()/3,"","")).append(delimiter);
    }
    fileWriter.append(newline);
    fileWriter.append(newline);
    // add detail
    for (ListKpiHie lkh : content) {
      fileWriter.append(delimiter);
      int idxHdr = 0;
      String suffix = lkh.getSatuan() == null || !lkh.getSatuan().equals("P") ? "" : "%"; //satuan, mis. %
      String prefix = lkh.getSatuan() == null || !lkh.getSatuan().equals("A") ? "" : "Rp "; //simbol mata uang
      String suffix1 = lkh.getAchieve() == null ? suffix : "%"; //satuan, mis. % di batas atas/bawah
      String prefix1 = lkh.getAchieve() == null ? prefix : ""; //simbol mata uang di batas atas/bawah
      //add space if text length lower than column width
      fileWriter.append(getStringWithSpace(lkh.getParent()+"",headers.get(idxHdr++).getWidth()/3,"","")).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getParentName(),headers.get(idxHdr++).getWidth()/3,"","")).append(delimiter);
      fileWriter.append(getStringWithSpace(kpiBrkdwnFlag.equals("1") ? lkh.getId()+"" : lkh.getKpi(),
              headers.get(idxHdr++).getWidth()/3,"","")).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getName(),headers.get(idxHdr++).getWidth()/3,"","")).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getBatasAtas()+"",headers.get(idxHdr++).getWidth()/3,suffix1,prefix1)).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getBatasBawah()+"",headers.get(idxHdr++).getWidth()/3,suffix1,prefix1)).append(delimiter);
      fileWriter.append(getStringWithSpace(suffix.equals("") ? prefix : suffix,headers.get(idxHdr++).getWidth()/3,"","")).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getTarget()+"",headers.get(idxHdr++).getWidth()/3,"","")).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getSatuan() != null && lkh.getSatuan().equals("B") ? (lkh.getActual() == 1 ? "Sudah" : "Belum") :
              lkh.getActual()+"",headers.get(idxHdr++).getWidth()/3,"","")).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getLastMonth()+"",headers.get(idxHdr++).getWidth()/3,"","")).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getAchieve()+"",headers.get(idxHdr++).getWidth()/3,"","")).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getGrowth()+"",headers.get(idxHdr++).getWidth()/3,"","")).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getDatePopulate(),headers.get(idxHdr++).getWidth()/3,"","")).append(delimiter);
      fileWriter.append(newline);
    }
    fileWriter.close();
  }
  
  //text with space to equal column width
  private String getStringWithSpace(String str, int length, String suffix, String prefix) {
    if(str == null || str.equals("null")) {
      str = "";
      suffix = "";
      prefix = "";
    }
    str = prefix + str + suffix;
    //cut text if it is too long (more than 40)
    if(str.length() > 40)
      str = str.substring(0,40);
    if(str.length() > length)
      str = str.substring(0,length);
    int spaceLen = length - str.length();
    StringBuilder sb = new StringBuilder(str);
    String space = " ";
    for(int idxSpace = 0; idxSpace < spaceLen; idxSpace++)
      sb.append(space);
    return sb.toString();
  }
  
  //set excel cell value include null value and unit
  private void setCellValue(Cell cell, String value) {
    if(value != null)
      cell.setCellValue(value);
  }
  private void setCellValue(Cell cell, Double value) {
    if(value != null)
      cell.setCellValue(value);
  }
  private void setCellValue(Cell cell, Integer value) {
    if(value != null)
      cell.setCellValue(value);
  }
}