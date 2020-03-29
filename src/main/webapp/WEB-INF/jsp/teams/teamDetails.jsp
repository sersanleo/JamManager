<%@ page session="false" trimDirectiveWhitespaces="true"
	import="org.springframework.samples.petclinic.model.InvitationStatus"%>
<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

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
			<td><a href="${fn:escapeXml(jamUrl)}"><c:out
						value="${team.jam.name}" /></a></td>
	</table>

	<br />
	<br />
	<b>Members</b>
	<table class="table table-striped">
		<tr>
			<th>Username</th>
			<th>Options</th>
		</tr>
		<c:forEach var="member" items="${team.members}">
			<tr>
				<td><c:out value="${member.username}" /></td>
				<td>
				<c:if test="${ isMember }">
				<spring:url
						value="/jams/{jamId}/teams/{teamId}/members/{username}/delete"
						var="deleteMemberUrl">
						<spring:param name="jamId" value="${team.jam.id}" />
						<spring:param name="teamId" value="${team.id}" />
						<spring:param name="username" value="${member.username}" />
					</spring:url> <a href="${fn:escapeXml(deleteMemberUrl)}" class="btn btn-default">Delete
						Member</a>
						</c:if>
						</td>

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
				<c:if test="${invitation.status != InvitationStatus.ACCEPTED}">
					<tr>
						<td><c:out value="${invitation.to.username}" /></td>
						<td><petclinic:localDateTime
								date="${invitation.creationDate}" /></td>
						<td><c:out value="${invitation.status}" /></td>
						<td><spring:url
								value="/jams/{jamId}/teams/{teamId}/invitations/{invitationId}/delete"
								var="deleteInvUrl">
								<spring:param name="jamId" value="${jam.id}" />
								<spring:param name="teamId" value="${team.id}" />
								<spring:param name="invitationId" value="${invitation.id}" />
							</spring:url> <a href="${fn:escapeXml(deleteInvUrl)}" class="btn btn-default">Delete
								Invitation</a></td>
						</td>
					</tr>
				</c:if>
			</c:forEach>
		</table>

		<spring:url value="{teamId}/edit" var="editUrl">
			<spring:param name="teamId" value="${team.id}" />
		</spring:url>
		<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit
			Team</a>

		<spring:url value="/jams/{jamId}/teams/{teamId}/invitations/new"
			var="newUrl">
			<spring:param name="jamId" value="${jam.id}" />
			<spring:param name="teamId" value="${team.id}" />
		</spring:url>
		<a href="${fn:escapeXml(newUrl)}" class="btn btn-default">Send
			Invitation</a>
	</c:if>
</petclinic:layout>
