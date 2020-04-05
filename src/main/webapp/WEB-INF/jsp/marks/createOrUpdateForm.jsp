<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="jams">
    <h2>
        <c:if test="${mark['new']}">New </c:if> mark 
    </h2>
    <form:form modelAttribute="mark" class="form-horizontal" id="add-mark-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Mark" name="mark" placeholder="0-10"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${mark['new']}">
                        <button class="btn btn-default" type="submit">Add mark</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update mark</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
