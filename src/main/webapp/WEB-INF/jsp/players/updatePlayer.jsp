<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="players">
    <h1>Actualizar jugador</h1>
	
	<form:form modelAttribute="player">
		<div class="form-group">
			<label for="firstName">Nombre</label>
		    <input type="text" class="form-control" id="firstName" value="${oldplayer.firstName}" name="firstName">
		</div>
		<div class="form-group">
		    <label for="lastName">Appelido</label>
		    <input type="text" class="form-control" id="lastName" value="${oldplayer.lastName}" name="lastName">
		</div>
		<div class="form-group">
		    <label for="username">Nombre de usuario</label>
		    <input type="text" class="form-control" id="username" value="${oldplayer.user.username}" name="user.username">
		</div>
		<div class="form-group">
		    <label for="email">Email</label>
		    <input type="email" class="form-control" id="email" value="${oldplayer.email}" name="email">
		</div>
		<div class="form-group">
		    <label for="password">Contraseña</label>
		    <input type="password" class="form-control" id="password" value="${oldplayer.user.password}" name="user.password">
		</div>
		  <button type="submit" class="btn btn-default">Actualizar jugador</button>
	</form:form>
	
   
    
</petclinic:layout>