<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="jams">
	<h2>Publish Results</h2>
	<form:form class="form-horizontal" id="publish-results-form" modelAttribute="jam">
		<div class="form-group has-feedback">
			<div class="control-group">
				<petclinic:selectField name="winner.id" label="Winner" items="${jam.teams}" size="1" labels="name" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<button class="btn btn-default" type="submit">Publish</button>
			</div>
		</div>
	</form:form>

	<c:forEach var="team" items="${jam.teams}">
		<br />
		<b>${team.name} Marks (${ team.average }/10)</b>
		<table class="table table-striped">
			<tr>
				<th>Judge</th>
				<th>Mark</th>
				<th>Comments</th>
			</tr>
			<c:forEach var="mark" items="${team.marks}">
				<tr>
					<td><c:out value="${mark.judge.username}" /></td>
					<td><c:out value="${mark.value}" /></td>
					<td><c:out value="${mark.comments}" /></td>
				</tr>
			</c:forEach>
		</table>
	</c:forEach>
</petclinic:layout>
