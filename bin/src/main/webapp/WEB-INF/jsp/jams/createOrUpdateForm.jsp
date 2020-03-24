<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="jams">
    <h2>
        <c:if test="${jam['new']}">New </c:if> Jam 
    </h2>
    <form:form modelAttribute="jam" class="form-horizontal" id="add-jam-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Name" name="name"/>
            <petclinic:inputField label="Description" name="description"/>
            <petclinic:inputField label="Difficulty" name="difficulty"/>
            <petclinic:inputField label="Inscription deadline" name="inscriptionDeadline"/>
            <petclinic:inputField label="Max. team members" name="maxTeamSize"/>
            <petclinic:inputField label="Min. teams" name="minTeams"/>
            <petclinic:inputField label="Max. teams" name="maxTeams"/>
            <petclinic:inputField label="Start" name="start"/>
            <petclinic:inputField label="End" name="end"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${jam['new']}">
                        <button class="btn btn-default" type="submit">Add Jam</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update Jam</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
