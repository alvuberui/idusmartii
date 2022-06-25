<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<petclinic:layout pageName="admin">
    <h1>Menú de administrador</h1>
    <ul><h3>Administrar jugadores</h3></ul>
    <ul><a  href="${fn:escapeXml('/admin/playersList')}" class="btn btn-default">Administrar jugadores</a></ul>
    <ul><h3>Administrar partidas</h3></ul>
    <ul><a  href="${fn:escapeXml('/admin/matchsList')}" class="btn btn-default">Administrar partidas</a></ul>
    <ul><h3>Administrar Logros</h3></ul>
    <ul><a  href="${fn:escapeXml('/admin/achivementsList')}" class="btn btn-default">Administrar logros</a></ul>
</petclinic:layout>
