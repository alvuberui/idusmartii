<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="error">

    

    <h2>ERROR 404</h2>
	<p>Esta p�gina no ha podido ser encontrada, por favor revise la direcci�n que ha introducido</p>
	<p>Disculpe las molestias</p>
    <p>${exception.message}</p>

</petclinic:layout>