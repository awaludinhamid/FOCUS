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
  
  /**
   * PDF file creator
   * @param file
   * @param titles
   * @param headers
   * @param content
   * @throws IOException
   * @throws COSVisitorException 
   */
  public void createPDF(File file, List<String> titles, List<ColumnHeader> headers, List<ListKpiHie> content)
          throws IOException, COSVisitorException {
    //object preparation
    PDDocument doc = new PDDocument();
    final int margin = 10;//space between page contain and page border
    final int rowHeight = 10;//line height
    final int cellMargin = 2;//space between text and line
    final int cols = headers.size();
    final int maxRowsPerPage = 78;//move to next page when reach this limit
    final int titleRows = titles.size() + 1;//plus space
    final int startLineY = (maxRowsPerPage - titleRows) * rowHeight;//top position where object start to draw
    final int recordsNum = content.size();
    int num = 0;
    //set table width
    int tableWidth = margin;
    for(ColumnHeader hdr : headers)
      tableWidth += hdr.getWidth() + cellMargin;
    //loop through page
    while(recordsNum > num) {
      //set table height
      int tableRows = recordsNum - num + 1 < maxRowsPerPage - titleRows ? recordsNum - num + 1 : maxRowsPerPage - titleRows;//plus header
      //page preparation
      PDPage page = new PDPage();
      doc.addPage(page);
      PDPageContentStream contentStream = new PDPageContentStream(doc, page);
      contentStream.setLineWidth(1);
      //draw table horizontal line
      int lineCordY = margin + startLineY;
      for (int i = 0; i <= tableRows; i++) {
        contentStream.drawLine(margin,lineCordY,tableWidth,lineCordY);
        lineCordY -= rowHeight;
      }
      //draw table vertical line
      int lineCordX = margin;
      lineCordY = margin + startLineY;
      int endLineY = margin + startLineY - (tableRows * rowHeight);
      for (int i = 0; i <= cols; i++) {
        contentStream.drawLine(lineCordX,endLineY,lineCordX,lineCordY);
        if(i < cols)
          lineCordX += headers.get(i).getWidth() + cellMargin;
      }

      //now add the text
      contentStream.setFont(PDType1Font.HELVETICA_BOLD,6);
      //add title
      int textx = margin;
      int texty = margin + cellMargin + (maxRowsPerPage - 1) * rowHeight;    
      for(String title : titles) {
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(title);
        contentStream.endText();
        texty -= rowHeight;
      }
      //add header
      textx = margin + cellMargin;
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
      textx = margin + cellMargin;
      texty -= rowHeight;
      int idxHdr = 0;
      int maxRecord = num + tableRows - 1;
      for (int idxLkh = num; idxLkh < maxRecord; idxLkh++) {
        ListKpiHie lkh = content.get(idxLkh);
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(++num + "");
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
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
        contentStream.drawString(lkh.getKpi());
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(lkh.getName().length() > 50 ? lkh.getName().substring(0,50) : lkh.getName());
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(lkh.getTarget() + "");
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(lkh.getActual() + "");
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(lkh.getLastMonth() + "");
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(lkh.getAchieve() + "");
        contentStream.endText();
        textx += headers.get(idxHdr++).getWidth() + cellMargin;
        contentStream.beginText();
        contentStream.moveTextPositionByAmount(textx,texty);
        contentStream.drawString(lkh.getGrowth() + "");
        contentStream.endText();
        texty -= rowHeight;
        textx = margin + cellMargin;
        idxHdr = 0;
      }
      contentStream.close();
    }
    doc.save(file);
  } 
  
  /**
   * Excel file creator
   * @param file
   * @param titles
   * @param headers
   * @param content
   * @throws IOException 
   */
  public void createExcel(File file, List<String> titles, List<ColumnHeader> headers, List<ListKpiHie> content)
          throws IOException {
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
    int num = 1;
    for (ListKpiHie lkh : content) {
      cellIdx = 0;
      row = sheet.createRow(rowIdx++);
      cell = row.createCell(cellIdx++);
      cell.setCellValue(num++);
      cell = row.createCell(cellIdx++);
      cell.setCellValue(lkh.getParent());
      cell = row.createCell(cellIdx++);
      cell.setCellValue(lkh.getParentName());
      cell = row.createCell(cellIdx++);
      cell.setCellValue(lkh.getKpi());
      cell = row.createCell(cellIdx++);
      cell.setCellValue(lkh.getName());
      cell = row.createCell(cellIdx++);
      cell.setCellValue(lkh.getTarget());
      cell = row.createCell(cellIdx++);
      cell.setCellValue(lkh.getActual());
      cell = row.createCell(cellIdx++);
      cell.setCellValue(lkh.getLastMonth());
      cell = row.createCell(cellIdx++);
      cell.setCellValue(lkh.getAchieve());
      cell = row.createCell(cellIdx++);
      cell.setCellValue(lkh.getGrowth());
    }
    for(int colIdx = 1; colIdx < cellIdx; colIdx++)
      sheet.autoSizeColumn(colIdx);
    
    OutputStream outFile = new FileOutputStream(file);
    workbook.write(outFile);
    outFile.close();
  }
  
  /**
   * Text file creator
   * @param file
   * @param titles
   * @param headers
   * @param content
   * @throws IOException 
   */
  public void createText(File file, List<String> titles, List<ColumnHeader> headers, List<ListKpiHie> content)
          throws IOException {
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
      fileWriter.append(getStringWithSpace(header.getName(),header.getWidth()/3)).append(delimiter);
    }
    fileWriter.append(newline);
    fileWriter.append(newline);
    // add detail
    int num = 0;
    for (ListKpiHie lkh : content) {
      fileWriter.append(delimiter);
      num++;
      int idxHdr = 0;
      //add space if text length lower than column width
      fileWriter.append(getStringWithSpace(num+"",headers.get(idxHdr++).getWidth()/3)).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getParent()+"",headers.get(idxHdr++).getWidth()/3)).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getParentName(),headers.get(idxHdr++).getWidth()/3)).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getKpi(),headers.get(idxHdr++).getWidth()/3)).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getName(),headers.get(idxHdr++).getWidth()/3)).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getTarget()+"",headers.get(idxHdr++).getWidth()/3)).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getActual()+"",headers.get(idxHdr++).getWidth()/3)).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getLastMonth()+"",headers.get(idxHdr++).getWidth()/3)).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getAchieve()+"",headers.get(idxHdr++).getWidth()/3)).append(delimiter);
      fileWriter.append(getStringWithSpace(lkh.getGrowth()+"",headers.get(idxHdr++).getWidth()/3)).append(delimiter);
      fileWriter.append(newline);
    }
    fileWriter.close();
  }
  
  //text with space to equal column width
  private String getStringWithSpace(String str, int length) {
    if(str == null)
      return null;
    //cut text if it is too long (more than 40)
    if(str.length() > 40)
      str = str.substring(0,40);
    if(str.length() > length)
      str = str.substring(0,length);
    int spaceLen = length - str.length();
    String space = " ";
    StringBuilder sb = new StringBuilder(str);
    for(int idxSpace = 0; idxSpace < spaceLen; idxSpace++)
      sb.append(space);
    return sb.toString();
  }
}