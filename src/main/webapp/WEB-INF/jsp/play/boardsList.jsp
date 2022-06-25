<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="boards">
    <h2>Active Boards</h2>

    <table id="BoardsTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Board ID</th>
            <th style="width: 150px;"></th>
            <th style="width: 150px;">Players</th>
            <th style="width: 150px;"></th>
            <th style="width: 150px;">Status</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${boards}" var="board">
            <tr>
                <td>
                    <a href="/board/${board.id}"><c:out value="${board.id}"/></a>
                </td>
                <td>
                    <th><c:out value="${board.numPlayers}"/></th>
                </td>
                <td>
                <c:choose>
                  <c:when test="${board.playingMatch != null}">
                        <th>
                         <sec:authorize access="hasAuthority('player')">
                               <spring:url value="board/watch/{boardId}" var="editUrl">
                               <spring:param name="boardId" value="${board.id}"/></spring:url>
                               <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Playing: Watch</a>
                           </sec:authorize>
                         </th>
                  </c:when>
                  <c:otherwise>
                    <th>
                    <sec:authorize access="hasAuthority('player')">
                    	            <spring:url value="play/join/{boardId}" var="editUrl">
                                    <spring:param name="boardId" value="${board.id}"/></spring:url>
                                    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Join</a>
                     </sec:authorize>
                    </th>

                   </c:otherwise>
                 </c:choose>

                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a href="/play/new" type="button" class="btn btn-default">New Board</a>
</petclinic:layout>

