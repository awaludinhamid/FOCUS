<%-- 
    Document   : loginpage
    Created on : Jun 19, 2015, 1:00:45 PM
    Author     : awal
--%>
<%@include file="header.jsp" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="../../css/login.css"/>
  </head>
<body>
  <div class="container">
    <form class="form-signin" method="POST" action="../../apps/auth/security">
      <h3 class="form-signin-heading">Please sign in to continue:</h3>
      <!--img src="../../img/avatar_2x.png" alt="avatar" width="280" height="160"/-->
      <svg width="280" height="160" overflow="hidden">
        <circle cx="160" cy="80" r="60" fill="#bbb"/>
        <circle cx="160" cy="55" r="18" fill="#aaa"/>
        <ellipse cx="160" cy="110" rx="40" ry="25" fill="#aaa"/>
        <ellipse cx="160" cy="115" rx="40" ry="25" fill="#aaa"/>
        <rect x="120" y="110" width="80" height="10" fill="#aaa"/>
      </svg>
      <br/>
      <label for="j_username" class="sr-only">Username</label>
      <input type="text" name="j_username" class="form-control" placeholder="Username" required autofocus>
      <label for="j_password" class="sr-only">Password</label>
      <input type="password" name="j_password" class="form-control" placeholder="Password" required>
      <span class="error-login">${error}</span>
      <div class="checkbox">
        <label>
          <input type="checkbox" value="remember-me"> Remember me
        </label>
      </div>
      <input class="btn btn-lg btn-primary btn-block" type="submit" value="Sign in" />
    </form>
  </div>

  <!--
<h1>

Login</h1><div id="login-error">

{error}
  </div><form action="../../j_spring_security_check" method="post" >



<p>


 <label for="j_username">Username</label>
 <input id="j_username" name="j_username" type="text" />
</p><p>


 <label for="j_password">Password</label>
 <input id="j_password" name="j_password" type="password" />
</p><input  type="submit" value="Login"/>

</form>
  -->
</body>
</html>