
<%@ page session="false" trimDirectiveWhitespaces="true" import="org.springframework.samples.petclinic.model.JamStatus"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<sec:authorize access="hasAuthority('jamOrganizator')">
	<c:set var="isOrganizator" value="true" />
</sec:authorize>


<petclinic:layout pageName="dashboard">


					<h2> Jams: </h2> 
					<b>Total number of Jams:</b><c:out value="${jams}"></c:out>
					<br><br>

					<h2> Teams: </h2> 
					<b>Total number of Teams:</b><c:out value="${teams}"></c:out>

					<br><br>			








</petclinic:layout>