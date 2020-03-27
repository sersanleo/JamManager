<%@ page session="false" trimDirectiveWhitespaces="true" import="org.springframework.samples.petclinic.model.InvitationStatus"%>
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
			<th>Creation Date</th>
			<td><petclinic:localDateTime date="${team.creationDate}" /></td>
		</tr>
	</table>

	<br />
	<b>Members</b>
	<table class="table table-striped">
		<tr>
			<th>Username</th>
		</tr>
		<c:forEach var="member" items="${team.members}">
			<tr>
				<td><c:out value="${member.username}" /></td>
			</tr>
		</c:forEach>
	</table>

	<br />
	<b>Pending invitations</b>
	<table class="table table-striped">
		<tr>
			<th>Username</th>
			<th>Date</th>
		</tr>
		<c:forEach var="invitation" items="${team.invitations}">
			<c:if test="${invitation.status == InvitationStatus.PENDING}">
			<tr>
				<td><c:out value="${invitation.to.username}" /></td>
				<td><petclinic:localDateTime date="${invitation.creationDate}" /></td>
			</tr>
			</c:if>
		</c:forEach>
	</table>

	<spring:url value="{teamId}/edit" var="editUrl">
		<spring:param name="teamId" value="${team.id}" />
	</spring:url>
	<a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Team</a>

	<spring:url value="/jams/{jamId}/teams/{teamId}/invitations/new" var="newUrl">
		<spring:param name="jamId" value="${jam.id}" />
		<spring:param name="teamId" value="${team.id}" />
	</spring:url>
	<a href="${fn:escapeXml(newUrl)}" class="btn btn-default">Send Invitation</a>
</petclinic:layout>
