<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="error">

    

    <h2>ERROR 403</h2>
	<p>Permiso denegado, por favor vuelva a la página de Inicio</p>
	<p>Disculpe las molestias</p>
    <p>${exception.message}</p>

</petclinic:layout>