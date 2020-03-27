<%@ page session="false" trimDirectiveWhitespaces="true" import="org.springframework.samples.petclinic.model.InvitationStatus"%>
<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="invitatons">
	<h2>Invitations</h2>

	<table id="invitationsUserTable" class="table table-striped">
		<thead>
			<tr>
				<th>Team</th>
				<th>Creation Date</th>
				<th>Status</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${invitations}" var="invitation">
				<tr>
					<td><c:out value="${invitation.from.name}" /></td>
					<td><c:out value="${invitation.creationDate}" /></td>
					<td><c:out value="${invitation.status}" /></td>
					<c:if test="${invitation.status == InvitationStatus.PENDING }">
					<td><spring:url value="/invitations/{invitationId}/accept" var="AccUrl">
						<spring:param name="invitationId" value="${invitation.id}" />
					</spring:url>
					<a href="${AccUrl}" class="btn btn-default">Accept Invitation</a></td>
					<td><spring:url value="/invitations/{invitationId}/reject" var="RejUrl">
						<spring:param name="invitationId" value="${invitation.id}" />
					</spring:url>
					<a href="${RejUrl}" class="btn btn-default">Reject Invitation</a></td>
					</c:if>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<table class="table-buttons">
		<tr>
			<td><a href="<spring:url value="/invitations/invitations.xml" htmlEscape="true" />">View as XML</a></td>
		</tr>
	</table>
</petclinic:layout>
