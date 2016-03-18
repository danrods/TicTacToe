<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%> --%>
<%@ page isELIgnored="false" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Tic-Tac-Toe"/>
    <jsp:param name="js" value="/js/welcome.js"/>
</jsp:include>



<div class="leaderboard centerStage">
    <h1>Leaderboard</h1>
    <div class="well">
        <ul class="list-group">
            <c:forEach var="player" varStatus="i" end="10" items="${players}">
                <li class="list-group-item">
                    <span class="badge">
                        <c:out value="${player.totalWins}"></c:out>
                    </span>
                    <c:out value="${player.userId}"></c:out>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>

<form method="post" action="/play">
    <div class="gameBtn centerStage">
        <button id="newGame" type="submit" class="btn btn-primary btn-lg">Let's Play!  <span class="glyphicon glyphicon-th">&nbsp;</span></button>
    </div>
</form>


<jsp:include page="footer.jsp"/>