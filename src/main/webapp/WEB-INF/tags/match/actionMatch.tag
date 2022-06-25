<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<table class="table table-striped">
  <thead>
    <tr>
      <th scope="col">#</th>
      <th scope="col">Accion</th>
    </tr>
  </thead>
  <tbody>
    <c:set var="contador" value="${0}" />
   <c:forEach var="frase" items="${turnLog}">
        <c:set var="contador" value="${0 + 1}" />
        <tr>
            <th scope="row"> <c:out value="${contador}" /></th>
            <td><c:out value="${frase}" /></td>
        </tr>
   </c:forEach>
</table>
