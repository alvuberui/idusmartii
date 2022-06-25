<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="edil" required="true" type="org.springframework.idusmartii.edil.Edil" %>


<div class="container">
        <div>
            <spring:url value="/match/{matchId}/action/edil/{edilId}/vote/1" var="editUrl">
            <spring:param name="matchId" value="${match.id}"/>
             <spring:param name="edilId" value="${edil.id}"/></spring:url>
            <a  href="${fn:escapeXml(editUrl)}" class="btn btn-default">Voto leal</a>
        </div>
        <div>
            <spring:url value="/match/{matchId}/action/edil/{edilId}/vote/2" var="editUrl">
               <spring:param name="matchId" value="${match.id}"/>
               <spring:param name="edilId" value="${edil.id}"/></spring:url>
            <a  href="${fn:escapeXml(editUrl)}" class="btn btn-default">Voto traidor</a>
        </div>
        <c:if test="${turn.turn >= match.numPlayers}">
            <div>
              <spring:url value="/match/{matchId}/action/edil/{edilId}/vote/3" var="editUrl">
                 <spring:param name="matchId" value="${match.id}"/>
                <spring:param name="edilId" value="${edil.id}"/></spring:url>
               <a  href="${fn:escapeXml(editUrl)}" class="btn btn-default">Voto neutro</a>
            </div>
        </c:if>
</div>
