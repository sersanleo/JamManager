<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="jams">
    <h2>
        <c:if test="${team['new']}">New </c:if> Team
    </h2>
    <form:form modelAttribute="team" class="form-horizontal" id="add-team-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Name" name="name"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${team['new']}">
                        <button class="btn btn-default" type="submit">Add Team</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update Team</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
