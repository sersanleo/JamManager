<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="jams">
	<h2>Jams</h2>

	<table id="jamsTable" class="table table-striped">
		<thead>
			<tr>
				<th>Name</th>
				<th>Difficulty</th>
				<th>Inscription deadline</th>
				<th>Start date</th>
				<th>End date</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${jams}" var="jam">
				<tr>
					<td><spring:url value="/jams/{jamId}" var="jamUrl">
							<spring:param name="jamId" value="${jam.id}" />
						</spring:url> <a href="${fn:escapeXml(jamUrl)}"><c:out value="${jam.name}" /></a>
					</td>
					<td><c:out value="${jam.difficulty}" />/5</td>
					<td><petclinic:localDateTime date="${jam.inscriptionDeadline}" /></td>
					<td><petclinic:localDateTime date="${jam.start}" /></td>
					<td><petclinic:localDateTime date="${jam.end}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<table class="table-buttons">
		<tr>
			<td><a href="<spring:url value="/jams/jams.xml" htmlEscape="true" />">View as XML</a></td>
		</tr>
	</table>

    <br/> 
    <sec:authorize access="hasAuthority('jamOrganizator')">
		<a class="btn btn-default" href='<spring:url value="/jams/new" htmlEscape="true"/>'>Add Jam</a>
	</sec:authorize>
</petclinic:layout>
