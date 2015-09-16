/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function(){
  $("#loading").show();
  var optFlag = 0;
  var prevId = "";
  var currId = "";
  var currPageNo = 1;
  var currMaxPage = 1;
  var currMaxId = 0;
  var currBtn = 0; //0=button new, 1=button edit, 2=button delete
  var columns, contents, currIdColumn, currIdRow, rowContents, sql;
  generateOption();
  generateData();

  $("#adminSelect").on("click","ul li",function() {
    var thisId = this.id; //current item id
    var parentId = $(this).parent().attr("id"); //current parent (group)
    if(thisId != "select"+parentId) {
      $("#adminSelect ul li#"+thisId+" span").removeClass(btnDefault).addClass(btnPrimary);
      $("#adminSelect ul li#"+thisId+" span").toggleClass(glyTriDown).toggleClass(glyClose);
      if(optFlag == 0) {
        $("#adminSelect ul:not('#"+parentId+"')").slideDown(translation);
        $("#adminSelect ul li:not('#select"+parentId+",#"+thisId+"')").slideDown(translation);
        optFlag = 1;
      } else {
        $("#adminSelect ul:not('#"+parentId+"')").slideUp(translation);
        $("#adminSelect ul li:not('#select"+parentId+",#"+thisId+"')").slideUp(translation);
        if(thisId != prevId) {
          $("#loading").show();
          $("#adminSelect ul li#"+prevId+" span").attr("class",unpickStyle);
          currId = thisId;
          currPageNo = 1;
          setTabelTitle();
          generateData();
        }
        optFlag = 0;
      }
      prevId = thisId;
    }
  });

  $("#idRefresh").on("click", function() {
    $("#loading").show();
    generateData();
  });

  $("div#pagination ul").on("click", "li", function() {
    var thisId = this.id;
    if(!$("#"+thisId).hasClass("active") && !$("#"+thisId).hasClass("disabled") && currPageNo <= currMaxPage) {
      $("#loading").show();
      if(thisId == "firstPage")
        currPageNo = 1;
      else if(thisId == "nextPage")
        currPageNo += 1;
      else if(thisId == "prevPage")
        currPageNo -= 1;
      else if(thisId == "lastPage")
        currPageNo = currMaxPage;
      else
        currPageNo = parseInt(thisId.replace("page",""));
      generateData();
    }
  });

  $("#tableData thead tr").on("click", "th button", function() {
    currBtn = 0;
    modalTableShow();
  });

  $("#tableData tbody").on("click", "tr td button#btn-delete", function() {
    currBtn = 2;
    $("#loading").show();
    currIdRow = $(this).parent().data("id");
    sql = "DELETE FROM "+currId+" WHERE " +
      (currIdColumn == "" ? "ROWID" : currIdColumn) + " = '" + currIdRow + "'";
    $.post("/FOCUS/apps/data/table/save",{sql: sql},function(data,status) {
      generateData();
    });
  });

  $("#tableData tbody").on("click", "tr td button#btn-edit", function() {
    currIdRow = $(this).parent().data("id");
    currBtn = 1;
    modalTableShow();
  });

  $("#mdl-table .modal-footer button#btn-save").click(function(){
    $("#loading").show();
    var delimiter = ",";
    var idx = 0;
    if(currBtn == 1) {
      sql = "UPDATE "+currId+" ";
      $("#mdl-table #tbl-modal td input").each(function() {
        if(column[idx].columnName != currIdColumn) {
          if($(this).data("type") == "NUMBER")
            sql += "SET " + columns[idx].columnName + " = " + $(this).val + delimiter + " ";
          else if($(this).data("type") == "DATE")
            sql += "SET " + columns[idx].columnName + " = " + "TO_DATE('" + $(this).val() + "','DD-MON-YY')" + delimiter + " ";
          else
            sql += "SET " + columns[idx].columnName + " = '" + $(this).val + "'" + delimiter + " ";
        }
        idx++;
      });
      sql = sql.substr(0,sql.lastIndexOf(delimiter)) + " WHERE " +
        (currIdColumn == "" ? "ROWID" : currIdColumn) + " = '" + currIdRow + "'";
    } else {
      sql = "INSERT INTO "+currId+" (";
      for(i = 0; i < columns.length; i++)
        sql += columns[i].columnName + delimiter;
      sql = sql.substr(0,sql.lastIndexOf(delimiter)) + ") VALUES (";
      $("#mdl-table #tbl-modal td input").each(function() {
        if($(this).data("type") == "NUMBER")
          sql += $(this).val() + delimiter;
        else if($(this).data("type") == "DATE")
          sql += "TO_DATE('" + $(this).val() + "','DD-MON-YY')" + delimiter;
        else
          sql += "'" + $(this).val() + "'" + delimiter;
      });
      sql = sql.substr(0,sql.lastIndexOf(delimiter)) + ")";
    }
    $.post("/FOCUS/apps/data/table/save",{sql: sql},function(data,status) {
      generateData();
    });
  });

  function generateOption() {
    var prevParent = "";
    for(i = 0; i < list_admin.length; i++) {
      if(prevParent != list_admin[i].parentCode) {
        $("#adminSelect").append(
          "<ul id='"+list_admin[i].parentCode+"'"+(i == 0 ? "" : " style='display: none'")+">"+
            "<li id='select"+list_admin[i].parentCode+"'><span id='spanselect"+list_admin[i].parentCode+"'>&nbsp;"+list_admin[i].parentName+"</span></li>"+
          "</ul>");
        $("#spanselect"+list_admin[i].parentCode)
            .attr("class",lblStyle).css("background-color",bkLabel).css("text-align","left").css("cursor","default");
        prevParent = list_admin[i].parentCode;
      }
      if(i == 0) {
        currId = list_admin[i].code;
        setTabelTitle();
        createList(list_admin[i].parentCode,list_admin[i].code,list_admin[i].name,true);
      } else {
        createList(list_admin[i].parentCode,list_admin[i].code,list_admin[i].name,false);
      }
    }
  }

  function generateData() {
    $.get("/FOCUS/apps/data/table",{tableName: currId, pageNo: currPageNo},function(data,status) {
      contents = data.contents;
      columns = data.columns;
      currMaxPage = data.maxPage;
      currMaxId = data.maxId;
      currIdColumn = "";
      $("#tableData thead tr th").detach();
      $("#tableData tbody tr").detach();
      for(i = 0; i < columns.length; i++) {
        $("#tableData thead tr").append(
          "<th>"+columns[i].columnName.replace(/_/g," ")+"</th>"
        );
        if(columns[i].constraintType == "P")
          currIdColumn = columns[i].columnName;
      }
      $("#tableData thead tr").append(
        "<th style='padding: 0px; padding-bottom: 3px'>"+
          "<button id='btn-new' class='btn btn-default btn-sm' style='width: 130px'><span class='glyphicon glyphicon-plus'></span>&nbsp;New Record</button>"+
        "</th>"
      );
      for(i = 0; i < contents.length; i++) {
        var groupText = contents[i].groupText.split("|");
        var contentFilled = "";
        for(j = 0; j < groupText.length; j++) {
          contentFilled +="<td>"+groupText[j]+"</td>";
        }
        $("#tableData tbody").append(
          "<tr>"+contentFilled+
            "<td style='padding: 0px; padding-top: 3px' data-id='"+contents[i].id+"'>"+
              "<button id='btn-edit' class='btn btn-success btn-sm'><span class='glyphicon glyphicon-pencil'></span>&nbsp;Edit</button>&nbsp;"+
              "<button id='btn-delete' class='btn btn-danger btn-sm'><span class='glyphicon glyphicon-remove'></span>&nbsp;Delete</button>"+
            "</td>"+
          "</tr>"
        );
      }
      setPagination();
      switchPageStatus();
      $("#loading").hide();
    });
  }

  function setTabelTitle() {
    for(i = 0; i < list_admin.length; i++) {
      if(list_admin[i].code == currId) {
        $("#tableTitle").html("<span class='glyphicon glyphicon-th-list'></span>&nbsp;"+list_admin[i].name+" ["+currId +"]");
        break;
      }
    }
  }

  function switchPageStatus() {
    if(currPageNo == 1)
      $("#firstPage").addClass("disabled");
    else
      $("#firstPage").removeClass("disabled");
    if(currPageNo > 1)
      $("#prevPage").removeClass("disabled");
    else
      $("#prevPage").addClass("disabled");
    if(currPageNo < currMaxPage)
      $("#nextPage").removeClass("disabled");
    else
      $("#nextPage").addClass("disabled");
    if(currPageNo == currMaxPage)
      $("#lastPage").addClass("disabled");
    else
      $("#lastPage").removeClass("disabled");
  }

  function setPagination() {
    $("div#pagination ul li").detach();
    $("div#pagination ul").append(
      "<li id='firstPage'><a href='#' aria-label='First'><span aria-hidden='true'>&laquo;</span></a></li>"+
      "<li id='prevPage'><a href='#' aria-label='Previous'><span aria-hidden='true'>&lt;</span></a></li>"
    );
    var pagePerLoad = 15;
    var startPageNo = Math.floor(currPageNo/pagePerLoad) * pagePerLoad + (currPageNo%pagePerLoad == 0 ? 0 : 1);
    var endPageNo = currMaxPage < (startPageNo + pagePerLoad) ? currMaxPage : (startPageNo + pagePerLoad -1);
    for(i = startPageNo; i <= endPageNo; i++) {
      $("div#pagination ul").append(
        "<li id='page"+i+"'"+(i == currPageNo ? " class='active' " : "")+"><a href='#'>"+i+"</a></li>"
      );
    }
    $("div#pagination ul").append(
      "<li id='nextPage'><a href='#' aria-label='Next'><span aria-hidden='true'>&gt;</span></a></li>"+
      "<li id='lastPage'><a href='#' aria-label='Last'><span aria-hidden='true'>&raquo;</span></a></li>"
    );
  }

  function modalTableShow() {
    var content = "<table id='tbl-modal' class='table table-bordered table-striped'>";
    if(currBtn == 1)
      setRowContents();
    for(i = 0; i < columns.length; i++)
      content += "<tr><td>"+columns[i].columnName.replace(/_/g," ")+
        "</td><td style='width: 300px'><input style='width: 300px' type='text'"+
        " data-type='"+columns[i].dataType+"'"+
        (currBtn == 1 ? (" value='"+rowContents[i]+"'"+(columns[i].constraintType == "P" ? " disabled" : "")) :
        (columns[i].constraintType == "P" && columns[i].dataType == "NUMBER" ? " value='"+currMaxId+"' disabled" : ""))+"></td></tr>";
    content += "</table>";
    $("#mdl-table #title").html("<span class='glyphicon glyphicon-plus'></span>&nbsp;New Record ["+currId+"]");
    $("#mdl-table #content").html(content);
    $("#mdl-table").modal("show");
  }
    function modalCommonShow() {
      var htmlTitle = "<h3>Undefined</h3>";
      var content = "";
      if(currBtn == 0) {
        htmlTitle = "<h3 style='color: red'><span class='glyphicon glyphicon-plus'></span>&nbsp;New Record</h3>";
        content = "<span>Save new record?</span>";
      } else if (currBtn == 1) {
        htmlTitle = "<h3 style='color: red'><span class='glyphicon glyphicon-pencil'></span>&nbsp;Edit</h3>";
        content = "<span>Save changed?</span>";
      } else if (errCode == 2) {
        htmlTitle = "<h3 style='color: orange'><span class='glyphicon glyphicon-remove'></span>&nbsp;Delete</h3>";
        content = "<span>Delete this record?</span>";
      }
      if(!(reqObjTemp == null)) {
        var message = reqObjTemp.responseText;
        var startIdx = message.indexOf("<body>");
        content += message.substring(startIdx).replace("</html>","").replace(/h\d>/g,"p>");
      }
      $("#mdl-common #myModalLabel").html(htmlTitle);
      $("#mdl-common #content").html(content);
      $("#mdl-common").modal("show");
      $("#loading").hide();
    }

  function setRowContents() {
    rowContents = [];
    for(i = 0; i < contents.length; i++) {
      if(contents[i].id == currIdRow) {
        rowContents = contents[i].groupText.split("|");
        break;
      }
    }
  }
});
