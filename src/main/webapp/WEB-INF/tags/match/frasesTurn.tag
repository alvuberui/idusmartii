<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<c:choose>
    <c:when test="${turn.matchTurnStatus == 'WAITTOVOTE'}">
          <h4>Los ediles estan votando</h4>
    </c:when>
    <c:when test="${turn.matchTurnStatus == 'CHOOSE_ROL'}">
       <h4>El consul esta eligiendo los roles</h4>
    </c:when>
    <c:when test="${turn.matchTurnStatus == 'CHANGING_VOTE'}">
       <h4>El pretor puede cambiar uno de los dos votos</h4>
    </c:when>
    <c:when test="${turn.matchTurnStatus == 'REVOTE'}">
       <h4>El edil "${turn.pretor.edil.player.user.username}" esta votando de nuevo</h4>
    </c:when>
    <c:when test="${turn.matchTurnStatus == 'CONT'}">
       <h4>Se estan contando los votos</h4>
    </c:when>
    <c:when test="${turn.matchTurnStatus == 'CHOOSE_FACCTION'}">
       <h4>El consul esta eligiendo faccion</h4>
    </c:when>
</c:choose>
