<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="achivementCreate">
    <h1>Actualizar logro</h1>
	
	<form:form modelAttribute="achivement">
		<div class="form-group">
			<label for="tittle">Título</label>
		    <input type="text" class="form-control" id="tittle" value="${oldachivement.title}" name="title">
		</div>
		<div class="form-group">
		    <label for="Description">Descripción</label>
		    <input type="text" class="form-control" id="description" value="${oldachivement.description}" name="description">
		</div>
		<div class="form-group">
		    <label for="Quantity">Cantidad</label>
		    <input type="number" class="form-control" id="quantity" value="${oldachivement.quantity}" name="quantity">
		</div>
		<div class="form-group">
		    <p><label for="Condition">Condición</label></p>
		    <form:select  path="conditions" name="conditions" items="${conditions}" value="${oldachivement.conditions}" />
		</div>
		<div class="form-group">
		<p><label for="Aplicable_Entity">Entidad a la que se aplica</label></p>
            <form:select path="aplicableEntity" name="aplicableEntity" items="${aplicableEntity}" value="${oldachivement.aplicableEntity}" />
		</div>
		  <button type="submit" class="btn btn-default">Actualizar logro</button>
	</form:form>
</petclinic:layout>