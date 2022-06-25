<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="players">
    <h1>Crear nuevo logro</h1>
	
    <form:form modelAttribute="achivement" class="form-horizontal" id="add-achivement-form">
        <div class="form-group has-feedback">
        
        	<b style="margin-left:205px">Título</b>
            <petclinic:inputField  name="title"/>
            
            <b style="margin-left:205px" >Descripción</b>
            <petclinic:inputField  name="description"/>
            
            <b style="margin-left:205px" >Cantidad</b>
            <petclinic:inputField  name="quantity"/>
            
            <p><b style="margin-left:205px">Condición</b></p>
            <form:select style="margin-left:205px"  path="conditions" name="conditions" items="${conditions}" />
            
            <p><b style="margin-left:205px">Entidad a la que se aplica</b></p>
            <form:select style="margin-left:205px"  path="aplicableEntity" name="aplicableEntity" items="${aplicableEntity}" />
        </div>
        
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            		<button class="btn btn-default" type="submit">Crear logro</button>  
            </div>
        </div>
    </form:form>
    
</petclinic:layout>