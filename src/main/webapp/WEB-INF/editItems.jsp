<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page errorPage="../error.jsp" isErrorPage="false" %>

<t:pagetemplate>
    <jsp:attribute name="header">
        AdminPanel - Varelager
    </jsp:attribute>

    <jsp:attribute name="footer">
        AdminPanel - Varelager
    </jsp:attribute>

    <jsp:body>
        <div class="mt-4">
            <form action="redirectadminpanel" method="post">
                <input type="submit" class="btn btn-primary fw-bold" value="Tilbage">
            </form>
        </div>

        <div class="container mt-3">
            <div class="row">
                <div class="col text-center">
                    <form action="" method="post">

                        <input type="submit" class="btn btn-primary fw-bold" value="Sortere [aA-åÅ]">
                    </form>
                </div>


                <div class="col text-center">
                    <form action="" method="post">


                        <input type="text" id="search" name="search" style="height: 35px;" placeholder="Search..."/>
                        <input type="submit" class="btn btn-primary fw-bold mb-1" value="Søg"/>
                    </form>
                </div>


                <div class="col text-center">
                    <form action="" method="post">

                        <input type="submit" class="btn btn-primary fw-bold mb-1" value="Nulstil"/>
                    </form>
                </div>
            </div>
        </div>

        <div class="container mt-4">
            <div class="row">
                <div class="col-sm-12">
                    <table class="table table-dark table-striped">
                        <thead>
                        <tr>
                            <th scope="col">Træ ID</th>
                            <th scope="col">Name</th>
                            <th scope="col">Længde</th>
                            <th scope="col">Bredde</th>
                            <th scope="col">Højde</th>
                            <th scope="col">Enhed</th>
                            <th scope="col">Price</th>
                            <th scope="col">Variant</th>
                            <th scope="col">Handling</th>
                        </tr>
                        </thead>
                        <c:forEach var="wood" items="${requestScope.woodList}">
                            <tbody>
                            <tr>
                                <td>${wood.idWood}</td>
                                <td>${wood.name}</td>
                                <td>${wood.length}</td>
                                <td>${wood.width} kr.</td>
                                <td>${wood.height}</td>
                                <td>${wood.unit}</td>
                                <td>${wood.price}</td>
                                <td>${wood.variant}</td>
                                <td class="text-center">
                                        <%--TODO need action--%>
                                    <form action="" method="post"
                                          onsubmit="return confirm('Denne handling medføre, at træet: '
                                                  + 'ID:' + ${wood.idWood} + ', Name: ' + ${wood.name} + '. bliver slette, er du sikker?')">

                                        <input type="number" hidden name="idWood" value="${wood.idWood}">

                                        <input type="submit" class="btn btn-danger fw-bold" value="Slet">
                                    </form>
                                </td>
                            </tr>
                            </tbody>
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