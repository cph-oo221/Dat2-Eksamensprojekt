<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page errorPage="error.jsp" isErrorPage="false" %>

<t:pagetemplate>
    <jsp:attribute name="header">
         Fog - Opret Ny Bruger
    </jsp:attribute>

    <jsp:attribute name="footer">
        Fog - Opret Ny Bruger
    </jsp:attribute>

    <jsp:body>

        <div class="container mt-3">
            <div class="card mt-2 p-1 cardPos">
                <h1>Opret Bruger</h1>

                <img src="images/loginAndSignup/login-Icon.png" alt="login icon" class="imgDisplay">
                <hr>
                    <div class="text-center errorMSG">
                        <p>${requestScope.errorMSG}</p>
                    </div>
                <div class="mt-1">
                    <form action="signup" method="post">

                            <%-- Email --%>
                        <label for="email" class="fw-bold popupSignUpEmail"> Email </label><br/>
                        <input type="text" id="email" name="email" class="inputBoxSize" placeholder="Angiv Email"/>

                        <br/>
                        <br/>
                            <%-- PASSWORD --%>
                        <label for="password" class="fw-bold popupSignUpPassword"> Password </label><br/>
                        <input type="password" id="password" name="password" class="inputBoxSize" placeholder="Angiv Password"/>

                        <br/>
                        <br/>
                            <%-- ADDRESS --%>
                        <label for="address" class="fw-bold popupSignUpAddress"> Adresse</label><br/>
                        <input type="text" id="address" name="address" class="inputBoxSize" placeholder="Angiv vej navn"/>

                        <br/>
                        <br/>
                            <%-- CITY --%>
                        <label for="city" class="fw-bold popupSignUpCity"> By</label><br/>
                        <input type="text" id="city" name="city" class="inputBoxSize" placeholder="Angiv by navn"/>


                        <br/>
                        <br/>
                            <%-- PHONE NUMBER --%>
                        <label for="phoneNumber" class="fw-bold popupSignUpPhoneNumber"> Telefon nummer</label><br/>
                        <input type="number" id="phoneNumber" name="phoneNumber" class="inputBoxSize" placeholder="Angiv telefon nummer"/>

                        <br/>
                        <br/>
                            <%-- SIGNUP BTN (SUBMITS ) --%>
                        <input type="submit" class="btn btn-primary fw-bold submitBtnSize" value="Opret Bruger"/>
                        <br>
                    </form>
                </div>
            </div>

                <%-- GRAY BOX WITH "AFBRYD" BTN  --%>
            <div class="greyBox">
                <div class="greyBoxSpacing">
                    <form action="index.jsp" method="post">
                        <input type="submit" class="btn btn-danger fw-bold" value="Afbryd">
                    </form>
                </div>
            </div>
        </div>

    </jsp:body>
</t:pagetemplate>