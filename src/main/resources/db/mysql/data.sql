INSERT INTO users(username,password,enabled,email,phone_country_code,phone_area_code,phone_number) VALUES ('jamOrganizator1','jamOrganizator1',TRUE,'example@example.com',34,'','600 000 000');
INSERT INTO authorities(username,authority) VALUES ('jamOrganizator1','jamOrganizator');
INSERT INTO users(username,password,enabled,email,phone_country_code,phone_area_code,phone_number) VALUES ('judge1','judge1',TRUE,'example@example.com',34,'','600 000 000');
INSERT INTO authorities(username,authority) VALUES ('judge1','judge');
INSERT INTO users(username,password,enabled,email,phone_country_code,phone_area_code,phone_number) VALUES ('judge2','judge2',TRUE,'example@example.com',34,'','600 000 000');
INSERT INTO authorities(username,authority) VALUES ('judge2','judge');
INSERT INTO users(username,password,enabled,email,phone_country_code,phone_area_code,phone_number) VALUES ('member1','member1',TRUE,'example@example.com',34,'','600 000 000');
INSERT INTO authorities(username,authority) VALUES ('member1','member');
INSERT INTO users(username,password,enabled,email,phone_country_code,phone_area_code,phone_number) VALUES ('member2','member2',TRUE,'example@example.com',34,'','600 000 000');
INSERT INTO authorities(username,authority) VALUES ('member2','member');
INSERT INTO users(username,password,enabled,email,phone_country_code,phone_area_code,phone_number) VALUES ('member3','member3',TRUE,'example@example.com',34,'','600 000 000');
INSERT INTO authorities(username,authority) VALUES ('member3','member');
INSERT INTO users(username,password,enabled,email,phone_country_code,phone_area_code,phone_number) VALUES ('member4','member4',TRUE,'example@example.com',34,'','600 000 000');
INSERT INTO authorities(username,authority) VALUES ('member4','member');
INSERT INTO users(username,password,enabled,email,phone_country_code,phone_area_code,phone_number) VALUES ('member5','member5',TRUE,'example@example.com',34,'','600 000 000');
INSERT INTO authorities(username,authority) VALUES ('member5','member');

/* Inscription Jam */
INSERT INTO jams(id,name,description,difficulty,inscription_deadline,max_team_size,min_teams,max_teams,start,end,rated,creator_username)
VALUES 			(1,'Inscription Jam','test',1,'2025-06-08 12:00',4,2,3,'2025-06-09 12:00','2025-06-10 12:00',FALSE,'jamOrganizator1');
INSERT INTO jam_resource(id,description,download_url,jams_id)
VALUES 			(1,'Test Resource 1','https://www.youtube.com/',1);
/* Teams */
INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(1,'Grupo 1','2020-03-08 12:00',1);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member2',1);
INSERT INTO invitations(id,creation_date,status,team_id,user_username)
VALUES          (1,'2020-03-08 12:00',0,1,'member1');
INSERT INTO invitations(id,creation_date,status,team_id,user_username)
VALUES          (2,'2020-03-08 12:00',0,1,'member4');
INSERT INTO invitations(id, creation_date, status, team_id, user_username)
VALUES 			(3,'2020-03-08 12:00',2,1, 'member3');
INSERT INTO invitations(id, creation_date, status, team_id, user_username)
VALUES 			(4,'2020-03-08 12:00',2,1, 'member4');

INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(12,'Grupo 2','2020-03-08 12:00',1);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member3',12);
INSERT INTO invitations(id,creation_date,status,team_id,user_username)
VALUES          (5,'2020-03-08 12:00',0,12,'member1');
INSERT INTO invitations(id, creation_date, status, team_id, user_username)
VALUES 			(6,'2020-03-08 12:00',0,12, 'member4');

/* Pending Jam */
INSERT INTO jams(id,name,description,difficulty,inscription_deadline,max_team_size,min_teams,max_teams,start,end,rated,creator_username)
VALUES 			(2,'Pending Jam','test',2,'2019-06-08 12:00',5,2,2,'2025-06-09 12:00','2025-06-10 12:00',FALSE,'jamOrganizator1');
INSERT INTO jam_resource(id,description,download_url,jams_id)
VALUES 			(2,'Test Resource 1','https://www.youtube.com/',2);
/* Teams */
INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(2,'Grupo 1','2019-03-08 12:00',2);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member1',2);
INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(3,'Grupo 2','2019-03-08 12:00',2);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member2',3);
INSERT INTO invitations(id,creation_date,status,team_id,user_username)
VALUES          (7,'2020-03-08 12:00',0,3,'member1');

/* In Progress Jam */
INSERT INTO jams(id,name,description,difficulty,inscription_deadline,max_team_size,min_teams,max_teams,start,end,rated,creator_username)
VALUES 			(3,'In Progress Jam','test',3,'2019-06-08 12:00',5,2,2,'2019-06-09 12:00','2025-06-10 12:00',FALSE,'jamOrganizator1');
INSERT INTO jam_resource(id,description,download_url,jams_id)
VALUES 			(3,'Test Resource 1','https://www.youtube.com/',3);
/* Teams */
INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(4,'Grupo 1','2019-03-08 12:00',3);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member1',4);
INSERT INTO deliveries(id, description, download_url,creation_date,team_id)
VALUES 			(1,'Primer envio en Progress','https://www.youtube.com/','2020-03-26 12:00',4);
INSERT INTO deliveries(id,description, download_url,creation_date,team_id)
VALUES 			(2,'Segundo envio en Progress','https://www.us.es/','2020-03-26 13:00',4);

INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(5,'Grupo 2','2019-03-08 12:00',3);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member2',5);

/* Rating Jam ready to publish */
INSERT INTO jams(id,name,description,difficulty,inscription_deadline,max_team_size,min_teams,max_teams,start,end,rated,creator_username)
VALUES 			(4,'Rating Jam','test',4,'2019-06-08 12:00',5,2,2,'2019-06-09 12:00','2019-06-10 12:00',FALSE,'jamOrganizator1');
INSERT INTO jam_resource(id,description,download_url,jams_id)
VALUES 			(4,'Test Resource 1','https://www.youtube.com/',4);
/* Teams */
INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(6,'Grupo 1','2019-03-08 12:00',4);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member1',6);
INSERT INTO deliveries(id,description, download_url,creation_date,team_id)
VALUES 			(3,'Primer envio en Rating grupo 1','https://www.us.es/','2020-03-26 13:00',6);

INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(7,'Grupo 2','2019-03-08 12:00',4);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member2',7);
INSERT INTO deliveries(id,description, download_url,creation_date,team_id)
VALUES 			(4,'Segundo envio en Rating grupo 2','https://www.us.es/','2020-03-26 13:00',7);
/* Marks */
INSERT INTO marks(value,comments,judge_username,team_id)
VALUES 			(4,'Not very good','judge1',6);
INSERT INTO marks(value,comments,judge_username,team_id)
VALUES 			(7.5,'Good','judge2',7);

/* Finished Jam */
INSERT INTO jams(id,name,description,difficulty,inscription_deadline,max_team_size,min_teams,max_teams,start,end,rated,creator_username)
VALUES 			(5,'Finished Jam','test',5,'2019-06-08 12:00',5,2,2,'2019-06-09 12:00','2019-06-10 12:00',FALSE,'jamOrganizator1');
INSERT INTO jam_resource(id,description,download_url,jams_id)
VALUES 			(5,'Test Resource 1','https://www.youtube.com/',5);
/* Teams */
INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(8,'Grupo 1','2019-03-08 12:00',5);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member1',8);
INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(9,'Grupo 2','2019-03-08 12:00',5);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member2',9);
/* Marks */
INSERT INTO marks(value,comments,judge_username,team_id)
VALUES 			(5,'Not very good','judge1',8);
INSERT INTO marks(value,comments,judge_username,team_id)
VALUES 			(7,'You could have done more','judge2',8);
INSERT INTO marks(value,comments,judge_username,team_id)
VALUES 			(9.5,'Very good','judge1',9);
INSERT INTO marks(value,comments,judge_username,team_id)
VALUES 			(10,'Perfect!','judge2',9);
/* Winner */
UPDATE jams SET rated = TRUE, winner_id = 9 WHERE id = 5;

/* Cancelled Jam */
INSERT INTO jams(id,name,description,difficulty,inscription_deadline,max_team_size,min_teams,max_teams,start,end,rated,creator_username)
VALUES 			(6,'Cancelled Jam','test',1,'2019-06-08 12:00',5,2,2,'2019-06-09 12:00','2025-06-10 12:00',FALSE,'jamOrganizator1');
INSERT INTO jam_resource(id,description,download_url,jams_id)
VALUES 			(6,'Test Resource 1','https://www.youtube.com/',6);

/* Full Jam */
INSERT INTO jams(id,name,description,difficulty,inscription_deadline,max_team_size,min_teams,max_teams,start,end,rated,creator_username)
VALUES 			(7,'Full Jam','test',2,'2025-06-08 12:00',5,2,2,'2025-06-09 12:00','2025-06-10 12:00',FALSE,'jamOrganizator1');
INSERT INTO jam_resource(id,description,download_url,jams_id)
VALUES 			(7,'Test Resource 1','https://www.youtube.com/',7);
/* Teams */
INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(10,'Grupo 1','2020-03-08 12:00',7);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member1',10);
INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(11,'Grupo 2','2020-03-08 12:00',7);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member2',11);

/* Rating Jam (every team must have one mark at least) */
INSERT INTO jams(id,name,description,difficulty,inscription_deadline,max_team_size,min_teams,max_teams,start,end,rated,creator_username)
VALUES 			(8,'Rating Jam (every team must have one mark at least)','test',4,'2019-06-08 12:00',5,2,2,'2019-06-09 12:00','2019-06-10 12:00',FALSE,'jamOrganizator1');
INSERT INTO jam_resource(id,description,download_url,jams_id)
VALUES 			(8,'Test Resource 1','https://www.youtube.com/',8);
/* Teams */
INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(13,'Grupo 1','2019-03-08 12:00',8);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member1',13);
INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(14,'Grupo 2','2019-03-08 12:00',8);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member2',14);
/* Marks */
INSERT INTO marks(value,comments,judge_username,team_id)
VALUES 			(4,'Not very good','judge1',13);

/* Rating Jam (every team needs the same number of marks) */
INSERT INTO jams(id,name,description,difficulty,inscription_deadline,max_team_size,min_teams,max_teams,start,end,rated,creator_username)
VALUES 			(9,'Rating Jam (every team needs the same number of marks)','test',4,'2019-06-08 12:00',5,2,2,'2019-06-09 12:00','2019-06-10 12:00',FALSE,'jamOrganizator1');
INSERT INTO jam_resource(id,description,download_url,jams_id)
VALUES 			(9,'Test Resource 1','https://www.youtube.com/',9);
/* Teams */
INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(15,'Grupo 1','2019-03-08 12:00',9);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member1',15);
INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(16,'Grupo 2','2019-03-08 12:00',9);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member2',16);
/* Marks */
INSERT INTO marks(value,comments,judge_username,team_id)
VALUES 			(4,'Not very good','judge1',15);
INSERT INTO marks(value,comments,judge_username,team_id)
VALUES 			(1,'Bad','judge2',15);
INSERT INTO marks(value,comments,judge_username,team_id)
VALUES 			(4,'Not very good','judge1',16);

/* Inscription Jam with a full team */
INSERT INTO jams(id,name,description,difficulty,inscription_deadline,max_team_size,min_teams,max_teams,start,end,rated,creator_username)
VALUES 			(10,'Inscription Jam (with a full team)','test',1,'2025-06-08 12:00',2,2,3,'2025-06-09 12:00','2025-06-10 12:00',FALSE,'jamOrganizator1');
INSERT INTO jam_resource(id,description,download_url,jams_id)
VALUES 			(10,'Test Resource 1','https://www.youtube.com/',10);
/* Teams */
INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(17,'Grupo 1','2020-03-08 12:00',10);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member2',17);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member3',17);