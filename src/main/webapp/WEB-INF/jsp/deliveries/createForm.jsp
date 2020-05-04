<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="deliveries">
    <h2>
      	New Delivery
    </h2>
    <form:form modelAttribute="delivery" class="form-horizontal" id="add-delivery-form">
        <div class="form-group has-feedback">
            <petclinic:inputURLField label="Download URL" name="downloadURL"/>
 			<petclinic:inputField label="Description" name="description"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            
                        <button class="btn btn-default" type="submit">Add Delivery</button>
            </div>
        </div>
    </form:form>
</petclinic:layout>
