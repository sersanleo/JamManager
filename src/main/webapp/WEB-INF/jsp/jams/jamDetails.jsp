
<%@ page session="false" trimDirectiveWhitespaces="true" import="org.springframework.samples.petclinic.model.JamStatus"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<sec:authorize access="hasAuthority('jamOrganizator')">
	<c:set var="isOrganizator" value="true" />
</sec:authorize>
<sec:authorize access="hasAuthority('member')">
	<c:set var="isMember" value="true" />
</sec:authorize>
<sec:authorize access="hasAuthority('judge')">
	<c:set var="isJudge" value="true" />
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
		<c:if test="${ jam.status == JamStatus.FINISHED }">
			<tr>
				<th>Winner</th>
				<td><spring:url value="/jams/{jamId}/teams/{teamId}" var="winnerUrl">
						<spring:param name="jamId" value="${jam.id}" />
						<spring:param name="teamId" value="${jam.winner.id}" />
					</spring:url> <a href="${fn:escapeXml(winnerUrl)}"><c:out value="${jam.winner.name}" /></a></td>
			</tr>
		</c:if>
	</table>


	<c:if test="${ isOrganizator && jam.status == JamStatus.INSCRIPTION }">
		<spring:url value="{jamId}/edit" var="editUrl">
			<spring:param name="jamId" value="${jam.id}" />
		</spring:url>
		<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Jam</a>

		<spring:url value="{jamId}/delete" var="deleteUrl">
			<spring:param name="jamId" value="${jam.id}" />
		</spring:url>
		<a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Delete Jam</a>
	</c:if>

	<c:if test="${ isOrganizator || jam.status == JamStatus.IN_PROGRESS  }">
		<br />
		<br />
		<br />
		<b>Resources</b>
		<table class="table table-striped">
			<tr>
				<th>Download URL</th>
				<th>Description</th>
				<c:if test="${ isOrganizator }">
					<th></th>
					<th></th>
				</c:if>
			</tr>
			<c:forEach var="jamResource" items="${jam.jamResources}">
				<tr>
					<td><a href="${jamResource.downloadUrl}"><c:out value="${jamResource.downloadUrl}" /></a></td>
					<td><c:out value="${jamResource.description}" /></td>
					<c:if test="${ isOrganizator }">
						<td><spring:url value="{jamId}/jamResources/{jamResourceId}/edit" var="editResUrl">
								<spring:param name="jamId" value="${jam.id}" />
								<spring:param name="jamResourceId" value="${jamResource.id}" />
							</spring:url> <a href="${fn:escapeXml(editResUrl)}" class="btn btn-default">Edit Jam Resource</a></td>
						</td>
						<td><spring:url value="{jamId}/jamResources/{jamResourceId}/delete" var="deleteResUrl">
								<spring:param name="jamId" value="${jam.id}" />
								<spring:param name="jamResourceId" value="${jamResource.id}" />
							</spring:url> <a href="${fn:escapeXml(deleteResUrl)}" class="btn btn-default">Delete Jam Resource</a></td>
						</td>
					</c:if>

				</tr>
			</c:forEach>
		</table>

		<c:if test="${ isOrganizator }">
			<spring:url value="{jamId}/jamResources/new" var="addResourceUrl">
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
			<c:if test="${ (isJudge && jam.status == JamStatus.RATING) || jam.status == JamStatus.FINISHED }">
				<th>Mark</th>
			</c:if>
			<c:if test="${ isJudge && jam.status == JamStatus.RATING }">
				<th></th>
			</c:if>
		</tr>
		<c:forEach var="team" items="${jam.teams}">
			<tr>
				<td><spring:url value="/jams/{jamId}/teams/{teamId}" var="teamUrl">
						<spring:param name="jamId" value="${jam.id}" />
						<spring:param name="teamId" value="${team.id}" />
					</spring:url> <a href="${fn:escapeXml(teamUrl)}"><c:out value="${team.name}" /></a></td>
				<td><c:forEach var="member" items="${team.members}">
						<c:out value="${member.username}" />
						<br>
					</c:forEach></td>
				<c:if test="${ (isJudge && jam.status == JamStatus.RATING) || jam.status == JamStatus.FINISHED }">
					<td>${ (team.average == null) ? "-" : team.average }/10<c:if test="${ isJudge }">
						 (${team.marks.size()} marks given)
					</c:if>
					</td>
				</c:if>
				<c:if test="${ isJudge && jam.status == JamStatus.RATING }">
					<sec:authentication var="principal" property="principal" />
					<spring:url value="/jams/{jamId}/teams/{teamId}/marks" var="markUrl">
						<spring:param name="jamId" value="${jam.id}" />
						<spring:param name="teamId" value="${team.id}" />
					</spring:url>
					<td><c:if test="${ !team.isMarkedBy(principal.username) && jam.status == JamStatus.RATING }">
							<a href="${fn:escapeXml(markUrl)}" class="btn btn-default">Give A Mark</a>
						</c:if></td>
				</c:if>
			</tr>
		</c:forEach>
	</table>
	<c:if test="${ isJudge && jam.status == JamStatus.RATING }">
		<spring:url value="{jamId}/publish" var="publishResultsUrl">
			<spring:param name="jamId" value="${jam.id}" />
		</spring:url>
		<a href="${fn:escapeXml(publishResultsUrl)}" class="btn btn-default">Publish Results</a>
	</c:if>

	<c:if test="${ jam.status == JamStatus.INSCRIPTION && jam.isFull == false && !hasTeam && isMember}">
		<spring:url value="{jamId}/teams/new" var="addTeamUrl">
			<spring:param name="jamId" value="${jam.id}" />
		</spring:url>
		<a href="${fn:escapeXml(addTeamUrl)}" class="btn btn-default">Join this Jam</a>
	</c:if>
</petclinic:layout>
