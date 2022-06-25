<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<petclinic:layout pageName="comments">
	<h1>Publicar comentario</h1>
	
    <form:form modelAttribute="comment" class="form-horizontal">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Comentario" name="comment"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<button class="btn btn-default" type="submit">Publicar comentario</button>  
            </div>
        </div>
    </form:form>
    
    <h1>Foro de partida</h1>
    <table id="commentsTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Usuario</th>
            <th style="width: 150px;">Comentario</th>
            <th style="width: 150px;">Fecha y hora</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${comments}" var="comment">
            <tr>
                <td>
                    <c:out value="${comment.player.user.username}"/>
                </td>
                <td>
					<c:out value="${comment.comment}"/>
                </td>
                <td>
                    <c:out value="${comment.date}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>