<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="players">
    <h2>Logros</h2>
	<sec:authorize access="hasAuthority('admin')">
     <a href="/admin/achivementsList/createachivement" class="btn btn-default">Crear nuevo logro</a>
    </sec:authorize>
    <br></br>
    <table id="playersTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Título</th>
            <th style="width: 150px;">Descripción</th>
            <th style="width: 150px;">Cantidad</th>
            <th style="width: 150px;">Condición</th>
            <th style="width: 150px;">Entidad</th>
            <sec:authorize access="hasAuthority('admin')">
            <th style="width: 150px;">Acciones</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${achivements}" var="achivement">
            <tr>
                <td>
                    <c:out value="${achivement.title}"/>
                </td>
                <td>
					<c:out value="${achivement.description}"/>
                </td>
                <td>
					<c:out value="${achivement.quantity}"/>
                </td>
                <td>
                    <c:out value="${achivement.conditions}"/>
                </td>
                <td>
                    <c:out value="${achivement.aplicableEntity}"/>
                </td>
                <sec:authorize access="hasAuthority('admin')">
      			<td>
      				<spring:url value="/admin/achivementsList/delete/{achivementId}" var="deleteachivementUrl">
                    <spring:param name="achivementId" value="${achivement.id}"/>
                    </spring:url>
      				<a href="${fn:escapeXml(deleteachivementUrl)}">Eliminar</a>
      				
      				<spring:url value="/admin/achivementsList/update/{achivementId}" var="updateachivementUrl">
                    <spring:param name="achivementId" value="${achivement.id}"/>
                    </spring:url>
      				<a href="${fn:escapeXml(updateachivementUrl)}">Actualizar</a>
      				
      			</td>
      			</sec:authorize>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>