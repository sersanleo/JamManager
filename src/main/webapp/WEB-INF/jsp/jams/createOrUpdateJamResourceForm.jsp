<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="jams">
    <jsp:body>
        <h2>
            <c:if test="${jamResource['new']}">New </c:if> Jam Resource
        </h2>
        <form:form modelAttribute="jamResource"
                   class="form-horizontal">
           
            
                <petclinic:inputField label="Description" name="description"/>
                <petclinic:inputField label="Download URL" name="downloadUrl"/>
                
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <c:choose>
                        <c:when test="${jamResource['new']}">
                            <button class="btn btn-default" type="submit">Add Jam Resource</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-default" type="submit">Update Jam Resource</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </form:form>
    </jsp:body>
</petclinic:layout>
