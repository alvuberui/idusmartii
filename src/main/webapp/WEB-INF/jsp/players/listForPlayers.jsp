<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="players">
    <h2>Players</h2>

    <table id="playersTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">User name</th>         
            <th style="width: 150px;">Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${players}" var="player">
            <tr>
                <td>
                    <spring:url value="/playerList/details/{playerUsername}" var="playerUrl">
                        <spring:param name="playerUsername" value="${player.user.username}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(playerUrl)}"><c:out value="${player.user.username}"/></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <sec:authorize access="hasAuthority('admin')">
     <a href="/playerList/new" class="btn btn-default">Create new Player</a>
    </sec:authorize>
</petclinic:layout>