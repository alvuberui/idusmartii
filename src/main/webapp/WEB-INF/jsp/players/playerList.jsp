<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="players">
    <h2>Players</h2>
	<sec:authorize access="hasAuthority('admin')">
     <a href="/admin/playersList/createPlayer" class="btn btn-default">Crear nuevo jugador</a>
    </sec:authorize>
    <br></br>
    <table id="playersTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Usuario</th>
            <th style="width: 150px;">Nombre</th>
            <th style="width: 150px;">Apellido</th>
            <th style="width: 150px;">email</th>
            <th style="width: 150px;">Contraseña</th>
            <sec:authorize access="hasAuthority('admin')">
            <th style="width: 150px;">Acciones</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${data}" var="player">
            <tr>
                <td>
                    <c:out value="${player.user.username}"/>
                </td>
                <td>
					<c:out value="${player.firstName}"/>
                </td>
                <td>
                    <c:out value="${player.lastName}"/>
                </td>
                <td>
                    <c:out value="${player.email}"/>
                </td>
                <td>
                    <c:out value="${player.user.password}"/>
                </td>
                <sec:authorize access="hasAuthority('admin')">
      			<td>
      				<spring:url value="/admin/playerList/delete/{playerId}" var="deleteplayerUrl">
                    <spring:param name="playerId" value="${player.id}"/>
                    </spring:url>
      				<a href="${fn:escapeXml(deleteplayerUrl)}">Eliminar</a>
      				
      				<spring:url value="/admin/playersList/update/{playerId}" var="updateplayerUrl">
                    <spring:param name="playerId" value="${player.id}"/>
                    </spring:url>
      				<a href="${fn:escapeXml(updateplayerUrl)}">Actualizar</a>
      			</td>
      			</sec:authorize>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <c:if test="${data.size() > 0 }">
        <div class="panel-footer">
            Mostrando ${number+1} of ${size+1}
            <ul class="pagination">
                <c:forEach begin="0" end="${totalPages-1}" var="page">
                    <li class="page-item">
                        <a href="/admin/playersList?page=${page}&size=${size}" class="page-link">${page+1}</a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </c:if>
</petclinic:layout>