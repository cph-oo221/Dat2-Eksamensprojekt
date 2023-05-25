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

        <c:if test="${sessionScope.user.role.equals('admin')}">
            <div class="mt-2">
                <form action="receiptsadmin" method="post">
                    <input type="submit" class="btn btn-primary fw-bold" value="Tilbage">
                </form>
            </div>
        </c:if>
        <c:if test="${sessionScope.user.role.equals('user')}">
            <div class="mt-2">
                <form action="receipts" method="post">
                    <input type="submit" class="btn btn-primary fw-bold" value="Tilbage">
                </form>
            </div>
        </c:if>

        <div class="container mt-4">
            <div class="row">
                <div class="col-sm-12">

                    <table class="table table-dark table-striped">
                        <thead>
                        <tr>
                            <th scope="col">Vare</th>
                            <th scope="col">Antal</th>
                            <th scope="col">Enhed</th>
                            <th scope="col">Pris stk</th>
                            <th scope="col">Beskrivelse</th>
                        </tr>
                        </thead>

                        <thead>
                        <tr style="color: white;" class="table">
                            <th scope="col">&nbsp;</th>
                            <th scope="col"></th>
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
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <c:forEach var="item" items="${requestScope.woodList}">
                            <tbody>
                            <tr>
                                <td>${item.material.name}</td>
                                <td>${item.amount}</td>
                                <td>${item.material.unit}</td>
                                <td>${item.material.price}</td>
                                <td>${item.desc}</td>
                            </tr>
                            </tbody>
                        </c:forEach>

                        <thead>
                        <tr style="color: white;" class="table">
                            <th scope="col">&nbsp;</th>
                            <th scope="col"></th>
                            <th scope="col"></th>
                            <th scope="col"></th>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <thead>
                        <tr>
                            <th scope="col">Skruer & Beslag</th>
                            <th scope="col"></th>
                            <th scope="col"></th>
                            <th scope="col"></th>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <c:forEach var="item" items="${requestScope.metalList}">
                            <tbody>
                            <tr>
                                <td>${item.material.name}</td>
                                <td>${item.amount}</td>
                                <td>${item.material.unit}</td>
                                <td>${item.material.price}</td>
                                <td>${item.desc}</td>
                            </tr>
                            </tbody>
                        </c:forEach>
                    </table>
                </div>
            </div>

        </div>

        <div class="ms-3 mt-2">

            <c:if test="${sessionScope.user.role.equals('admin')}">
                <h3 class="colorGreen">Materiales pris ${requestScope.netPrice} kr.</h3>
            </c:if>
            <h3 class="colorGreen">Fog's pris ${requestScope.totalPrice} kr.</h3>
            <c:if test="${sessionScope.user.role.equals('admin') && requestScope.orderState == OrderState.OPEN}">
                <div class="mt-2">
                    <form action="updateprice" method="post">

                        <input type="number" id="pris" name="pris" class="inputBoxSizeHeight"/>
                        <input type="number" hidden name="idReceipt" value="${requestScope.idReceipt}"/>
                        <input type="number" hidden name="netPrice" value="${requestScope.netPrice}"/>
                        <input type="submit" class="btn btn-primary fw-bold mb-1" value="Sæt pris"/>
                    </form>
                </div>
            </c:if>
        </div>


    </jsp:body>
</t:pagetemplate>