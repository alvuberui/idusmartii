<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>


<table class="table table-striped">
      	<sec:authentication var="user" property="principal" />
		<c:set var="contador" value="${1}" />
		<c:forEach items="${players}" var="player">
			<tr>
				<th>Jugador ${contador}</th>
				<td><b><c:out value="${player.user.username}" /></b></td>
				<c:set var="contador" value="${contador + 1}" />
				     <c:choose>
                     <c:when test="${player.user.username == board.anfitrion.user.username}">
                                <c:choose>
                                    <c:when test="${user.username eq board.anfitrion.user.username}">
                                           <th>
                                                    <spring:url value="/match/new/{boardId}" var="editUrl">
                                                     <spring:param name="boardId" value="${board.id}"/></spring:url>
                                                    <a  href="${fn:escapeXml(editUrl)}" class="btn btn-default">Empezar partida</a>
                                             </th>
                                     </c:when>
                                      <c:otherwise>
                                              <th>Anfitrion</th>
                                         </c:otherwise>
                                     </c:choose>
                     </c:when>
                       <c:otherwise>
                             <th></th>
                       </c:otherwise>
                      </c:choose>
			</tr>
		</c:forEach>
	</table>
