<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page errorPage="../error.jsp" isErrorPage="false" %>
<%@ page import="dat.backend.model.entities.OrderState" %>

<t:pagetemplate>
    <jsp:attribute name="header">
            Dine bestillinger
    </jsp:attribute>

    <jsp:attribute name="footer">
        Dine bestillinger
    </jsp:attribute>

    <jsp:body>
    <style>
        h1
        {
            text-align: center;
        }
    </style>

        <div class="mt-3">
            <strong>${sessionScope.msg}</strong>
            <h3>Under behandling</h3>
            <table class="table table-dark table-striped">
                <tr>
                    <th>Ordrenummer</th>
                    <th>Tidspunkt</th>
                    <th>Pris</th>
                    <th>Handlinger</th>
                </tr>

               <c:forEach var="receipt" items="${requestScope.receiptList}">
                    <c:if test="${receipt.orderState == OrderState.OPEN}">
                        <tr>
                            <td>${receipt.idReceipt}</td>
                            <td>${receipt.timeOfOrder}</td>
                            <td>
                                Fog behandler din ordre
                            </td>
                            <td>
                                Fog behandler din ordre
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>

            </table>
        </div>

        <div class="mt-3">
            <strong>${sessionScope.msg}</strong>
            <h3>Tilbud</h3>
            <table class="table table-dark table-striped">
                <tr>
                    <th>Ordrenummer</th>
                    <th>Tidspunkt</th>
                    <th>Pris</th>
                    <th>Handlinger</th>
                </tr>

                <c:forEach var="receipt" items="${requestScope.receiptList}">
                    <c:if test="${receipt.orderState == OrderState.OFFER}">
                        <tr>
                            <td>${receipt.idReceipt}</td>
                            <td>${receipt.timeOfOrder}</td>
                            <td>${receipt.price}</td>
                            <td>
                                <form action="updatereceipt" method="post">
                                    <input type="text" hidden name="idReceipt" value="${receipt.idReceipt}">
                                    <input type="submit" class="btn btn-secondary" value="Accepter">
                                </form>
                                <form action="deletereceipt" method="post" onsubmit="return confirm('Er du sikker på at du vil slette denne ordre?')">
                                    <input type="text" hidden name="idReceipt" value="${receipt.idReceipt}">
                                    <input type="submit" class="btn btn-secondary" value="Afvis">
                                </form>
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>

            </table>
        </div>

        <div class="mt-3">
            <h3>Kvitteringer</h3>
            <table class="table table-dark table-striped">
                <tr>
                    <th>Ordrenummer</th>
                    <th>Tidspunkt</th>
                    <th>Pris</th>
                    <th>Handlinger</th>
                </tr>

                <c:forEach var="receipt" items="${requestScope.receiptList}">
                    <c:if test="${receipt.orderState == OrderState.COMPLETE}">
                        <tr>
                            <td>${receipt.idReceipt}</td>
                            <td>${receipt.timeOfOrder}</td>
                            <td>${receipt.price}</td>
                            <td>
                                <form action="itemslist" method="post">
                                    <input type="text" hidden name="idReceipt" value="${receipt.idReceipt}">
                                    <input type="submit" class="btn btn-secondary" value="Se stykliste">
                                </form>
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>

            </table>
        </div>

    </jsp:body>

</t:pagetemplate>