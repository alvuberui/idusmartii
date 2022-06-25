<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="error">

    <spring:url value="/resources/images/pets.png" var="petsImage"/>

    <h2>Ha ocurrido un error no esperado, revise la url que ha introducido</h2>	
   	<p>Vuelva a la página de inicio</p>
    <p>Perdón por las molestias</p>

    <p>${exception.message}</p>

</petclinic:layout>
