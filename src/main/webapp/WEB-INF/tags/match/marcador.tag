<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="container" style="position: absolute; left: 50%; display: flex">
            <div  style="width: 25px;height: 25px; background: #F8D625; border: 1px solid #F5AD25; margin: 10px;  text-align: center;font-family: 'DalekPinpointBold';">
                <c:out value="${match.votosLeal}" />
            </div>
            <div style="width: 25px;height: 25px; background:  #E2372B; border: 1px solid #F5AD25; margin: 10px; text-align: center; padding: auto;font-family: 'DalekPinpointBold';">
                <c:out value="${match.votosTraidor}" />
            </div>
</div>
