<%--
  Created by IntelliJ IDEA.
  User: kiril
  Date: 14.05.2021
  Time: 20:33
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html lang="ru">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!--Import Google Icon Font-->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <!--Import materialize.css-->
    <link type="text/css" rel="stylesheet" href="<c:url value="/css/materialize.min.css" />"  media="screen,projection"/>
    <link type="text/css" rel="stylesheet" href="<c:url value="/css/main.css" />"  media="screen,projection"/>
    <title>Car Sales</title>
</head>
<body>
<div>
    <c:import url="_menu.jsp"/>
    <div class="container js-main-container main-container">
        <h3 class="center-align page-title">ваши объявления</h3>
        <div class="js-user-announcement">
            <div class="row center-align">Вы не подали еще ни одного объявления!</div>
        </div>
    </div>
    <div id="modal1" class="modal js-modal modal-custom">
        <div class="modal-content js-modal-content">
            <p class="js-modal-msg center-align"></p>
        </div>
        <div class="modal-footer">
            <a href="#!" class="modal-close waves-effect waves-green btn-flat">Ok</a>
        </div>
    </div>
</div>
</div>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script type="text/javascript" src="<c:url value="/js/materialize.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/function.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/profile.js" />"></script>
</body>
</html>