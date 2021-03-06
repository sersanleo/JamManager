<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="invitations">
    <h2>
      	New Invitation
    </h2>
    <form:form modelAttribute="invitation" class="form-horizontal" id="add-invitation-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Username" name="to.username"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            
                        <button class="btn btn-default" type="submit">Send Invitation</button>
            </div>
        </div>
    </form:form>
</petclinic:layout>
