<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="matchs">
    <h1>Administrar partidas</h1>
	
	<h2>Partidas actualmente activas</h2>
    <table id="matchsTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 150px;">Id</th>
            <th style="width: 150px;">Número de jugadores</th>
            <th style="width: 150px">Estado</th>
            <th style="width: 150px">Id mesa</th>
            <th style="width: 150px">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${matchs}" var="match">
        	<c:if test="${match.matchStatus != 'FINISHED'}">
	            <tr>
	            	<td>
	                    <c:out value="${match.id}"/>
	                </td>
	                <td>
	                    <c:out value="${match.numPlayers}"/>
	                </td>
	                <td>
	                    <c:out value="${match.matchStatus}"/>
	                </td>
	                <td>
	                    <c:out value="${match.board.id}"/>
	                </td>
	                <td>
	                	<spring:url value="/admin/matchsList/matchinfo/{matchId}" var="infomatchurl">
	                    <spring:param name="matchId" value="${match.id}"/>
	                    </spring:url>
	      				<a href="${fn:escapeXml(infomatchurl)}"> Ver más información</a>
	      			</td>
	            </tr>
	        </c:if>
        </c:forEach>
        </tbody>
    </table>
	
	<h2>Historial de partidas</h2>
    <table id="matchsTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 150px;">Id</th>
			<th style="width: 150px;">Ganador</th>
            <th style="width: 150px;">Número de jugadores</th>
            <th style="width: 150px">Estado</th>
            <th style="width: 150px">Id mesa</th>
            <th style="width: 150px">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${matchs}" var="match">
        	<c:if test="${match.matchStatus eq 'FINISHED'}">
	            <tr>
	            	<td>
	                    <c:out value="${match.id}"/>
	                </td>
	                <td>
	                    <c:out value="${match.winner}"/>
	                </td>
	                <td>
	                    <c:out value="${match.numPlayers}"/>
	                </td>
	                <td>
	                    <c:out value="${match.matchStatus}"/>
	                </td>
	                <td>
	                    <c:out value="${match.board.id}"/>
	                </td>
	                <td>
	                	<spring:url value="/admin/matchsList/matchinfo/{matchId}" var="infomatchurl">
	                    <spring:param name="matchId" value="${match.id}"/>
	                    </spring:url>
	      				<a href="${fn:escapeXml(infomatchurl)}">Ver más información</a>
	      			</td>
	                
	            </tr>
	    	</c:if>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
