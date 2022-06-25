<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="match" tagdir="/WEB-INF/tags/match" %>




<petclinic:layout pageName="prematch">
  <div class="row">
		<div class="col-md-8">
      <c:forEach var="player" items="${players}" varStatus="loopCount">
            <c:if test="${loopCount.count eq 1}">
              <c:set var="boardId" value="${player.board.id}" />
              <h2>Mesa de juego ${boardId}</h2>
            </c:if>
        </c:forEach>
            <match:boardTable />
              <c:if test="${watching}">
                        <h2>Se esta jugando el turno: <c:out value="${turn.turn + 1}" /></h2>
                        <match:actionMatch />
             </c:if>
            <c:choose>
                    <c:when test="${watching}">
                        <sec:authorize access="hasAuthority('player')">
                            <div style='display: flex; justify-content: space-between;'>
                             <spring:url value="/play" var="editUrl"></spring:url>
                            <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Left</a>
                            </div>
                        </sec:authorize>
                    </c:when>
                    <c:otherwise>
                        <sec:authorize access="hasAuthority('player')">
                            <div style='display: flex; justify-content: space-between;'>
                             <spring:url value="/play/left" var="editUrl"></spring:url>
                            <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Left</a>
                            </div>
                        </sec:authorize>
                    </c:otherwise>
            </c:choose>
      </div>
		<div class="col-md-4">
			<table id="friendsTable" class="table table-striped">
		        <thead>
		        <tr>
		            <th style="width: 150px;">Friend</th>
		            <th style="width: 150px;">Invitación a mesa</th>
		        </tr>
		        </thead>
		        <tbody>
		        <sec:authentication var="name" property="name" />
		        <c:forEach items="${friends}" var="friend">
		         <c:if test = "${name == friend.player1.user.username}">
		        <c:set var = "actualFriend1"  value = "${friend.player2.user.username}"/>
		        </c:if>
		        <c:if test = "${name == friend.player2.user.username}">
		        <c:set var = "actualFriend1"  value = "${friend.player1.user.username}"/>
		        </c:if>
		            <tr>
		                <td>
							<c:out value="${actualFriend1}"/>
		                </td>
		                
		                
		                <td>
		                    <spring:url value="/friendList/invite/{friendId}" var="inviteUrl">
		                    <spring:param name="friendId" value="${friend.id}"/></spring:url>
		                    <a href="${fn:escapeXml(inviteUrl)}" class="btn btn-default">Invitar</a>
		                </td>
		              
		            </tr>
		        </c:forEach>
		        </tbody>
		    </table>
		</div>
	</div>
</petclinic:layout>
