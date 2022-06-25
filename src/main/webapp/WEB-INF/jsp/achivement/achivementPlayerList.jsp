<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="achivements">
    <h2>Achivements</h2>

    <table id="achivementsTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 150px;">Achivement Name</th>         
            <th style="width: 150px;">Description</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${achivements}" var="achivement">
            <tr>
                <td>
                   <th><c:out value="${achivement.title}"/></th>
                </td>
                <td>
                   <th><c:out value="${achivement.description}"/></th>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>