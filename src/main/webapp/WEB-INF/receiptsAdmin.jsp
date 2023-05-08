<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page errorPage="../error.jsp" isErrorPage="false" %>
<%@ page import="dat.backend.model.entities.OrderState" %>

<t:pagetemplate>
    <jsp:attribute name="header">
        AdminPanel - Bestillinger
    </jsp:attribute>

    <jsp:attribute name="footer">
        AdminPanel - Bestillinger
    </jsp:attribute>
    <jsp:body>

        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <h1>Under behandling</h1>

                    <table class="table table-dark table-striped">
                        <thead>
                        <tr>
                            <th scope="col">Ordre ID</th>
                            <th scope="col">Kunde ID</th>
                            <th scope="col">Dato</th>
                            <th scope="col">Totalpris</th>
                            <th scope="col">Status</th>
                            <th scope="col">Brede</th>
                            <th scope="col">Længde</th>
                            <th scope="col">Kommentar</th>
                        </tr>
                        </thead>

                        <c:forEach var="receipts" items="${requestScope.receiptsList}">
                            <c:if test="${receipts.orderstate == OrderState.OPEN}">
                                <tbody>
                                <tr>
                                    <td>${receipts.idReceipt}</td>
                                    <td>${receipts.idUser}</td>
                                    <td>${receipts.timeOfOrder}</td>
                                    <td>${receipts.price}</td>
                                    <td>${receipts.orderstate}</td>
                                    <td>${receipts.width}</td>
                                    <td>${receipts.length}</td>
                                    <td>${receipts.comment}</td>
                                </tr>
                                </tbody>
                            </c:if>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>


        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <h1>Tilbud</h1>

                    <table class="table table-dark table-striped">
                        <thead>
                        <tr>
                            <th scope="col">Ordre ID</th>
                            <th scope="col">Kunde ID</th>
                            <th scope="col">Dato</th>
                            <th scope="col">Totalpris</th>
                            <th scope="col">Status</th>
                            <th scope="col">Brede</th>
                            <th scope="col">Længde</th>
                            <th scope="col">Kommentar</th>
                        </tr>
                        </thead>

                        <c:forEach var="receipts" items="${requestScope.receiptsList}">
                            <c:if test="${receipts.orderstate == OrderState.OFFER}">
                                <tbody>
                                <tr>
                                    <td>${receipts.idReceipt}</td>
                                    <td>${receipts.idUser}</td>
                                    <td>${receipts.timeOfOrder}</td>
                                    <td>${receipts.price}</td>
                                    <td>${receipts.orderstate}</td>
                                    <td>${receipts.width}</td>
                                    <td>${receipts.length}</td>
                                    <td>${receipts.comment}</td>
                                </tr>
                                </tbody>
                            </c:if>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>

        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <h1>Kvitteringer</h1>

                    <table class="table table-dark table-striped">
                        <thead>
                        <tr>
                            <th scope="col">Ordre ID</th>
                            <th scope="col">Kunde ID</th>
                            <th scope="col">Dato</th>
                            <th scope="col">Totalpris</th>
                            <th scope="col">Status</th>
                            <th scope="col">Brede</th>
                            <th scope="col">Længde</th>
                            <th scope="col">Kommentar</th>
                        </tr>
                        </thead>

                        <c:forEach var="receipts" items="${requestScope.receiptsList}">
                            <c:if test="${receipts.orderstate == OrderState.COMPLETE}">
                                <tbody>
                                <tr>
                                    <td>${receipts.idReceipt}</td>
                                    <td>${receipts.idUser}</td>
                                    <td>${receipts.timeOfOrder}</td>
                                    <td>${receipts.price}</td>
                                    <td>${receipts.orderstate}</td>
                                    <td>${receipts.width}</td>
                                    <td>${receipts.length}</td>
                                    <td>${receipts.comment}</td>
                                </tr>
                                </tbody>
                            </c:if>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>
    </jsp:body>
</t:pagetemplate>