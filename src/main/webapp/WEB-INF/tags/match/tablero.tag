<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="match" tagdir="/WEB-INF/tags/match" %>


<div class="container tablerobg img-responsive"  style="position: relative;">
        <div style="height: 600px;">
                <match:marcador/>
        <div class="container" style="height: 60%;">
                <match:jugadores />
        </div>
        <div style="height: 400px;">
                    <c:forEach var="edil" items="${turn.playerEdil}">
                        <c:if test="${edil.player.id == playerCredentials.id}">
                                <c:if test="${turn.matchTurnStatus == 'WAITTOVOTE' || (turn.matchTurnStatus == 'REVOTE' && turn.pretor.edil.id == edil.id)}">
                                                <match:voteButton edil="${edil}"/>
                                </c:if>
                        </c:if>
         		    </c:forEach>
         		     <c:if test="${turn.matchTurnStatus == 'CHANGING_VOTE' && turn.pretor.player.id == playerCredentials.id}">
                            <match:pretorChange />
                     </c:if>
         </div>
         <c:choose>
             <c:when test="${factionSelected != null}">
                    <div class="card" style="right: 40%;  top: 45%;">
                               <match:factionCard faction="${factionSelected}"/>
                    </div>
            </c:when>
            <c:otherwise>
              <div  class="card" style="right: 25%;  top: 45%;">
                     <match:factionCard faction="${factionCard1}"/>
               </div>
               <div class="card" style="right: 55%;  top: 45%;">
                      <match:factionCard faction="${factionCard2}"/>
               </div>
            </c:otherwise>
            </c:choose>
        </div>
</div>
