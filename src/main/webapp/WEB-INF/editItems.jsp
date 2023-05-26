<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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

        <div class="text-center">
            <h3 class="colorGreen">
                    ${requestScope.product.name}
            </h3>

            <h3 style="color: red">
                    ${requestScope.msgError}
            </h3>
        </div>

        <div class="container mt-3">
            <div class="row">
                <div class="col text-center">
                    <div class="dropdown">
                        <button class="btn btn-primary dropdown-toggle" type="button"
                                id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false"> Sorter
                        </button>

                        <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                            <li>
                                <form action="adminaction" method="post">
                                    <input type="number" hidden name="action" value="1">
                                    <input type="number" hidden name="sortOption" value="1">
                                    <input type="submit" class="dropdown-item" value="ID">
                                </form>
                            </li>

                            <li>
                                <form action="adminaction" method="post">
                                    <input type="number" hidden name="action" value="1">
                                    <input type="number" hidden name="sortOption" value="2">
                                    <input type="submit" class="dropdown-item" value="Navn">
                                </form>
                            </li>

                            <li>
                                <form action="adminaction" method="post">
                                    <input type="number" hidden name="action" value="1">
                                    <input type="number" hidden name="sortOption" value="3">
                                    <input type="submit" class="dropdown-item" value="Variant">
                                </form>
                            </li>
                        </ul>
                    </div>
                </div>


                <div class="col text-center">
                    <form action="adminaction" method="post">

                        <input type="number" hidden name="action" value="2">
                        <input type="text" id="search" name="search" class="inputBoxSizeHeight" placeholder="Search..."/>
                        <input type="submit" class="btn btn-primary fw-bold mb-1" value="Søg"/>
                    </form>
                </div>


                <div class="col text-center">
                    <form action="adminaction" method="post">

                        <input type="number" hidden name="action" value="3">
                        <input type="submit" class="btn btn-primary fw-bold mb-1" value="Nulstil"/>
                    </form>
                </div>
            </div>
        </div>

        <div class="container mt-4">

            <div class="mb-2">
                <h3>
                    Træ:
                </h3>
            </div>

            <div class="row">
                <div class="col-sm-12">
                    <table class="table table-dark table-striped">
                        <thead>
                        <tr>
                            <th scope="col">Træ ID</th>
                            <th scope="col">Navn</th>
                            <th scope="col">Længde</th>
                            <th scope="col">Bredde</th>
                            <th scope="col">Højde</th>
                            <th scope="col">Enhed</th>
                            <th scope="col">Pris</th>
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
                                <td>${wood.width}</td>
                                <td>${wood.height}</td>
                                <td>${wood.unit}</td>
                                <td>${wood.price} kr.</td>
                                <td>${wood.variant}</td>
                                <td class="text-center">
                                    <form action="adminaction" method="post"
                                          onsubmit="return confirm('Denne handling medfører, at træet: '
                                                  + 'ID:' + ${wood.idWood} + ', Name: ' + ${wood.name} + '. bliver slettet, er du sikker?')">

                                        <input type="number" hidden name="action" value="4">
                                        <input type="number" hidden name="deleteOption" value="1">
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


        <div class="container mt-4">

            <div class="mb-2">
                <h3>
                    Metal:
                </h3>
            </div>

            <div class="row">
                <div class="col-sm-12">
                    <table class="table table-dark table-striped">
                        <thead>
                        <tr>
                            <th scope="col">Metal ID</th>
                            <th scope="col">Navn</th>
                            <th scope="col">Enhed</th>
                            <th scope="col">Pris</th>
                            <th scope="col">Variant</th>
                            <th scope="col">Handling</th>
                        </tr>
                        </thead>
                        <c:forEach var="metal" items="${requestScope.metalList}">
                            <tbody>
                            <tr>
                                <td>${metal.idMetal}</td>
                                <td>${metal.name}</td>
                                <td>${metal.unit}</td>
                                <td>${metal.price} kr.</td>
                                <td>${metal.variant}</td>
                                <td class="text-center">
                                    <form action="adminaction" method="post"
                                          onsubmit="return confirm('Denne handling medføre, at metallet: '
                                                  + 'ID:' + ${metal.idMetal} + ', Name: ' + ${metal.name} + '. bliver slettet, er du sikker?')">

                                        <input type="number" hidden name="action" value="4">
                                        <input type="number" hidden name="deleteOption" value="2">
                                        <input type="number" hidden name="idMetal" value="${metal.idMetal}">
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


        <div class="container mt-3">
            <div class="row">
                <div class="col">
                    <div class="card mt-2 p-1">
                        <h1 class="text-center">Tilføj nyt træ</h1>
                        <hr>
                        <div class="mt-1">
                            <form action="adminaction" method="post">

                                <label for="name" class="fw-bold"> Navn </label><br/>
                                <input type="text" id="name" name="name" class="inputBoxSize"
                                       placeholder="Angiv Navn"/>


                                <br/>
                                <br/>
                                <label for="length" class="fw-bold"> Længde </label><br/>
                                <input type="number" id="length" name="length" class="inputBoxSize"
                                       placeholder="Angiv Længde"/>

                                <br/>
                                <br/>
                                <label for="width" class="fw-bold"> Bredde </label><br/>
                                <input type="number" id="width" name="width" class="inputBoxSize"
                                       placeholder="Angiv Bredde"/>

                                <br/>
                                <br/>
                                <label for="height" class="fw-bold"> Højde </label><br/>
                                <input type="number" id="height" name="height" class="inputBoxSize"
                                       placeholder="Angiv Højde"/>


                                <br/>
                                <br/>
                                <label for="unit" class="fw-bold"> Enhed </label><br/>
                                <input type="text" id="unit" name="unit" class="inputBoxSize"
                                       placeholder="Angiv Enhed"/>


                                <br/>
                                <br/>
                                <label for="variant" class="fw-bold"> Variant </label><br/>
                                <input type="text" id="variant" name="variant" class="inputBoxSize"
                                       placeholder="Angiv Variant"/>


                                <br/>
                                <br/>
                                <label for="price" class="fw-bold"> Pris </label><br/>
                                <input type="number" id="price" name="price" class="inputBoxSize"
                                       placeholder="Angiv Pris"/>

                                <br/>
                                <br/>
                                <input type="number" hidden name="action" value="5">
                                <input type="submit" class="btn btn-primary fw-bold submitBtnSize"
                                       value="Tilføj"/>
                                <br>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="col">
                    <div class="card mt-2 p-1">
                        <h1 class="text-center">Ændr pris på træ</h1>
                        <hr>
                        <div class="mt-1">
                            <form action="adminaction" method="post">

                                <label for="idWood" class="fw-bold"> Træ ID </label><br/>
                                <input type="number" id="idWood" name="idWood" class="inputBoxSize"
                                       placeholder="Angiv Træ ID"/>

                                <br/>
                                <br/>
                                <label for="newPrice" class="fw-bold"> Ny pris </label><br/>
                                <input type="number" id="newPrice" name="newPrice" class="inputBoxSize"
                                       placeholder="Angiv ny pris"/>

                                <br/>
                                <br/>
                                <input type="number" hidden name="action" value="6">
                                <input type="submit" class="btn btn-primary fw-bold submitBtnSize"
                                       value="Ændr pris"/>
                                <br>
                            </form>
                        </div>
                    </div>

                    <br/>
                    <br/>
                    <br/>
                    <br/>

                    <div class="card mt-2 p-1">
                        <h1 class="text-center">Ændr pris på metal</h1>
                        <hr>
                        <div class="mt-1">
                            <form action="adminaction" method="post">

                                <label for="idMetal" class="fw-bold"> Metal ID </label><br/>
                                <input type="number" id="idMetal" name="idMetal" class="inputBoxSize"
                                       placeholder="Angiv Metal ID"/>

                                <br/>
                                <br/>
                                <label for="newPriceMetal" class="fw-bold"> Ny pris </label><br/>
                                <input type="number" id="newPriceMetal" name="newPriceMetal"
                                       class="inputBoxSize"
                                       placeholder="Angiv ny pris"/>

                                <br/>
                                <br/>
                                <input type="number" hidden name="action" value="7">
                                <input type="submit" class="btn btn-primary fw-bold submitBtnSize"
                                       value="Ændr pris"/>
                                <br>
                            </form>
                        </div>
                    </div>
                </div>


                <div class="col">
                    <div class="card mt-2 p-1">
                        <h1 class="text-center">Tilføj nyt metal</h1>
                        <hr>
                        <div class="mt-1">
                            <form action="adminaction" method="post">

                                <label for="metalName" class="fw-bold"> Navn </label><br/>
                                <input type="text" id="metalName" name="metalName" class="inputBoxSize"
                                       placeholder="Angiv Navn"/>

                                <br/>
                                <br/>
                                <label for="metalPrice" class="fw-bold"> Pris </label><br/>
                                <input type="number" id="metalPrice" name="metalPrice"
                                       class="inputBoxSize"
                                       placeholder="Angiv Pris"/>

                                <br/>
                                <br/>
                                <label for="metalUnit" class="fw-bold"> Enhed </label><br/>
                                <input type="text" id="metalUnit" name="metalUnit" class="inputBoxSize"
                                       placeholder="Angiv Enhed"/>

                                <br/>
                                <br/>
                                <label for="metalVariant" class="fw-bold"> Variant </label><br/>
                                <input type="text" id="metalVariant" name="metalVariant"
                                       class="inputBoxSize"
                                       placeholder="Angiv Variant"/>


                                <br/>
                                <br/>
                                <input type="number" hidden name="action" value="8">
                                <input type="submit" class="btn btn-primary fw-bold submitBtnSize"
                                       value="Tilføj"/>
                                <br>
                            </form>
                        </div>
                    </div>
                </div>

            </div>
        </div>

        <div class="mt-4">
            <form action="redirectadminpanel" method="post">
                <input type="submit" class="btn btn-primary fw-bold" value="Tilbage">
            </form>
        </div>

    </jsp:body>
</t:pagetemplate>