<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="forum">
    <h2>Foros de partidas</h2>

    <table id="forumsTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Id partida</th>
            <th style="width: 150px;">Estado partida</th>
            <th style="width: 150px;">Jugadores</th>
            <th style="width: 150px;">Votos Leal</th>
            <th style="width: 150px;">Votos Traidor</th>
            <th style="width: 150px;">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${matchs}" var="match">
            <tr>
                <td>
                    <c:out value="${match.id}"/>
                </td>
                <td>
                    <c:out value="${match.matchStatus}"/>
                </td>
                <td>
                	<c:forEach items="${match.playerList}" var="player">
                    	<c:out value="${player.user.username}"/>,
                    </c:forEach>
                </td>
                <td>
                    <c:out value="${match.votosLeal}"/>
                </td>
                <td>
                    <c:out value="${match.votosTraidor}"/>
                </td>
                 <td>
                    <spring:url value="/forum/{forumId}" var="forumUrl">
                    <spring:param name="forumId" value="${match.id}"/>
                    </spring:url>
      				<a href="${fn:escapeXml(forumUrl)}">Entrar</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>