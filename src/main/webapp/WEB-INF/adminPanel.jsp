<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page errorPage="../error.jsp" isErrorPage="false" %>

<t:pagetemplate>
    <jsp:attribute name="header">
        AdminPanel
    </jsp:attribute>

    <jsp:attribute name="footer">
        AdminPanel
    </jsp:attribute>

    <jsp:body>

        <div class="mt-2">
            <div class="row">
                <div class="col mt-1">
                    <h2>Varelager:</h2>

                    <form action="edititems" method="post">
                        <input type="image" src="<c:url value="/images/AdminPanel/Varelager_1.png"/>" alt="Submit"/>
                    </form>
                </div>

                <div class="col mt-1">
                    <h2>Bestillinger: </h2>

                    <form action="receiptsadmin" method="post">
                        <input type="image" src="<c:url value="/images/AdminPanel/Bestillinger.png"/>" alt="Submit"/>
                    </form>
                </div>
            </div>
        </div>

    </jsp:body>
</t:pagetemplate>