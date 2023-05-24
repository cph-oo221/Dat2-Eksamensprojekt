<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page errorPage="../error.jsp" isErrorPage="false" %>

<t:pagetemplate>
    <jsp:attribute name="header">
         Design din carport
    </jsp:attribute>

    <jsp:attribute name="footer">
        Carport Design
    </jsp:attribute>

    <jsp:body>
        <div class="mt-2">
            <form action="userpage" method="post">
                <input type="submit" class="btn btn-primary fw-bold" value="Tilbage">
            </form>
        </div>


        <div class="container mt-4">
            <div class="card mt-2 p-1 cardPos">
                <p>${requestScope.errorMSG}</p>
                <form action="makeorder" method="post">
                    <label for="width" class="fw-bold">Carports bredde:</label>
                        <br>
                        <select name="width" id="width" class="inputBoxSize">
                            <c:if test="${requestScope.defaultSet != null}">
                            <option value="${requestScope.defaultWidth}">Valgt skabelon: ${requestScope.defaultWidth} cm</option>
                            </c:if>
                            <option value="240">240 cm</option>
                            <option value="270">270 cm</option>
                            <option value="300">300 cm</option>
                            <option value="330">330 cm</option>
                            <option value="360">360 cm</option>
                            <option value="390">390 cm</option>
                            <option value="420">420 cm</option>
                            <option value="450">450 cm</option>
                            <option value="480">480 cm</option>
                            <option value="510">510 cm</option>
                            <option value="540">540 cm</option>
                            <option value="570">570 cm</option>
                            <option value="600">600 cm</option>
                        </select>

                    <br><br>
                    <label for="length" class="fw-bold">Carports længde:</label>
                        <br>
                        <select name="length" id="length" class="inputBoxSize">
                            <c:if test="${requestScope.defaultSet != null}">
                                <option value="${requestScope.defaultLength}">Valgt skabelon: ${requestScope.defaultLength} cm</option>
                            </c:if>
                            <option value="240">240 cm</option>
                            <option value="270">270 cm</option>
                            <option value="300">300 cm</option>
                            <option value="330">330 cm</option>
                            <option value="360">360 cm</option>
                            <option value="390">390 cm</option>
                            <option value="420">420 cm</option>
                            <option value="450">450 cm</option>
                            <option value="480">480 cm</option>
                            <option value="510">510 cm</option>
                            <option value="540">540 cm</option>
                            <option value="570">570 cm</option>
                            <option value="600">600 cm</option>
                            <option value="630">630 cm</option>
                            <option value="660">660 cm</option>
                            <option value="690">690 cm</option>
                            <option value="720">720 cm</option>
                            <option value="750">750 cm</option>
                            <option value="780">780 cm</option>
                        </select>

                    <br><br>
                    <label for="withRoof" class="fw-bold">Tag:</label>
                        <br>
                        <select name="withRoof" id="withRoof" class="inputBoxSize">
                            <c:if test="${requestScope.defaultSet != null}">
                                <option value="${requestScope.defaultRoof}">Valgt skabelon: ${requestScope.defaultRoofString}</option>
                            </c:if>
                            <option value="false">Uden tagplader</option>
                            <option value="true">Trapeztag</option>
                        </select>

                    <br><br>
                    <label for="shedLength" class="fw-bold">Skur:</label>
                    <br>
                    <select name="shedLength" id="shedLength" class="inputBoxSize">
                        <option value="0">Intet skur</option>
                        <option value="100">100 cm længde. Halv carport bredde</option>
                        <option value="200">200 cm længde. Halv carport bredde</option>
                        <option value="300">300 cm længde. Halv carport bredde</option>
                    </select>

                    <br><br>
                    <label for="comment" class="fw-bold">Bemærkninger eller særlige ønsker:</label>
                    <br>
                    <input type="text" id="comment" name="comment" class="inputBoxSize">

                    <br><br>
                    <button class="btn btn-primary fw-bold submitBtnSize" type="submit">
                        Bestil
                    </button>
                </form>

                <form action="orderpage" method="post">

                    <input type="number" hidden name = "defaultSet" value="1">
                    <input type="number" hidden name = "defaultWidth" value="600">
                    <input type="number" hidden name = "defaultLength" value="720">
                    <input type="number" hidden name = "defaultSet" value="1">
                    <input type="submit" name="submit">

                </form>


            </div>
        </div>

        <div>
            <h4>Vigtig info</h4>
            <p>
                Bestilling af en carport er <b>ikke bindende,</b> kun et tilbud. <br>
                En salgsspecialist vil behandle din ordre efter bestilling, <br>
                og kan vende tilbage med spørgsmål, hvis behovet skulle opstå.
            </p>
        </div>

        <div class="mt-2">
            <form action="userpage" method="post">
                <input type="submit" class="btn btn-primary fw-bold" value="Tilbage">
            </form>
        </div>
    </jsp:body>

</t:pagetemplate>