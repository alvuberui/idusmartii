<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="error">

    

    <h2>ERROR 500</h2>
	<p>Ha habido un error en el servidor, por favor vuelva a la página de inicio</p>
	<p>Disculpe las molestias</p>
    <p>${exception.message}</p>

</petclinic:layout>