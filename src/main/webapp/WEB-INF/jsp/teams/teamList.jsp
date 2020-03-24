<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="teams">
	<h2>Teams</h2>

	<table id="teamsTable" class="table table-striped">
		<thead>
			<tr>
            <th style="width: 200px;">Name</th>
            <th style="width: 300px;">Creation Date</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${teams}" var="team">
				<tr>
					<td><spring:url value="/teams/{teamId}" var="teamUrl">
							<spring:param name="teamId" value="${team.id}" />
						</spring:url> <a href="${fn:escapeXml(teamUrl)}"><c:out value="${team.name}" /></a>
					</td>
					<td><c:out value="${team.creationDate}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<table class="table-buttons">
		<tr>
			<td><a href="<spring:url value="/teams/teams.xml" htmlEscape="true" />">View as XML</a></td>
		</tr>
	</table>

    <br/> 
    <sec:authorize access="hasAuthority('admin')">
		<a class="btn btn-default" href='<spring:url value="/teams/new" htmlEscape="true"/>'>Add Team</a>
	</sec:authorize>
</petclinic:layout>
