<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="container">
        <c:choose>
            <c:when test="${turn.pretor.edil == null}">
            <c:forEach var="edil" items="${turn.playerEdil}">
              <div>
                    <spring:url value="/match/{matchId}/action/pretor/changeVote/{edilId}" var="editUrl">
                         <spring:param name="matchId" value="${match.id}"/>
                         <spring:param name="edilId" value="${edil.id}"/>
                    </spring:url>
                    <a  href="${fn:escapeXml(editUrl)}" class="btn btn-default">Cambiar voto de ${edil.player.user.username}</a>
              </div>
            </c:forEach>
             </c:when>
            <c:when test="${turn.pretor.edil != null}">
               <c:choose>
                 <c:when test="${round == 1}">
                    <div>
                        <div>El voto de ${edil.player.user.username} es :${turn.pretor.edil.vote}</div>
                        <spring:url value="/match/{matchId}/action/pretor/changeVote/{edilId}" var="editUrl">
                         <spring:param name="matchId" value="${match.id}"/>
                         <spring:param name="edilId" value="${turn.pretor.edil.id}"/>
                        </spring:url>
                    <a  href="${fn:escapeXml(editUrl)}" class="btn btn-default">¿Quieres cambiarlo?</a>
                  </div>
                 </c:when>
                 <c:when test="${round == 2 && turn.pretor.edil.vote != 'NEUTRAL'}">
                     <div>El voto de ${edil.player.user.username} no es neutro</div>
                 </c:when>
               </c:choose>
            </c:when>
        </c:choose>

</div>
