<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page errorPage="../error.jsp" isErrorPage="false" %>

<t:pagetemplate>
    <jsp:attribute name="header">
            Bruger Side
    </jsp:attribute>

    <jsp:attribute name="footer">
        Bruger Side
    </jsp:attribute>

    <jsp:body>

        <div class="mt-3">
            <h3>Design en carport</h3>
            <form action="orderpage" method="post">
                <input type="submit" value="lav en ordre">
            </form>
        </div>

        <div class="mt-3">
            <h3>Se dine ordrer</h3>
            <form action="receipts" method="post">
                <input type="submit" value="Se ordrer">
            </form>
        </div>
    </jsp:body>
</t:pagetemplate>