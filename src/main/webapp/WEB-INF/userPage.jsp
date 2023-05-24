<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page errorPage="../error.jsp" isErrorPage="false" %>

<t:pagetemplate>
    <jsp:attribute name="header">
            Brugerside
    </jsp:attribute>

    <jsp:attribute name="footer">
        Brugerside
    </jsp:attribute>

    <jsp:body>
        <div class="mt-2">
            <div class="row">
                <div class="col mt-1">
                    <h2>Design en carport</h2>
                    <form action="orderpage" method="post">
                        <input type="image" src="<c:url value="/images/UserPage/DesignEnCarport.png"/>" alt="Submit"/>
                    </form>
                </div>

                <div class="col mt-1">
                    <h2>Se ordrer</h2>

                    <form action="receipts" method="post">
                        <input type="image" src="<c:url value="/images/AdminPanel/Bestillinger.png"/>" alt="Submit"/>
                    </form>
                </div>
            </div>
        </div>
    </jsp:body>
</t:pagetemplate>