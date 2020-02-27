<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="jams">

    <h2>Jam Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${jam.name}"/></b></td>
        </tr>
        <tr>
            <th>Description</th>
            <td><c:out value="${jam.description}"/></td>
        </tr>
        <tr>
            <th>Difficulty</th>
            <td><c:out value="${jam.difficulty}"/></td>
        </tr>
        <tr>
            <th>Inscription deadline</th>
            <td><c:out value="${jam.inscriptionDeadline}"/></td>
        </tr>
        <tr>
            <th>Min. team members</th>
            <td><c:out value="${jam.minTeamSize}"/></td>
        </tr>
        <tr>
            <th>Max. team members</th>
            <td><c:out value="${jam.maxTeamSize}"/></td>
        </tr>
        <tr>
            <th>Min. teams</th>
            <td><c:out value="${jam.minTeams}"/></td>
        </tr>
        <tr>
            <th>Max. teams</th>
            <td><c:out value="${jam.maxTeams}"/></td>
        </tr>
        <tr>
            <th>Start date</th>
            <td><c:out value="${jam.start}"/></td>
        </tr>
        <tr>
            <th>End date</th>
            <td><c:out value="${jam.end}"/></td>
        </tr>
    </table>

    <spring:url value="{jamId}/edit" var="editUrl">
        <spring:param name="jamId" value="${jam.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Jam</a>

    <br/>
    <br/>
    <br/>
    <h2>Resources</h2>

    <table class="table table-striped">
        <c:forEach var="pet" items="${owner.pets}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt>Name</dt>
                        <dd><c:out value="${pet.name}"/></dd>
                        <dt>Birth Date</dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt>Type</dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                    </dl>
                </td>
                <td valign="top">
                    <table class="table-condensed">
                        <thead>
                        <tr>
                            <th>Visit Date</th>
                            <th>Description</th>
                        </tr>
                        </thead>
                        <c:forEach var="visit" items="${pet.visits}">
                            <tr>
                                <td><petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd"/></td>
                                <td><c:out value="${visit.description}"/></td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(petUrl)}">Edit Pet</a>
                            </td>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/visits/new" var="visitUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(visitUrl)}">Add Visit</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

        </c:forEach>
    </table>
    
    <spring:url value="{jamId}/resources/new" var="addResourceUrl">
        <spring:param name="jamId" value="${jam.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(addResourceUrl)}" class="btn btn-default">Add New Resource</a>

</petclinic:layout>
