<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page errorPage="../error.jsp" isErrorPage="false" %>

<t:pagetemplate>
    <jsp:attribute name="header">
            Your Orders
    </jsp:attribute>

    <jsp:attribute name="footer">
        Your Orders
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
            <h3>Incomplete orders</h3>
            <table class="table table-dark table-striped">
                <tr>
                    <th>Order Number</th>
                    <th>Time Of Order</th>
                    <th>Actions</th>
                </tr>

                <c:forEach var="receipt" items="${requestScope.receiptList}">
                    <c:if test="${receipt.complete == false}">
                        <tr>
                            <td>${receipt.idReceipt}</td>
                            <td>${receipt.timeOfOrder}</td>
                            <td>
                                <form action="Orders" method="post">
                                    <input type="text" hidden name="idReceipt" value="${receipt.idReceipt}">
                                    <input type="text" hidden name="complete" value="${receipt.complete}">
                                    <input type="submit" class="btn btn-secondary" value="View order">
                                </form>
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>

            </table>
        </div>

        <div class="mt-3">
            <h3>Complete orders</h3>
            <table class="table table-dark table-striped">
                <tr>
                    <th>Order Number</th>
                    <th>Time Of Order</th>
                    <th>Actions</th>
                </tr>

                <c:forEach var="receipt" items="${requestScope.receiptList}">
                    <c:if test="${receipt.complete == true}">
                        <tr>
                            <td>${receipt.idReceipt}</td>
                            <td>${receipt.timeOfOrder}</td>
                            <td>
                                <form action="Orders" method="post">
                                    <input type="text" hidden name="idReceipt" value="${receipt.idReceipt}">
                                    <input type="text" hidden name="complete" value="${receipt.complete}">
                                    <input type="submit" class="btn btn-secondary" value="View order">
                                </form>
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>

            </table>
        </div>

    </jsp:body>

</t:pagetemplate>