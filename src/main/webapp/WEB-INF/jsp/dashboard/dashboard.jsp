
<%@ page session="false" trimDirectiveWhitespaces="true" import="org.springframework.samples.petclinic.model.JamStatus"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<sec:authorize access="hasAuthority('jamOrganizator')">
	<c:set var="isOrganizator" value="true" />
</sec:authorize>


<petclinic:layout pageName="dashboard">
	<script src=/resources/js/Chart.js " type="text/javascript"></script>

	<h2>Jams:</h2>
	<b>Active jams grouped by status:</b>
	<canvas id="jamsStatus"></canvas>
	<script>
		var ctx = document.getElementById('jamsStatus').getContext('2d');
		var myChart = new Chart(ctx, {
			type : "pie",
			data : {
				labels : [
						'Inscription', 'Pending', 'In progress', 'Rating'
				],
				datasets : [
					{
						label : '# of Votes',
						data : [
							${ dashboard.jamsInscription }, ${ dashboard.jamsPending }, ${ dashboard.jamsInProgress }, ${ dashboard.jamsRating }
						],
						backgroundColor : [
								'rgba(255, 99, 132, 0.2)', 'rgba(54, 162, 235, 0.2)', 'rgba(255, 206, 86, 0.2)', 'rgba(75, 192, 192, 0.2)'
						],
						borderColor : [
								'rgba(255, 99, 132, 1)', 'rgba(54, 162, 235, 1)', 'rgba(255, 206, 86, 1)', 'rgba(75, 192, 192, 1)'
						],
						borderWidth : 1
					}
				]
			},
			options : {
				legend : {
					display : true
				}
			}
		});
	</script>
	<br>
	<b>Active jams capacity:</b>
	<canvas id="jamsCapacity"></canvas>
	<script>
		var ctx = document.getElementById('jamsCapacity').getContext('2d');
		var myChart = new Chart(ctx, {
			type : "bar",
			data: {
                labels: [ <c:forEach var='jam' items='${ dashboard.activeJams }'>'${ jam.name }',</c:forEach>],
                datasets: [
                	{
                        label: [ 'Current teams count' ],
                        data: [<c:forEach var='jam' items='${ dashboard.activeJams }'>${ jam.teams.size() },</c:forEach>],
                        backgroundColor: "rgb(150,0,0)",
                    },
                	{
                        label: [ 'Remaining' ],
                        data: [<c:forEach var='jam' items='${ dashboard.activeJams }'>${ jam.maxTeams-jam.teams.size() },</c:forEach>],
                        backgroundColor: "green",
                    }
                ]
            },
            options: {
                scales: {
                    xAxes: [{
                        stacked: true
                    }],
                    yAxes: [{
                        stacked: true,
                        ticks : {
							stepSize: 1
						}
                    }]
                }
            }
		});
	</script>
	<br>
	<br>

	<b>Winners:</b>
	<table id="winnersTable" class="table table-striped">
		<thead>
			<tr>
				<th>Username</th>
				<th>Times he/she won</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${dashboard.winners}" var="winner">
				<tr>
					<td><c:out value="${winner.key}" /></td>
					<td><c:out value="${winner.value}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<br>
	<br>
</petclinic:layout>