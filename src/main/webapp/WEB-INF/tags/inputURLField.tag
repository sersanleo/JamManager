<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ attribute name="name" required="true" rtexprvalue="true"
              description="Name of corresponding property in bean object" %>
<%@ attribute name="label" required="true" rtexprvalue="true"
              description="Label appears in red color if input is considered as invalid after submission" %>

<petclinic:inputField label="${label}" name="${name}" placeholder="http://example.com"/>