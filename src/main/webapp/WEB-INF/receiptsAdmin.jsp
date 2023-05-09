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

        <div class="mt-2">
            <form action="redirectadminpanel" method="post">
                <input type="submit" class="btn btn-primary fw-bold" value="Tilbage">
            </form>
        </div>

        <div class="container mt-2">
            <div class="row">
                <div class="col-sm-12">
                    <h1>Under behandling</h1>

                    <table class="table table-dark table-striped">
                        <thead>
                        <tr>
                            <th scope="col">Ordre ID</th>
                            <th scope="col">Kunde ID</th>
                            <th scope="col">Email</th>
                            <th scope="col">Tidspunkt</th>
                            <th scope="col">Pris</th>
                            <th scope="col">Status</th>
                            <th scope="col">Bredde</th>
                            <th scope="col">længde</th>
                            <th scope="col">Kommentar</th>
                            <th scope="col">Handling</th>

                        </tr>
                        </thead>

                        <c:forEach var="receipts" items="${requestScope.receiptsList}">
                            <c:if test="${receipts.orderstate == OrderState.OPEN}">
                                <tbody>
                                <tr>
                                    <td>${receipts.idReceipt}</td>
                                    <td>${receipts.idUser}</td>
                                    <td>
                                        <c:forEach var="users" items="${requestScope.usersList}">
                                            <c:if test="${users.idUser == receipts.idUser}">
                                                ${users.email}
                                            </c:if>
                                        </c:forEach>
                                    </td>
                                    <td>${receipts.timeOfOrder}</td>
                                    <td>${receipts.price} kr.</td>
                                    <td>${receipts.orderstate}</td>
                                    <td>${receipts.width}</td>
                                    <td>${receipts.length}</td>
                                    <td>${receipts.comment}</td>
                                    <td>
                                        <div class="mt-2 text-center">
                                            <form action="itemslist" method="post">

                                                <input type="number" hidden name="idUser" value="${receipts.idUser}"/>
                                                <input type="number" hidden name="idReceipt" value="${receipts.idReceipt}"/>
                                                <input type="number" hidden name="price" value="${receipts.price}"/>
                                                <input type="number" hidden name="width" value="${receipts.width}"/>
                                                <input type="number" hidden name="length" value="${receipts.length}"/>

                                                <input type="submit" class="btn btn-info fw-bold" value="Se stykliste"/>
                                            </form>
                                        </div>

                                        <div class="mt-2 text-center">
                                            <form action="updatereceipt" method="post">
                                                <input type="number" hidden name="idUser" value="${receipts.idUser}"/>
                                                <input type="number" hidden name="idReceipt" value="${receipts.idReceipt}"/>

                                                <input type="submit" class="btn btn-success fw-bold" value="Godkend"/>
                                            </form>
                                        </div>

                                        <div class="mt-2 text-center">
                                            <form action="deletereceipt" method="post" onsubmit="return confirm('Denne handling medføre, at ordren: ' + ${receipts.idReceipt} + '. bliver slette, er du sikker?')">
                                                <input type="number" hidden name="idUser" value="${receipts.idUser}"/>
                                                <input type="number" hidden name="idReceipt" value="${receipts.idReceipt}"/>

                                                <input type="submit" class="btn btn-danger fw-bold" value="Afvis"/>
                                            </form>
                                        </div>
                                    </td>
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
                            <th scope="col">Email</th>
                            <th scope="col">Tidspunkt</th>
                            <th scope="col">Pris</th>
                            <th scope="col">Status</th>
                            <th scope="col">Brede</th>
                            <th scope="col">Længde</th>
                            <th scope="col">Kommentar</th>
                            <th scope="col">Handling</th>
                        </tr>
                        </thead>

                        <c:forEach var="receipts" items="${requestScope.receiptsList}">
                            <c:if test="${receipts.orderstate == OrderState.OFFER}">
                                <tbody>
                                <tr>
                                    <td>${receipts.idReceipt}</td>
                                    <td>${receipts.idUser}</td>
                                    <td>
                                        <c:forEach var="users" items="${requestScope.usersList}">
                                            <c:if test="${users.idUser == receipts.idUser}">
                                                ${users.email}
                                            </c:if>
                                        </c:forEach>
                                    </td>
                                    <td>${receipts.timeOfOrder}</td>
                                    <td>${receipts.price} kr.</td>
                                    <td>${receipts.orderstate}</td>
                                    <td>${receipts.width}</td>
                                    <td>${receipts.length}</td>
                                    <td>${receipts.comment}</td>
                                    <td>
                                        <div class="mt-2 text-center">
                                            <form action="" method="post">

                                                <input type="submit" class="btn btn-info fw-bold" value="Se stykliste"/>
                                            </form>
                                        </div>
                                    </td>
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
                            <th scope="col">Email</th>
                            <th scope="col">Tidspunkt</th>
                            <th scope="col">Pris</th>
                            <th scope="col">Status</th>
                            <th scope="col">Brede</th>
                            <th scope="col">Længde</th>
                            <th scope="col">Kommentar</th>
                            <th scope="col">Handling</th>
                        </tr>
                        </thead>

                        <c:forEach var="receipts" items="${requestScope.receiptsList}">
                            <c:if test="${receipts.orderstate == OrderState.COMPLETE}">
                                <tbody>
                                <tr>
                                    <td>${receipts.idReceipt}</td>
                                    <td>${receipts.idUser}</td>
                                    <td>
                                        <c:forEach var="users" items="${requestScope.usersList}">
                                            <c:if test="${users.idUser == receipts.idUser}">
                                                ${users.email}
                                            </c:if>
                                        </c:forEach>
                                    </td>
                                    <td>${receipts.timeOfOrder}</td>
                                    <td>${receipts.price} kr.</td>
                                    <td>${receipts.orderstate}</td>
                                    <td>${receipts.width}</td>
                                    <td>${receipts.length}</td>
                                    <td>${receipts.comment}</td>
                                    <td class="text-center" style="color: green;"> <h3>Færdiggjort</h3> </td>
                                </tr>
                                </tbody>
                            </c:if>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>


        <div class="mt-2">
            <form action="redirectadminpanel" method="post">
                <input type="submit" class="btn btn-primary fw-bold" value="Tilbage">
            </form>
        </div>
    </jsp:body>
</t:pagetemplate>