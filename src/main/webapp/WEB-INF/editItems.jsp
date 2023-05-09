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
        <div class="mt-2">
            <form action="redirectadminpanel" method="post">
                <input type="submit" class="btn btn-primary fw-bold" value="Tilbage">
            </form>
        </div>





        <div class="mt-2">
            <form action="redirectadminpanel" method="post">
                <input type="submit" class="btn btn-primary fw-bold" value="Tilbage">
            </form>
        </div>

    </jsp:body>
</t:pagetemplate>