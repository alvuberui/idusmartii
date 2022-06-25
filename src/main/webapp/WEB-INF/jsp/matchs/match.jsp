<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="match" tagdir="/WEB-INF/tags/match" %>

<petclinic:layout pageName="match">
<c:choose>
   <c:when test="${match.matchStatus == 'WAITTING'}">
            <h2>La partida esta comenzando partida en mesa ${match.board.id} </h2>
            <match:boardTable />
   </c:when>
   <c:when test="${match.matchStatus == 'PLAYING'}">
              <h2>Estas en la mesa ${match.board.id} </h2>
                <h3>Turn ${turn.turn + 1} Round ${round} </h3>
              <match:frasesTurn />
              <match:tablero />
    </c:when>
    <c:when test="${match.matchStatus == 'FINISHED'}">
            <h2>La partida ha finalizado</h2>
             <h2><c:out value="${match.winner}"/></h2>
                <div class="row">
                    <div class="col">
                            <spring:url value="/board/{boardId}" var="editUrl">
                            <spring:param name="boardId" value="${match.board.id}"/></spring:url>
                            <a  href="${fn:escapeXml(editUrl)}" class="btn btn-default">Volver a la sala</a>
                    </div>
                    <div  class="col">
                             <spring:url value="/play/left" var="editUrl"></spring:url>
                             <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Left</a>
                    </div>
                </div>
    </c:when>
   <c:otherwise>
                  <h2> Hay un error con esta partida</h2>
   </c:otherwise>
    </c:choose>
</petclinic:layout>
