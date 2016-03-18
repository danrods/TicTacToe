<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp">
    <jsp:param name="title" value="Let's Play"/>
    <jsp:param name="js" value="/js/play.js"/>
</jsp:include>

<div class="alert alert-danger alert-dismissible" role="alert">
    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <strong>Error!</strong> <span id="errMsg"></span>
</div>

<c:if test="${isAIStart}">
    <script>
        findAIMove();
    </script>
</c:if>
<div id="row">
<div id="playerBox" class="pull-left player gamePanel col-md-2">
    <div id="playerpop" class="pull-left" data-toggle="popover" data-placement="right" title="Your Turn!" data-content="...">
        <p class="panelText">Player : ${user.email}</p>
    </div>
    <div class="pull-right gamePanelImg">
        <img src="${playerImg}"/>
    </div>
</div>

<div id="gameArea" class=" gameScreen col-md-6">
    <div id="board" class="gameBoard  ">
        <c:set var="j" value="1"/>
        <c:forEach var="i" begin="1" end="3">
            <div id="row${i}" class="boardRow row">
                <div class="playStage">
            <c:forEach var="block" begin="1" end="3">
                <div class="cellBlock col-md-4 ">
                    <img id="${j}" src="/img/Solid_white.png">
                </div>
                <c:set var="j" value="${j + 1}"/>
            </c:forEach>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<div id="computerBox" class="pull-right ai gamePanel col-md-2" >
    <div id="popover" class="pull-left" data-toggle="popover" data-placement="left" title="Thinking..." data-content="....">
        <p class="panelText">Computer : Bob</p>
    </div>
    <div class="gamePanelImg pull-right">
        <img src="${computerImg}"/>
    </div>
</div>

</div>

<div id="winModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 id="modalTitle"></h3>
            </div>
            <div class="modal-body">
                <h3 id="modalText"></h3>
            </div>
            <div id="modalFooter" class="modal-footer">
                <form method="post" action="/play">
                    <button id="replay" type="submit" class="btn btn-primary">Play Again</button>
                </form>
                <form method="get" action="/welcome">
                    <button type="submit" class="btn btn-default">Close</button>
                </form>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<jsp:include page="footer.jsp"/>
