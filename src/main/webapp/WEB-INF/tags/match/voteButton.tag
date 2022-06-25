<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="edil" required="true" type="org.springframework.idusmartii.edil.Edil" %>



 <div style="display: flex;width: 25%;justify-content: space-evenly;padding: 1rem;">
                          <c:choose>
                               <c:when test="${(turn.matchTurnStatus == 'REVOTE' && edil.voteChange != null) || (turn.matchTurnStatus == 'WAITTOVOTE' && edil.vote != null)}">
                                         <c:choose>
                                                <c:when test="${edil.vote == 'LOYAL'}">
                                                    <img style="padding: 0; width: 50px" src="<spring:url value="/resources/images/voteGood.png" htmlEscape="true" />"></img>
                                                </c:when>
                                                <c:when test="${edil.vote == 'TRAITOR'}">
                                                     <img style="padding: 0; width: 50px" src="<spring:url value="/resources/images/voteBad.png" htmlEscape="true" />"></img>
                                                </c:when>
                                                <c:when test="${edil.vote == 'NEUTRAL'}">
                                                <img style="padding: 0; width: 50px" src="<spring:url value="/resources/images/voteNeutro.png" htmlEscape="true" />"></img>
                                                </c:when>
                                         </c:choose>
                               </c:when>
                                <c:otherwise>
                                    <div class="btn" style="padding: 0; width: 50px;">
                                             <spring:url value="/match/{matchId}/action/edil/vote/1" var="editUrl">
                                             <spring:param name="matchId" value="${match.id}"/>
                                             </spring:url>
                                            <a  href="${fn:escapeXml(editUrl)}">  <img style="width: 100%" src="<spring:url value="/resources/images/voteGood.png" htmlEscape="true" />"></img></a>
                                    </div>
                                    <div class="btn" style="padding: 0; width: 50px">
                                             <spring:url value="/match/{matchId}/action/edil/vote/2" var="editUrl">
                                             <spring:param name="matchId" value="${match.id}"/>
                                             </spring:url>
                                             <a  href="${fn:escapeXml(editUrl)}"><img style="width: 100%" src="<spring:url value="/resources/images/voteBad.png" htmlEscape="true" />"></img></a>
                                    </div>
                                        <c:if test="${round == 2 && turn.matchTurnStatus != 'REVOTE'}">
                                            <div class="btn" style="padding: 0; width: 50px">
                                                    <spring:url value="/match/{matchId}/action/edil/vote/3" var="editUrl">
                                                    <spring:param name="matchId" value="${match.id}"/></spring:url>
                                                <a  href="${fn:escapeXml(editUrl)}">  <img style="width: 100%" src="<spring:url value="/resources/images/voteNeutro.png" htmlEscape="true" />"></img></a>
                                            </div>
                                        </c:if>
                                   </c:otherwise>
                          </c:choose>

 </div>

