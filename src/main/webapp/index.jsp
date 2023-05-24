<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page errorPage="error.jsp" isErrorPage="false" %>

<t:pagetemplate>
    <jsp:attribute name="header">
        Fog - Log ind
    </jsp:attribute>

    <jsp:attribute name="footer">
        Fog - Log ind
    </jsp:attribute>

    <jsp:body>

        <div class="container mt-3">
            <div class="card mt-2 p-1 cardPos">
                <h1>Log ind</h1>

                <img src="images/loginAndSignup/login-Icon.png" alt="login icon" class="imgDisplay">
                <hr>
                <div class="text-center errorMSG">
                    <p> ${requestScope.errorMSG}</p>
                </div>

                <div class="mt-1">
                    <form action="login" method="post">
                        <p class="errorMSG">${requestScope.errormessage}</p>

                        <label for="email" class="fw-bold popupLoginEmail"> E-mail </label><br/>
                        <input type="text" id="email" name="email" class="inputBoxSize" placeholder="Angiv e-mail"/>

                        <br/>
                        <br/>
                        <label for="password" class="fw-bold popupLoginPassword"> Kodeord </label><br/>
                        <input type="password" id="password" name="password" class="inputBoxSize" placeholder="Angiv kodeord"/>

                        <br/>
                        <br/>
                        <input type="submit" class="btn btn-primary fw-bold submitBtnSize" value="Log ind"/>
                        <br>
                    </form>
                </div>
            </div>

                <%-- GRAY BOX WITH CREATED NEW USER "OPRET NY BRUGER" --%>
            <div class="greyBox">
                <div class="greyBoxSpacing">
                    <form action="registerUser.jsp" method="post">
                        <input type="submit" class="btn btn-link noLineUnderBtn" value="opret ny bruger?">
                    </form>
                </div>
            </div>
        </div>
    </jsp:body>
</t:pagetemplate>