<%@ page session="false" trimDirectiveWhitespaces="true" import="org.springframework.samples.petclinic.model.JamStatus"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<sec:authorize access="hasAuthority('jamOrganizator')">
	<c:set var="isOrganizator" value="true" />
</sec:authorize>

<petclinic:layout pageName="jams">

	<h2>Jam Information</h2>

	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<td><b><c:out value="${jam.name}" /></b></td>
		</tr>
		<tr>
			<th>Description</th>
			<td><c:out value="${jam.description}" /></td>
		</tr>
		<tr>
			<th>Difficulty</th>
			<td><c:out value="${jam.difficulty}" /></td>
		</tr>
		<tr>
			<th>Inscription deadline</th>
			<td><petclinic:localDateTime date="${jam.inscriptionDeadline}" /></td>
		</tr>
		<tr>
			<th>Max. team members</th>
			<td><c:out value="${jam.maxTeamSize}" /></td>
		</tr>
		<tr>
			<th>Min. teams</th>
			<td><c:out value="${jam.minTeams}" /></td>
		</tr>
		<tr>
			<th>Inscribed teams</th>
			<td><c:out value="${jam.teams.size()}" />/<c:out value="${jam.maxTeams}" /></td>
		</tr>
		<tr>
			<th>Start date</th>
			<td><petclinic:localDateTime date="${jam.start}" /></td>
		</tr>
		<tr>
			<th>End date</th>
			<td><petclinic:localDateTime date="${jam.end}" /></td>
		</tr>
		<tr>
			<th>Status</th>
			<td><c:out value="${jam.status}" /></td>
		</tr>
		<tr>
			<th>Created by</th>
			<td><c:out value="${jam.creator.username}" /></td>
		</tr>
	</table>

	<c:if test="${ isOrganizator && jam.status == JamStatus.INSCRIPTION }">
		<spring:url value="{jamId}/edit" var="editUrl">
			<spring:param name="jamId" value="${jam.id}" />
		</spring:url>
		<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Jam</a>
	</c:if>

	<c:if test="${ isOrganizator || jam.status == JamStatus.IN_PROGRESS }">
		<br />
		<br />
		<br />
		<b>Resources</b>
		<table class="table table-striped">
			<tr>
				<th>Download URL</th>
				<th>Description</th>
			</tr>
			<c:forEach var="resource" items="${jam.resources}">
				<tr>
					<td><c:out value="${resource.downloadUrl}" /></td>
					<td><c:out value="${resource.description}" /></td>
				</tr>
			</c:forEach>
		</table>

		<c:if test="${ isOrganizator }">
			<spring:url value="{jamId}/resources/new" var="addResourceUrl">
				<spring:param name="jamId" value="${jam.id}" />
			</spring:url>
			<a href="${fn:escapeXml(addResourceUrl)}" class="btn btn-default">Add New Resource</a>
		</c:if>
	</c:if>
	
	<br />
	<br />
	<br />
	<b>Teams</b>
	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<th>Members</th>
		</tr>
		<c:forEach var="team" items="${jam.teams}">
			<tr>
				<spring:url value="/jams/{jamId}/teams/{teamId}" var="teamUrl">
					<spring:param name="jamId" value="${jam.id}" />
					<spring:param name="teamId" value="${team.id}" />
				</spring:url>
				<td><a href="${fn:escapeXml(teamUrl)}"><c:out value="${team.name}" /></a></td>
				<td><c:forEach var="member" items="${team.members}">
						<c:out value="${member.username}" />
						<br>
					</c:forEach></td>
			</tr>
		</c:forEach>
	</table>

	<c:if test="${ jam.status == JamStatus.INSCRIPTION }">
		<spring:url value="{jamId}/teams/new" var="addTeamUrl">
			<spring:param name="jamId" value="${jam.id}" />
		</spring:url>
		<a href="${fn:escapeXml(addTeamUrl)}" class="btn btn-default">Join this Jam</a>
	</c:if>
</petclinic:layout>
