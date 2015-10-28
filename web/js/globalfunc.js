/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

  //create list on pComp with new member pCode and pName
  //if pFirstStat is true then show item, otherwise hide it
  function createList(pComp,pCode,pName,pFirstStat) {
    if(pFirstStat) {
      $("#"+pComp).append("<li id='"+pCode+"'>"+
        "<span id='span"+pCode+"' style='text-align: left'>&nbsp;&nbsp;"+
        breakLongText(pName)+"</span></li>");
      $("#"+pComp+" #span"+pCode).attr("class",pickStyle);//.css("background-color",bkColPick);
    } else {
      $("#"+pComp).append("<li id='"+pCode+"' style='display: none'>"+
        "<span id='span"+pCode+"' style='text-align: left'>&nbsp;&nbsp;"+
        breakLongText(pName)+"</span></li>");
      $("#"+pComp+" #span"+pCode).attr("class",unpickStyle);//.css("background-color",bkColUnpick);
    }
  }

  //toggle display and icon's list on pComp, except pFirstId only toggle its icon
  function toggleList(pComp,pFirstId) {
    $("#"+pComp+" li#"+pFirstId+" span").removeClass(btnDefault).addClass(btnPrimary);//.css("background-color",bkColPick);
    $("#"+pComp+" li#"+pFirstId+" span").toggleClass(glyTriDown).toggleClass(glyClose);
    $("#"+pComp+" li").each(function() {
      var currId = this.id;
      //if(currId != "select"+pComp && currId != pFirstId) {
      if(currId != pFirstId) {
        $("#"+pComp+" li#"+currId).slideToggle(translation);
        if(currId != "select"+pComp)
          $("#"+pComp+" li#"+currId+" span").attr("class",unpickStyle);//.css("background-color",bkColUnpick);
      }
    });
  }

  //hide list of pComp except pAllId
  function toggleChildList(pComp,pAllId) {
    $("#"+pComp).slideDown(translation);
    $("#"+pComp+" li").each(function() {
      var currId = this.id;
      if(currId != "select"+pComp) {
        if(currId == pAllId) {
                $("#"+pComp+" li#"+currId).slideDown(translation);
                $("#"+pComp+" li#"+currId+" span").attr("class",pickStyle);//.css("background-color",bkColPick);
        } else {
                $("#"+pComp+" li#"+currId).slideUp(translation);
                $("#"+pComp+" li#"+currId+" span").attr("class",unpickStyle);//.css("background-color",bkColUnpick);
        }
      }
    });
  }

  //change pText to camel word
  function initCap(pText) {
    return pText.toLowerCase().replace(/(?:^|\s)[a-z]/g, function (initChar) {
      return initChar.toUpperCase();
     });
  }

  //generate number with comma thousand separator
  function numberWithCommas(x) {
    var parts = x.toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(".");
  }

  //show modal info with message 'content' and object reqObjTemp
  //errCode: 1=Error, 2=Failed, 3=Warning, 4=Info
  //generate error message on vary situation, so keep updating
  function modalCommonShow(errCode,content,reqObjTemp) {
    var htmlTitle = "<h3>Undefined</h3>";
    if(errCode == 1)
      htmlTitle = "<h3><span style='color: red' class='glyphicon glyphicon-minus-sign'></span>&nbsp;Error</h3>";
    else if (errCode == 2)
      htmlTitle = "<h3><span style='color: red' class='glyphicon glyphicon-ban-circle'></span>&nbsp;Failed</h3>";
    else if (errCode == 3)
      htmlTitle = "<h3><span style='color: orange' class='glyphicon glyphicon-exclamation-sign'></span>&nbsp;Warning</h3>";
    else if (errCode == 4)
      htmlTitle = "<h3><span style='color: blue' class='glyphicon glyphicon-info-sign'></span>&nbsp;Info</h3>";
    $("#mdl-common #myModalLabel").html(htmlTitle);
    if(!(reqObjTemp == null)) {
      var message = reqObjTemp.responseText;
      var startIdx = message.indexOf("<body>");
      localStorage["exception"] = message.substring(startIdx).replace("</html>","");
      if(message.indexOf("ORA-00001") > -1) {
        content += "Duplicate key/PK found";
      } else if(message.indexOf("ORA-02291") > -1) {
        content += "Parent key not found";
      } else if(message.indexOf("ORA-02292") > -1 || message.indexOf("ORA-02266") > -1) {
        content += "Child/referential record found";
      } else if(message.indexOf("NullPointerException") > -1) {
        content += "Process return null value";
      } else if(message.indexOf("ORA-12899") > -1 || message.indexOf("ORA-00942") > -1) {
        var msgTooLong = message.substring(message.indexOf("ORA-")+11);
        content += msgTooLong.substring(0,msgTooLong.indexOf("\n"));
      } else if(message.indexOf("ORA-00947") > -1) {
        content += "The number of upload file column less than table column";
      } else if(message.indexOf("ORA-00913") > -1) {
        content += "The number of upload file column more than table column";
      } else if(message.indexOf("ORA-01722") > -1 || message.indexOf("ORA-00932") > -1 || message.indexOf("ORA-01858")) {
        content += "Datatype of file and table column mismatch";        
      } else if(message.indexOf("ArrayIndexOutOfBoundsException") > -1) {
        content += "Failed access array data";
      } else if(message.indexOf("InvalidFormatException") > -1 || message.indexOf("InvocationTargetException") > -1) {
        content += "Unsupported file type";
      } else {
        content += "[undefined error, click show log to find out]";
      }
    }
    $("#mdl-common #content").html(content + " " +
            "<div><a href='../../jsp/support/loggingpage.jsp' target='_blank' class='btn btn-link'>show log</a></div>");
    $("#mdl-common").modal("show");
    $("#loading").hide();
  }
  
  //breaking text into separate line when reach the limit to avoid very long label
  function breakLongText(txt) {
    var textLimit = 25;
    var breakDelim = "<br/>";
    if(txt.length > textLimit && txt.indexOf(" ") > -1) {
      var rtnStr = "";
      var textTmp = txt.trim();
      while(textTmp.length > textLimit && textTmp.indexOf(" ") > -1) {
        var lastIdx = textTmp.substring(0,textLimit).lastIndexOf(" ");
        rtnStr += textTmp.substring(0,lastIdx)+breakDelim;
        textTmp = textTmp.substring(lastIdx).trim();
      }
      if(textTmp.replace(/\s+/,"") === "")
        return rtnStr.substring(0,rtnStr.lastIndexOf(breakDelim));
      else
        return rtnStr + textTmp;
    } else {
      return txt;
    }
  }