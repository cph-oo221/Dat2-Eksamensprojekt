<%@tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>

<!DOCTYPE html>
<html lang="da">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><jsp:invoke fragment="header"/></title>
    <link rel="icon" href="<%=request.getContextPath()%>/images/Pagetemplate/FogLogo.png">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
    integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
<header>
    <nav class="navbar navbar-expand-lg navbar-light backgroundColorFog">
        <div class="container">
            <c:if test="${sessionScope.user == null }">
                <a class="navbar-brand" href="index.jsp">
                    <img src="${pageContext.request.contextPath}/images/Pagetemplate/FogLogo.png" width="80px;" class="img-fluid"/>
                </a>
            </c:if>

            <c:if test="${sessionScope.user.role.equals('admin')}">
                <form action="redirectadminpanel" method="post">
                    <input type="image" class="navbar-brand" src="${pageContext.request.contextPath}/images/Pagetemplate/FogLogo.png"
                           width="80px;" class="img-fluid" alt="Submit"/>
                </form>
            </c:if>

            <c:if test="${sessionScope.user.role.equals('user')}">
                <form action="userpage" method="post">
                    <input type="image" class="navbar-brand" src="${pageContext.request.contextPath}/images/Pagetemplate/FogLogo.png"
                           width="80px;" class="img-fluid" alt="Submit"/>
                </form>
            </c:if>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavAltMarkup"
                    aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-end" id="navbarNavAltMarkup">
                <div class="navbar-nav">

                    <c:if test="${sessionScope.user.role.equals('user')}">
                        <form action="redirectadminpanel" method="post">
                            <input type="submit" class="nav-item nav-link btn btn-link" value="Bruger Side"/>
                        </form>
                    </c:if>

                    <c:if test="${sessionScope.user.role.equals('admin') }">
                        <form action="redirectadminpanel" method="post">
                            <input type="submit" class="nav-item nav-link btn btn-link" value="Admin Panel"/>
                        </form>
                    </c:if>

                    <c:if test="${sessionScope.user == null }">
                        <a class="nav-item nav-link" href="${pageContext.request.contextPath}/index.jsp">Login</a>
                    </c:if>
                    <c:if test="${sessionScope.user != null }">
                        <a class="nav-item nav-link" href="${pageContext.request.contextPath}/logout">Log out</a>
                    </c:if>
                </div>
            </div>
        </div>
    </nav>
</header>

<div id="body" class="container mt-4" style="min-height: 400px;">
    <h1><jsp:invoke fragment="header"/></h1>
    <jsp:doBody/>
</div>

<!-- Footer -->
<div class="container mt-3">
    <hr/>
    <div class="row mt-4">
        <div class="col text-center">
            Firskovvej 20<br/>
            2800 Lyngby
        </div>
        <div class="col text-center">
            <jsp:invoke fragment="footer"/><br/>
            <p>&copy; 2023 Johannes Fog A/S</p>
        </div>
        <div class="col text-center">
            Kontakt information:<br/>
            fog@gmail.com
        </div>
    </div>
</div>

<!-- Bootstrap Bundle with Popper -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
</body>
</html>