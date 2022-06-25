<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="container " style="width: 25%;right: 0; top: 0; position: absolute;">
			<table class="table">
                   <c:if test="${turn != null}">
			          <c:if test="${turn.consul != null}">
				        	<tr>
					    	    <th>Consul</th>
					    	    <td><c:out value="${turn.consul.player.user.username}" /></td>
					         </tr>
					    </c:if>
				    <c:if test="${turn.pretor != null}">
					        <tr>
                    		    <th>Pretor</th>
                    		    <td><c:out value="${turn.pretor.player.user.username}" /></td>
                            </tr>
                   </c:if>
					<c:forEach var="edil" items="${turn.playerEdil}">
						<tr>
							<th>Edil</th>
							<td><c:out value="${edil.player.user.username}" /></td>
						</tr>
					</c:forEach>
					 </c:if>
					<c:forEach var="player" items="${playerWithNotRoll}">
						<tr>
                            <c:choose>
                                 <c:when test="${turn.matchTurnStatus == 'CHOOSE_ROL' && turn.consul.player.id == playerCredentials.id && rolTypeValor!=3}">
                                        <th><c:out value="${player.user.username}"/></th>
                                        <td>
                                              <spring:url value="/match/{matchId}/action/consul/chooseRol/{playerId}/{rolType}" var="editUrl">
                                              <spring:param name="matchId" value="${match.id}"/>
                                               <spring:param name="rolType" value="${rolTypeValor}"/>
                                              <spring:param name="playerId" value="${player.id}"/></spring:url>
                                              <a  href="${fn:escapeXml(editUrl)}" class="btn btn-default">Elegir</a>
                                         </td>
                                 </c:when>
                                 <c:otherwise>
                                            <th>Player</th>
                                            <td><c:out value="${player.user.username}"/></td>
                                 </c:otherwise>
                             </c:choose>
                        </tr>
                    </c:forEach>
			</table>
                <c:forEach var="edil" items="${turn.playerEdil}">
                    	           <c:if test="${edil.player.id == playerCredentials.id && turn.matchTurnStatus == 'WAITTOVOTE'}">
                    	                 <match:edilVote edil="${edil}" />
                                   </c:if>
                                    <c:if test="${turn.pretor.player.id == playerCredentials.id && turn.matchTurnStatus == 'CHANGING_VOTE'}">
                                   	          <match:pretorChange edil="${edil}" />
                                     </c:if>
                                     <c:if test="${edil.player.id == playerCredentials.id && edilChanged != null && edilChanged.player.id == playerCredentials.id && turn.matchTurnStatus == 'REVOTE'}">
                                              <div>Voto para revotar</div>
                                     </c:if>
                    </c:forEach>
</div>
