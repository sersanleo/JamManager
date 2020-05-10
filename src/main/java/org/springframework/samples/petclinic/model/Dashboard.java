  
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "dashboard")
public class Dashboard extends BaseEntity {
	
	
//	Integer				totalNumberOfJams;
//	Integer				totalNumberOfJamsMonth;
//	Integer				totalNumberOfTeamsMonth;
	
//	
//	Double				ratioInscriptionJams;
//	Double				ratioPendingJams;
//	Double				ratioInProgressJams;
//	Double				ratioRatingJams;
//	
//	Double				ratioFinishedJams;
//	Double				ratioCancelledJams;
	
	
//	Double				ratioNotPuntuedTeam;
//	Double				ratioPuntuadTeam;
	
	
	
	
//	Integer						totalNumberOfAnnouncements;
//	Integer						totalNumberOfCompanyRecords;
//	Integer						totalNumberOfInvestorRecords;
//
//	Double						maxRewardOfActiveRequests;
//	Double						minRewardOfActiveRequests;
//	Double						avgRewardOfActiveRequests;
//	Double						standardDeviationRewardOfActiveRequests;
//
//	Double						maxRewardOfActiveOffers;
//	Double						minRewardOfActiveOffers;
//	Double						avgRewardOfActiveOffers;
//	Double						standardDeviationRewardOfActiveOffers;
//
//	Double						avgJobsPerEmployer;
//	Double						avgApplicationsPerEmployer;
//	Double						avgApplicationsPerWorker;
//
//	Object[]					investorsCommonSectors;
//	Object[]					companiesCommonSectors;
//	Object[]					companySectors;
//	Object[]					investorSectors;
//
//	Double						ratioOfPublishedJobs;
//	Double						ratioOfDraftJobs;
//	Double						ratioOfPendingApplications;
//	Double						ratioOfAcceptedApplications;
//	Double						ratioOfRejectedApplications;
//
//	Object[]					pendingApplicationsPerDay;
//	Object[]					acceptedApplicationsPerDay;
//	Object[]					rejectedApplicationsPerDay;
}
