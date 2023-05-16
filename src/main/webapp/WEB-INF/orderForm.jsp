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
        <div class="container mt-4">
            <div class="card mt-2 p-1" style="position: center; margin-left: 25%; margin-right: 25%;">
                <form action="makeorder" method="post">
                    <label for="width">Carport bredde:
                        <br>
                        <select name="width" id="width">
                            <option value="240">240 cm</option>
                            <option value="270">270 cm</option>
                            <option value="300">300 cm</option>
                            <option value="330">330 cm</option>
                            <option value="360">360 cm</option>
                            <option value="390">360 cm</option>
                            <option value="420">420 (blaze it) cm</option>
                            <option value="450">450 cm</option>
                            <option value="480">480 cm</option>
                            <option value="510">510 cm</option>
                            <option value="540">540 cm</option>
                            <option value="570">570 cm</option>
                            <option value="600">600 cm</option>
                        </select>
                    </label>
                    <br><br>
                    <label for="length">Carport længde:
                        <br>
                        <select name="length" id="length">
                            <option value="240">240 cm</option>
                            <option value="270">270 cm</option>
                            <option value="300">300 cm</option>
                            <option value="330">330 cm</option>
                            <option value="360">360 cm</option>
                            <option value="390">360 cm</option>
                            <option value="420">420 (blaze it) cm</option>
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
                    </label>
                    <br><br>
                    <label for="withRoof">Carport tag:
                        <br>
                        <select name="withRoof" id="withRoof">
                            <option value="false">Uden tagplader</option>
                            <option value="true">Trapeztag</option>
                        </select>
                    </label>
                    <label for="shedLength">Skur:
                        <br>
                        <select name="shedLength" id="shedLength">
                            <option value="0">Intet skur</option>
                            <option value="100">100 cm</option>
                            <option value="200">200 cm</option>
                            <option value="300">300 cm</option>
                        </select>
                    </label>
                    <br><br>
                    <label for="comment">Bemærkninger eller særlige ønsker:
                        <br>
                        <input type="text" id="comment" name="comment">
                    </label>
                    <br><br>
                    <button class="btn btn-primary" type="submit">
                        Bestil
                    </button>
                </form>


            </div>
        </div>
    </jsp:body>

</t:pagetemplate>