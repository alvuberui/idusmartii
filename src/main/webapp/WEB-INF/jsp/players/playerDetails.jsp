<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="players">

    <h2>Player Information</h2>


    <table class="table table-striped">
    	<thead>
            <tr>
        		<th style="width: 150px;"></th>
        		<th style="width: 150px;"></th>
        	</tr>
        </thead>
        <tr>
            <th>User Name</th>
            <td><b><c:out value="${player.user.username}"/></b></td>
        </tr>
        <tr>
            <th>First Name</th>
            <td><c:out value="${player.firstName}"/></td>
        </tr>
        <tr>
            <th>Last Name</th>
            <td><c:out value="${player.lastName}"/></td>
        </tr>
        <tr>
            <th>Email</th>
            <td><c:out value="${player.email}"/></td>
        </tr>
    </table>
       <sec:authorize access="hasAuthority('admin')">
	     <spring:url value="/edit/{playerId}" var="editUrl">
        <spring:param name="playerId" value="${player.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Player</a>
    </sec:authorize>
    
    
    <h2>Player Estadistics</h2>
        
    <table class="table table-striped">
    	<thead>
            <tr>
        		<th style="width: 150px;"></th>
        		<th style="width: 150px;"></th>
        	</tr>
        </thead>
    	<tr>
    		<th>Points</th>
    		<td><b><c:out value="${estadistic.points}"/></b></td>
    	</tr> 
    	<tr>
    		<th>Matchs Played</th>
    		<td><c:out value="${estadistic.matchsPlayed}"/></td>
    	</tr>
    	<tr>
    		<th>Matchs Won</th>
    		<td><c:out value="${estadistic.matchsWins}"/></td>
    	</tr> 
    	<tr>
    		<th>Winned Matchs %</th>
    		<td><c:out value="${(estadistic.matchsWins/estadistic.matchsPlayed)*100}"/></td>
    	</tr>
    	<tr>
    		<th>Longuer Win Strike</th>
    		<td><c:out value="${estadistic.winsLongerStrike}"/></td>
    	</tr>
    	<tr>
    		<th>Longuest Match</th>
    		<td><c:out value="${longer}"/></td>
    	</tr>
    	<tr>
    		<th>Shortest Match</th>
    		<td><c:out value="${shorter}"/></td>
    	</tr>
    </table>
    
    <h2>Player Achivements</h2>
    
    <table class="table table-striped">
    	<thead>
            <tr>
        		<th style="width: 150px;">Logro</th>
        		<th style="width: 150px;">Descripción</th>
        	</tr>
        </thead>
        <c:forEach items="${achivements}" var="achivement">
    	<tr>
    		<th>
    			<c:out value="${achivement.title}"/>
    		</th>
    		<td>
    			<c:out value="${achivement.description}"/>
    		</td>
    	</tr> 
    	</c:forEach> 
    </table>
    <br/>
    <br/>
    <br/>


</petclinic:layout>
