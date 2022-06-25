<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="players">
    <h1>Información de la partida</h1>
  	
  	<div class="row">
	
		<!-- alvuberui: Información sobre la partida (ronda y turno)-->
		<div class="col-md-8">
		    <table id="playersTable" class="table table-striped">
		        <thead>
		        <tr>
		            <th style="width: 150px;">Id</th>
		            <th style="width: 150px;">Anfitrión</th>
		            <th style="width: 150px;">Ganador</th>
		            <th style="width: 150px;">Número de jugadores</th>
		            <th style="width: 150px;">Estado</th>
		            <th style="width: 150px;">Id mesa</th>
		        </tr>
		        </thead>
		        <tbody>
		            <tr>
		                <td>
		                    <c:out value="${match.id}"/>
		                </td>
		                <td>
							<c:out value="${match.board.anfitrion.user.username}"/>
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
		            </tr>
		        </tbody>
		    </table>
		 </div>
		 <div class="col-md-4">
			<c:if test="${match.matchStatus != 'FINISHED'}">
				<table id="playersTable" class="table table-striped">
			        <thead>
			        <tr>
			            <th style="width: 150px;">Jugadores</th>
			        </tr>
			        </thead>
			        <tbody>
			        	<c:forEach items="${playersPlaying}" var="player">
			        		<tr>
			                	<td>
				                    <c:out value="${player.user.username}"/>
			                	</td>  
			            	</tr>
				        </c:forEach>
			        </tbody>
			    </table>
			</c:if>
			<c:if test="${match.matchStatus == 'FINISHED'}">
			    <table id="playersTable" class="table table-striped">
			        <thead>
			        <tr>
			            <th style="width: 150px;">Jugadores</th>
			        </tr>
			        </thead>
			        <tbody>
			        	<c:forEach items="${players}" var="player">
			        		<c:forEach items="${player.matchsPlayed}" var="pm">
					        	<c:if test="${pm.id eq match.id}">
				            		<tr>
				                		<td>
					                    	<c:out value="${player.user.username}"/>
				                		</td>  
				            		</tr>
				            	</c:if>
				        	</c:forEach>
				        </c:forEach>
			        </tbody>
			    </table>
			</c:if>
		 </div>
	</div>
</petclinic:layout>