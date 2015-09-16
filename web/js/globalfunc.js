/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

  //create list on pComp with new member pCode and pName
  //if pFirstStat is true then show item, otherwise hide it
  function createList(pComp,pCode,pName,pFirstStat) {
    if(pFirstStat) {
      $("#"+pComp).append("<li id='"+pCode+"'>"+
        "<span id='span"+pCode+"' style='text-align: left'>&nbsp;"+
        pName+"</span></li>");
      $("#"+pComp+" #span"+pCode).attr("class",pickStyle);//.css("background-color",bkColPick);
    } else {
      $("#"+pComp).append("<li id='"+pCode+"' style='display: none'>"+
        "<span id='span"+pCode+"' style='text-align: left'>&nbsp;"+
        pName+"</span></li>");
      $("#"+pComp+" #span"+pCode).attr("class",unpickStyle);//.css("background-color",bkColUnpick);
    }
  }

  //toggle display and icon's list on pComp, except pFirstId only toggle its icon
  function toggleList(pComp,pFirstId) {
    $("#"+pComp+" li#"+pFirstId+" span").removeClass(btnDefault).addClass(btnPrimary);//.css("background-color",bkColPick);
    $("#"+pComp+" li#"+pFirstId+" span").toggleClass(glyTriDown).toggleClass(glyClose);
    $("#"+pComp+" li").each(function() {
      var currId = this.id;
      if(currId != "select"+pComp && currId != pFirstId) {
        $("#"+pComp+" li#"+currId).slideToggle(translation);
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

  function numberWithCommas(x) {
    var parts = x.toString().split(".");
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    return parts.join(".");
  }
