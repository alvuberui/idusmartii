
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="friends">

    <h2>Estadisticas Globales</h2>
    
    <table class="table table-striped">
    	<tr>
    		<th>Jugadores Totales</th>
    		<th><b><c:out value="${totalPlayers}"/></b></th>
    	</tr>
    	<tr>
    		<th>Media de puntos</th>
    		<th><b><c:out value="${globalEstadistic.points/totalPlayers}"/></b></th>
    	</tr> 
        <tr>
    		<th>Partidas totales jugadas</th>
    		<th><b><c:out value="${globalEstadistic.matchsPlayed}"/></b></th>
    	</tr>

    	<tr>
    		<th>Porcentaje de partidas Ganadas</th>
    		<th><b><c:out value="${(globalEstadistic.matchsWins/globalEstadistic.matchsPlayed)*100}%"/></b></th>
    	</tr>
    	<tr>
    		<th>Porcentaje de partidas Perdidas</th>
    		<th><b><c:out value="${(globalEstadistic.matchsLoses/globalEstadistic.matchsPlayed)*100}%"/></b></th>
    	</tr>
    	<tr>
    		<th>Racha de victoria mas larga</th>
    		<th><b><c:out value="${globalEstadistic.winsLongerStrike}"/></b></th>
    	</tr>
    	<tr>
    		<th>Partida con duración mas larga</th>
    		<th><b><c:out value="${longuest}"/></b></th>
    	</tr>
    	<tr>
    		<th>Partida con duración mas corta</th>
    		<th><b><c:out value="${shortest}"/></b></th>
    	</tr>
    	<tr>
    		<th>Facción con mas partidas ganadas</th>
    		<th><c:out value="${factionMostWinned}"/></th>
    	</tr>
    	
    </table>
       
       
       
    <h2>Players Ranking</h2>
       
       <table class="table table-striped">
       <thead>
        <tr>
            <th style="width: 150px;">Jugador</th>
            <th style="width: 150px;">Puntos</th>
            <th style="width: 150px;">Posicion</th>
       </tr>
       </thead>
        <c:forEach items="${playerRanking}" var="player">
            <tr>
                <th>
					<c:out value="${player.user.username}"/>
                </th>
                <td>
                    <c:out value="${player.estadistic.points}"/>
                </td>
                <td>
                    <c:out value="${player.estadistic.rankingPos}"/>
                </td>
              
            </tr>
        </c:forEach>
        <thead>
        <tr>
        	<th style="width: 150px;">Tu</th>
        	<th style="width: 150px;"></th>
        	<th style="width: 150px;"></th>
        </tr>
        </thead>
        <tr>
                <th>
					<c:out value="${actualPlayer.user.username}"/>
                </th>
                <td>
                    <c:out value="${actualPlayer.estadistic.points}"/>
                </td>
                <td>
                    <c:out value="${actualPlayer.estadistic.rankingPos}"/>
                </td>
              
            </tr>
    </table>
    
    
    
</petclinic:layout>
