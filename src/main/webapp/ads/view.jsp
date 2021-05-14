<%--
  Created by IntelliJ IDEA.
  User: kiril
  Date: 14.05.2021
  Time: 19:51
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
    <link type="text/css" rel="stylesheet" href="<c:url value="/css/materialize.min.css" />" media="screen,projection"/>
    <link type="text/css" rel="stylesheet" href="<c:url value="/css/main.css" />" media="screen,projection"/>
    <title>Car Sales</title>
</head>
<body>
<div>
    <c:import url="../_menu.jsp"/>
    <div class="container js-main-container main-container">
        <h3 class="center-align page-title"><span class="js-model"><c:out value="${ads.car.carModel.name}"/></span>, <span class="js-price"><c:out value="${ads.price}"/> &#x20bd;</span></h3>
        <div class="row card auto-form">
            <div class="col s7 image-container">
                <c:forEach var="carPhoto" items="${ads.car.carPhotos}">
                    <img src="<c:url value='/ads/photo?name=${carPhoto.id}'/>" width="500" >
                </c:forEach>
                <c:if test="${empty ads.car.carPhotos}">
                    <img src="${pageContext.request.contextPath}/photos/noimage2.jpeg" width="1380">
                </c:if>
            </div>
            <div class="col s5 property-container">
                <div class="col s12">
                    <span class="prop-name">Город:</span><span class="prop-val js-city" ><c:out value="${ads.city.name}"/></span>
                </div>
                <div class="col s12">
                    <span class="prop-name">Пробег:</span> <span class="prop-val js-mileage"><c:out value="${ads.car.mileage}"/></span> км
                </div>
                <div class="col s12">
                    <span class="prop-name">Состояние:</span> <span class="prop-val js-is_broken">
                        <c:if test="${ads.car.broken}">битый</c:if>
                        <c:if test="${not ads.car.broken}">не битый</c:if>
                    </span>
                </div>
                <div class="col s12">
                    <span class="prop-name">Кузов:</span> <span class="prop-val js-body_type"><c:out value="${ads.car.carBodyType.name}"/></span>
                </div>
                <div class="col s12">
                    <span class="prop-name">Двигатель:</span> <span class="prop-val js-engine"><c:out value="${ads.car.carEngineType.name}"/></span>
                </div>
                <div class="col s12">
                    <span class="prop-name">Коробка передач:</span> <span class="prop-val js-transmission_box"><c:out value="${ads.car.carTransmissionBoxType.name}"/></span>
                </div>
                <div class="col s12 description js-description">
                    <c:out value="${ads.car.description}"/>
                </div>
                <div class="col s12 phone-number center-align">
                    <c:if test="${ads.user.id == user.id}">
                        <p>
                            <label>
                                <input data-ads_id="<c:out value="${ads.id}"/>" type="checkbox" class="filled-in js-is_sold" <c:if test="${ads.sold}">checked</c:if>>
                                <span>продано</span>
                            </label>
                        </p>
                    </c:if>
                    <c:if test="${ads.user.id != user.id}">
                        <a href="tel:<c:out value="${ads.user.phone}"/>" class="waves-effect waves-light btn">
                            позвонить
                        </a>
                    </c:if>
                </div>
            </div>
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
<script type="text/javascript" src="<c:url value="/js/view.js" />"></script>
</body>
</html>