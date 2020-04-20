<%@ page session="false" trimDirectiveWhitespaces="true"
	import="org.springframework.samples.petclinic.model.InvitationStatus,org.springframework.samples.petclinic.model.JamStatus"%>

<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<sec:authorize access="hasAuthority('judge')">
	<c:set var="isJudge" value="true" />
</sec:authorize>

<petclinic:layout pageName="jams">

	<h2>Team Information</h2>
	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<td><b><c:out value="${team.name}" /></b></td>
		</tr>
		<tr>
			<th>Creation date</th>
			<td><petclinic:localDateTime date="${team.creationDate}" /></td>
		</tr>
		<tr>
			<spring:url value="/jams/{jamId}" var="jamUrl">
				<spring:param name="jamId" value="${team.jam.id}" />
			</spring:url>
			<th>Jam</th>
			<td><a href="${fn:escapeXml(jamUrl)}"><c:out value="${team.jam.name}" /></a></td>
	</table>

	<br />
	<br />
	<b>Members</b>
	<table class="table table-striped">
		<tr>
			<th>Username</th>
			<th></th>
		</tr>
		<c:forEach var="member" items="${team.members}">
			<tr>
				<td><c:out value="${member.username}" /></td>
				<td><c:if test="${ isMember && team.jam.status == JamStatus.INSCRIPTION}">
						<spring:url value="/jams/{jamId}/teams/{teamId}/members/{username}/delete" var="deleteMemberUrl">
							<spring:param name="jamId" value="${team.jam.id}" />
							<spring:param name="teamId" value="${team.id}" />
							<spring:param name="username" value="${member.username}" />
						</spring:url>
						<a href="${fn:escapeXml(deleteMemberUrl)}" class="btn btn-default">Delete Member</a>
					</c:if></td>

				</td>
			</tr>
		</c:forEach>
	</table>

	<c:if test="${isMember}">
		<br />
		<br />
		<b>Invitations</b>
		<table class="table table-striped">
			<tr>
				<th>Username</th>
				<th>Date</th>
				<th>Status</th>
				<th></th>
			</tr>
			<c:forEach var="invitation" items="${team.invitations}">
				<tr>
					<td><c:out value="${invitation.to.username}" /></td>
					<td><petclinic:localDateTime date="${invitation.creationDate}" /></td>
					<td><c:out value="${invitation.status}" /></td>
					<td><c:if test="${ team.jam.status == JamStatus.INSCRIPTION  && invitation.status == InvitationStatus.PENDING }">
							<spring:url value="/jams/{jamId}/teams/{teamId}/invitations/{invitationId}/delete" var="deleteInvUrl">
								<spring:param name="jamId" value="${jam.id}" />
								<spring:param name="teamId" value="${team.id}" />
								<spring:param name="invitationId" value="${invitation.id}" />
							</spring:url>
							<a href="${fn:escapeXml(deleteInvUrl)}" class="btn btn-default">Delete Invitation</a>
						</c:if></td>
					</td>
				</tr>
			</c:forEach>
		</table>

		<spring:url value="{teamId}/edit" var="editUrl">
			<spring:param name="teamId" value="${team.id}" />
		</spring:url>
		<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Team</a>

		<c:if test="${ !team.isFull }">
			<spring:url value="/jams/{jamId}/teams/{teamId}/invitations/new" var="newUrl">
				<spring:param name="jamId" value="${jam.id}" />
				<spring:param name="teamId" value="${team.id}" />
			</spring:url>
			<a href="${fn:escapeXml(newUrl)}" class="btn btn-default">Send Invitation</a>
		</c:if>
	</c:if>

	<c:if
		test="${ (isJudge && team.jam.status == JamStatus.RATING) || ((isJudge || isMember) && team.jam.status == JamStatus.FINISHED) }">
		<br />
		<br />
		<b>Marks</b>
		<table class="table table-striped">
			<tr>
				<th>Judge</th>
				<th>Mark</th>
				<th>Comments</th>
				<c:if test="${ team.jam.status == JamStatus.RATING }">
					<th></th>
				</c:if>
			</tr>
			<sec:authentication var="principal" property="principal" />
			<c:forEach var="mark" items="${team.marks}">
				<tr>
					<td><c:out value="${mark.judge.username}" /></td>
					<td><c:out value="${mark.value}" /></td>
					<td><c:out value="${mark.comments}" /></td>
					<c:if test="${ team.jam.status == JamStatus.RATING }">
						<td><c:if test="${ mark.judge.username.equals(principal.username) }">
								<spring:url value="/jams/{jamId}/teams/{teamId}/marks" var="markUrl">
									<spring:param name="jamId" value="${jam.id}" />
									<spring:param name="teamId" value="${team.id}" />
								</spring:url>
								<a href="${fn:escapeXml(markUrl)}" class="btn btn-default">Edit</a>
							</c:if></td>
					</c:if>
				</tr>
			</c:forEach>
		</table>
	</c:if>
</petclinic:layout>
