<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page errorPage="../error.jsp" isErrorPage="false" %>

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
                            <th scope="col">Prise</th>
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
                        <c:forEach var="item" items="${requestScope.itemList}">
                        <tbody>
                        <tr>
                            <td>${item.name}</td>
                            <td>${item.amount}</td>
                            <td>${item.price}</td>
                            <td>${item.description}</td>
                        </tr>
                        </tbody>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>

        <div class="ms-3 mt-2">
            <h3 style="color: green">Total pris 17000 kr.</h3>

            <c:if test="${sessionScope.user.role.equals('admin')}">
                <div class="mt-2">
                    <%-- TODO needs an action to update price!!! --%>
                    <form action="" method="post">

                        <input type="number" id="pris" name="pris" style="height: 35px;"/>
                        <input type="submit" class="btn btn-primary fw-bold mb-1" value="Sæt pris"/>
                    </form>
                </div>
            </c:if>
        </div>


    </jsp:body>
</t:pagetemplate>