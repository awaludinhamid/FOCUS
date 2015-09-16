$(document).ready(function(){
  var arrPage = ["login","home","application","admin"];
  $("#mdl-logout .modal-footer button#btn-ok").click(function(){
    window.location.replace("/FOCUS/apps/auth/logout");
  });
  var path = window.location.pathname;
  var page = path.split("/").pop().replace("#","");
  var addPar = page.indexOf(";");
  if(addPar > -1) {
    page = page.substring(0,addPar);
  }
  var cnName = $("#cnname").text();
  $("#pageLabel").text(initCap(page));
  if(cnName == null || cnName == "") {
    $("#userMenu").append(
      "<span class='label label-default full-width'>Not logged in</span>"
    );
  } else if($.inArray(page, arrPage) > -1){
    $("#userMenu").append(
      "<button class='btn btn-sm' style='cursor: not-allowed'>Welcome, " + cnName + "</button>" +
      "<button data-toggle='dropdown' class='btn btn-sm dropdown-toggle' style=''><span class='caret'></span></button>" +
      "<ul class='dropdown-menu pull-right'>" +
        "<li id='idLogout'><a href='#'><i class='glyphicon glyphicon-off'>&nbsp;</i>Logout</a></li>" +
        ((page == "application" || page == "admin") ?
          "<li id='idRefresh'><a href='#'><i class='glyphicon glyphicon-refresh'>&nbsp;</i>Refresh Data</a></li>" : "") +
        ((page == "home") ?
          "" : "<li><a href='/FOCUS/apps/main/home'><i class='glyphicon glyphicon-home'>&nbsp;</i>Home</a></li>") +
        ((page == "application") ?
          "" : "<li><a href='/FOCUS/apps/main/application'><i class='glyphicon glyphicon-tags'>&nbsp;</i>Aplikasi</a></li>") +
        ((page == "admin") ?
          "" : "<li><a href='/FOCUS/apps/main/admin'><i class='glyphicon glyphicon-lock'>&nbsp;</i>Admin</a></li>") +
      "</ul>"
    );
    $(".dropdown-toggle").dropdown();
    $("#idLogout").on("click", function() {
      $("#mdl-logout").modal("show");
    });
  } else {
    $("#userMenu").append(
      "<span class='label label-default full-width'>Error</span>"
    );
  }
});