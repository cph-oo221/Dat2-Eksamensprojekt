<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page errorPage="../error.jsp" isErrorPage="false" %>
<%@ page import="dat.backend.model.entities.OrderState" %>
<%@ page import="dat.backend.model.entities.Wood" %>
<%@ page import="dat.backend.model.entities.Metal" %>

<t:pagetemplate>
    <jsp:attribute name="header">
        Stykliste
    </jsp:attribute>

    <jsp:attribute name="footer">
        Stykliste
    </jsp:attribute>

    <jsp:body>
        <div class="container mt-4">
        <div class="row">
            <div class="col-sm-12">

                <table class="table table-dark table-striped">
                    <thead>
                    <tr>
                        <th scope="col">Vare</th>
                        <th scope="col">Antal</th>
                        <th scope="col">Pris stk</th>
                        <th scope="col">Beskrivelse</th>
                    </tr>
                    </thead>

                    <thead>
                    <tr style="color: white;" class="table">
                        <th scope="col">x</th>
                        <th scope="col"></th>
                        <th scope="col"></th>
                        <th scope="col"></th>
                    </tr>
                    </thead>

                    <thead>
                    <tr>
                        <th scope="col">Træ & Tagplader</th>
                        <th scope="col"></th>
                        <th scope="col"></th>
                        <th scope="col"></th>
                    </tr>
                    </thead>
                    <c:forEach var="item" items="${requestScope.woodList}">
                            <tbody>
                            <tr>
                                <td>${item.material.name}</td>
                                <td>${item.amount}</td>
                                <td>${item.material.price}</td>
                                <td>${item.desc}</td>
                            </tr>
                            </tbody>
                    </c:forEach>
                </table>
            </div>
        </div>
        <br>
        <div class="row">
        <div class="col-sm-12">

            <table class="table table-dark table-striped">
                <thead>
                <tr>
                    <th scope="col">Vare</th>
                    <th scope="col">Antal</th>
                    <th scope="col">Pris stk</th>
                    <th scope="col">Beskrivelse</th>
                </tr>
                </thead>
                <thead>
                <tr>
                    <th scope="col">Skruer & Beslag</th>
                    <th scope="col"></th>
                    <th scope="col"></th>
                    <th scope="col"></th>
                </tr>
                </thead>
                <c:forEach var="item" items="${requestScope.metalList}">
                    <%--<c:if test="${item.material.class.name.equals('Metal.class')}">--%>
                        <tbody>
                        <tr>
                            <td>${item.material.name}</td>
                            <td>${item.amount}</td>
                            <td>${item.material.price}</td>
                            <td>${item.desc}</td>
                        </tr>
                        </tbody>
                   <%-- </c:if>--%>
                </c:forEach>
            </table>

        </div>

        <div class="ms-3 mt-2">

            <c:if test="${sessionScope.user.role.equals('admin')}">
                <h3 style="color: green">Materiale pris ${requestScope.netPrice} kr.</h3>
            </c:if>
            <h3 style="color: green">Fog's pris ${requestScope.totalPrice} kr.</h3>
            <c:if test="${sessionScope.user.role.equals('admin') && requestScope.orderState == OrderState.OPEN}">
                <div class="mt-2">
                        <%-- TODO needs an action to update price!!! --%>
                    <form action="updateprice" method="post">

                        <input type="number" id="pris" name="pris" style="height: 35px;"/>
                        <input type="number" hidden name="idReceipt" value="${requestScope.idReceipt}"/>
                        <input type="submit" class="btn btn-primary fw-bold mb-1" value="Sæt pris"/>
                    </form>
                </div>
            </c:if>
        </div>


    </jsp:body>
</t:pagetemplate>