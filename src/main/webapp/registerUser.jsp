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
            <div class="card mt-2 p-1" style="position: center; margin-left: 25%; margin-right: 25%;">
                <h1>SignUp</h1>

                <img src="images/loginAndSignup/login-Icon.png" alt="login icon" width="125" height="125"
                     style="display: block; margin: auto;">
                <hr>

                <div class="mt-1">
                    <form action="signup" method="post">

                            <%-- Email --%>
                        <label for="email" class="fw-bold popupSignUpEmail"> Email </label><br/>
                        <input type="text" id="email" name="email" style="width: 100%; height: 35px;"
                               placeholder="Angiv Email"/>

                        <br/>
                        <br/>
                            <%-- PASSWORD --%>
                        <label for="password" class="fw-bold popupSignUpPassword"> Password </label><br/>
                        <input type="password" id="password" name="password" style="width: 100%; height: 35px;"
                               placeholder="Angiv Password"/>

                        <br/>
                        <br/>
                            <%-- ADDRESS --%>
                        <label for="address" class="fw-bold popupSignUpAddress"> Adresse</label><br/>
                        <input type="text" id="address" name="address" style="width: 100%; height: 35px;"
                               placeholder="Angiv vej navn"/>

                        <br/>
                        <br/>
                            <%-- CITY --%>
                        <label for="city" class="fw-bold popupSignUpCity"> By</label><br/>
                        <input type="text" id="city" name="city" style="width: 100%; height: 35px;"
                               placeholder="Angiv by navn"/>


                        <br/>
                        <br/>
                            <%-- PHONE NUMBER --%>
                        <label for="phoneNumber" class="fw-bold popupSignUpPhoneNumber"> Telefon nummer</label><br/>
                        <input type="number" id="phoneNumber" name="phoneNumber" style="width: 100%; height: 35px;"
                               placeholder="Angiv telefon nummer"/>

                        <br/>
                        <br/>
                            <%-- SIGNUP BTN (SUBMITS ) --%>
                        <input type="submit" class="btn btn-primary fw-bold" style="width: 100%; color: white;"
                               value="SignUp"/>
                        <br>
                    </form>
                </div>
            </div>

                <%-- GRAY BOX WITH "AFBRUD" BTN  --%>
            <div style="background-color: lightgray; height: 90px; margin-left: 25%; margin-right: 25%; display: flex; justify-content: space-between; align-items: center;">
                <div style="margin-left: 5px;">
                    <form action="index.jsp" method="post">
                        <input type="submit" class="btn btn-danger fw-bold" value="Afbrud">
                    </form>
                </div>
            </div>
        </div>

    </jsp:body>
</t:pagetemplate>