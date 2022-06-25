<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="faction" required="true" type="org.springframework.idusmartii.faction.FactionCard" %>


<div>
            <c:choose>
                <c:when test="${faction.cardType == 'LOYAL'}">
                       <c:choose>
                           <c:when test="${turn.matchTurnStatus == 'CHOOSE_FACCTION' && turn.consul.player.id == playerCredentials.id && factionSelected == null}">
                                       <spring:url value="/match/{matchId}/action/consul/chooseFaction/1" var="editUrl">
                                         <spring:param name="matchId" value="${match.id}"/>
                                       </spring:url>
                                <a  href="${fn:escapeXml(editUrl)}"  style="width: 105%; position:relative;">
                                    <img src="<spring:url value="/resources/images/cesar.png" htmlEscape="true" />"/>
                                </a>
                           </c:when>
                           <c:otherwise>
                              <img style="width: 100%; position:relative;" src="<spring:url value="/resources/images/cesar.png" htmlEscape="true" />"/>
                           </c:otherwise>
                       </c:choose>
                </c:when>
                <c:when test="${faction.cardType == 'TRAITOR'}">
                       <c:choose>
                           <c:when test="${turn.matchTurnStatus == 'CHOOSE_FACCTION' && turn.consul.player.id == playerCredentials.id && factionSelected == null}">
                                       <spring:url value="/match/{matchId}/action/consul/chooseFaction/2" var="editUrl">
                                         <spring:param name="matchId" value="${match.id}"/>
                                       </spring:url>
                                <a  href="${fn:escapeXml(editUrl)}"  style="width: 105%; position:relative;">
                                    <img src="<spring:url value="/resources/images/nocesar.png" htmlEscape="true" />"/>
                                </a>
                           </c:when>
                           <c:otherwise>
                              <img style="width: 100%; position:relative;" src="<spring:url value="/resources/images/nocesar.png" htmlEscape="true" />"/>
                           </c:otherwise>
                       </c:choose>
                </c:when>
                <c:when test="${faction.cardType == 'MERCHANT'}">
                       <c:choose>
                           <c:when test="${turn.matchTurnStatus == 'CHOOSE_FACCTION' && turn.consul.player.id == playerCredentials.id && factionSelected == null}">
                                       <spring:url value="/match/{matchId}/action/consul/chooseFaction/3" var="editUrl">
                                         <spring:param name="matchId" value="${match.id}"/>
                                       </spring:url>
                                <a  href="${fn:escapeXml(editUrl)}"  style="width: 105%; position:relative;">
                                    <img src="<spring:url value="/resources/images/mercader.png" htmlEscape="true" />"/>
                                </a>
                           </c:when>
                           <c:otherwise>
                              <img style="width: 100%; position:relative;" src="<spring:url value="/resources/images/mercader.png" htmlEscape="true" />"/>
                           </c:otherwise>
                       </c:choose>
                </c:when>
            </c:choose>
</div>
