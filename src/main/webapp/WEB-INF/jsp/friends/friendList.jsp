
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="friends">

<h2>FriendsNotPlaying</h2>
    <table id="friendsTableConnected" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Friend</th>
            <th style="width: 150px;">DeleteFriend</th>
            <sec:authorize access="hasAuthority('admin')">
            <th style="width: 150px;">Actions</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody>
        <sec:authentication var="name" property="name" />
        <c:forEach items="${friendsNotPlaying}" var="friendC">
         
        
             <c:if test = "${name == friendC.player1.user.username}">
		        <c:set var = "actualFriend2"  value = "${friendC.player2.user.username}"/>
		        </c:if>
		        <c:if test = "${name == friendC.player2.user.username}">
		        <c:set var = "actualFriend2"  value = "${friendC.player1.user.username}"/>
		        </c:if>
		            <tr>
		                <td>
							<c:out value="${actualFriend2}"/>
		                </td>
		                
		                
		                <td>
		                    <spring:url value="friendList/delete/{friendId}" var="deleteUrl">
		                    <spring:param name="friendId" value="${friendC.id}"/></spring:url>
		                    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Delete</a>
		                </td>
		              
		            </tr>
        </c:forEach>
        </tbody>
    </table>

<h2>Friends</h2>
	<div class="row">
		<div class="col-md-8">
		    <table id="friendsTable" class="table table-striped">
		        <thead>
		        <tr>
		            <th style="width: 150px;">Friend</th>
		            <th style="width: 150px;">DeleteFriend</th>
		            <sec:authorize access="hasAuthority('admin')">
		            <th style="width: 150px;">Actions</th>
		            </sec:authorize>
		        </tr>
		        </thead>
		        <tbody>
		        <sec:authentication var="name" property="name" />
		        <c:forEach items="${friends}" var="friend">
		         <c:if test = "${name == friend.player1.user.username}">
		        <c:set var = "actualFriend1"  value = "${friend.player2.user.username}"/>
		        <c:set var = "playerMain"  value = "${friend.player1.user.username}"/>
		        </c:if>
		        <c:if test = "${name == friend.player2.user.username}">
		        <c:set var = "actualFriend1"  value = "${friend.player1.user.username}"/>
		        <c:set var = "playerMain"  value = "${friend.player2.user.username}"/>
		        </c:if>
		            <tr>
		                <td>
							<c:out value="${actualFriend1}"/>
		                </td>
		                
		                
		                <td>
		                    <spring:url value="friendList/delete/{friendId}" var="deleteUrl">
		                    <spring:param name="friendId" value="${friend.id}"/></spring:url>
		                    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Delete</a>
		                </td>
		              
		            </tr>
		        </c:forEach>
		        </tbody>
		    </table>
		</div>
		<h2>Invitaciones a jugar</h2>
		<div class="col-md-4">
			<table id="friendsTable" class="table table-striped">
		        <thead>
		        <tr>
		            <th style="width: 150px;">Amigo</th>
		            <th style="width: 150px;">Acciones</th>
		        </tr>
		        </thead>
		        <tbody>
		        	 <c:forEach items="${friends}" var="friend">
				        <c:if test = "${null != friend.receiver}">
					        <c:if test = "${friend.sender.user.username!=playerMain}">
					        	<tr>
					        		<td><c:out value="${friend.sender.user.username}"/></td>
					        		<td>
							        	<spring:url value="play/join/{boardId}" var="joinUrl">
					                    	<spring:param name="boardId" value="${friend.sender.board.id}"/>
					                    	<spring:param name="friendId" value="${friend.id}"/></spring:url>
					                    <a href="${fn:escapeXml(joinUrl)}" class="btn btn-default">Unirte</a>
							        	
							        	<spring:url value="friendList/deleteInvitation/{friendId}" var="deleteUrl">
					                    <spring:param name="friendId" value="${friend.id}"/></spring:url>
					                    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Eliminar</a>
					                </td>
					            </tr>
					        </c:if>
				        </c:if>
				        
				     </c:forEach>
		        </tbody>
		    </table>
		</div>
	</div>
	
	

    <table id="friendsTablePending" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">RequestSender</th>
            <th style="width: 150px;">RequestAction</th>

            <sec:authorize access="hasAuthority('admin')">
            <th style="width: 150px;">Actions</th>
            </sec:authorize>
        </tr>
        </thead>


        <h2>PendingRequests</h2>
        <tbody>
        <c:forEach items="${friendsPending}" var="friendP">
        <c:if test = "${name == friendP.player1.user.username}">
        <c:set var = "actualFriend"  value = "${friendP.player2.user.username}"/>
        </c:if>
        <c:if test = "${name == friendP.player2.user.username}">
        <c:set var = "actualFriend"  value = "${friendP.player1.user.username}"/>
        </c:if>
            <tr>
                <td>
					<c:out value="${actualFriend}"/>
                </td>
                <td>
                    <spring:url value="friendList/edit/{friendId}" var="editUrl">
                    <spring:param name="friendId" value="${friendP.id}"/></spring:url>
                    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Accept</a>
                    <spring:url value="friendList/delete/{friendId}" var="deleteUrl">
                    <spring:param name="friendId" value="${friendP.id}"/></spring:url>
                    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Reject</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
       </table>

    <div class="col-md-4">
    <table id="sendRecomendationEmail" class="table table-striped">
        <thead>
            <tr>
                <th>Recomienda el juego a tus amigos</th>
            </tr>
        </thead>
        <tbody>
            <tr>
               <td>
                <form class="form-inline" action="/sendRecomendationEmail" method="get">
                     <div class="input-group">
                            <input size="55em" type="text" class="form-control" name="to" placeholder="ejemplo@email.com" aria-label="to" aria-describedby="basic-addon1">
                     </div>

                </form>
                </td>

            </tr>
        </tbody>
    </table>
    </div>
</petclinic:layout>

