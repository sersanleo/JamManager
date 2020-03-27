INSERT INTO users(username,password,enabled,email,phone_country_code,phone_area_code,phone_number) VALUES ('jamOrganizator1','jamOrganizator1',TRUE,'example@example.com',34,'','600 000 000');
INSERT INTO authorities(username,authority) VALUES ('jamOrganizator1','jamOrganizator');
INSERT INTO users(username,password,enabled,email,phone_country_code,phone_area_code,phone_number) VALUES ('judge1','judge1',TRUE,'example@example.com',34,'','600 000 000');
INSERT INTO authorities(username,authority) VALUES ('judge1','judge');
INSERT INTO users(username,password,enabled,email,phone_country_code,phone_area_code,phone_number) VALUES ('member1','member1',TRUE,'example@example.com',34,'','600 000 000');
INSERT INTO authorities(username,authority) VALUES ('member1','member');
INSERT INTO users(username,password,enabled,email,phone_country_code,phone_area_code,phone_number) VALUES ('member2','member2',TRUE,'example@example.com',34,'','600 000 000');
INSERT INTO authorities(username,authority) VALUES ('member2','member');
INSERT INTO users(username,password,enabled,email,phone_country_code,phone_area_code,phone_number) VALUES ('member3','member3',TRUE,'example@example.com',34,'','600 000 000');
INSERT INTO authorities(username,authority) VALUES ('member3','member');
INSERT INTO users(username,password,enabled,email,phone_country_code,phone_area_code,phone_number) VALUES ('member4','member4',TRUE,'example@example.com',34,'','600 000 000');
INSERT INTO authorities(username,authority) VALUES ('member4','member');

INSERT INTO vets VALUES (1, 'James', 'Carter');
INSERT INTO vets VALUES (2, 'Helen', 'Leary');
INSERT INTO vets VALUES (3, 'Linda', 'Douglas');
INSERT INTO vets VALUES (4, 'Rafael', 'Ortega');
INSERT INTO vets VALUES (5, 'Henry', 'Stevens');
INSERT INTO vets VALUES (6, 'Sharon', 'Jenkins');

INSERT INTO specialties VALUES (1, 'radiology');
INSERT INTO specialties VALUES (2, 'surgery');
INSERT INTO specialties VALUES (3, 'dentistry');

INSERT INTO vet_specialties VALUES (2, 1);
INSERT INTO vet_specialties VALUES (3, 2);
INSERT INTO vet_specialties VALUES (3, 3);
INSERT INTO vet_specialties VALUES (4, 2);
INSERT INTO vet_specialties VALUES (5, 1);

INSERT INTO types VALUES (1, 'cat');
INSERT INTO types VALUES (2, 'dog');
INSERT INTO types VALUES (3, 'lizard');
INSERT INTO types VALUES (4, 'snake');
INSERT INTO types VALUES (5, 'bird');
INSERT INTO types VALUES (6, 'hamster');

/* Jam 1 */
INSERT INTO jams(id,name,description,difficulty,inscription_deadline,max_team_size,min_teams,max_teams,start,end,rated,creator_username)
VALUES 			(1,'Jam 1','prueba',2,'2020-06-08 12:00',5,5,10,'2020-06-09 12:00','2020-06-10 12:00',FALSE,'jamOrganizator1');
INSERT INTO jam_resource(id,description,download_url,jams_id)
VALUES 			(1,'Jam 1','https://www.youtube.com/',1);
/* Jam 1 Team 1 */
INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(1,'Grupo 1','2020-06-08 12:00',1);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member1',1);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member2',1);

/* Jam 2 */
INSERT INTO jams(id,name,description,difficulty,inscription_deadline,max_team_size,min_teams,max_teams,start,end,rated,creator_username)
VALUES 			(2,'Jam 2','prueba2',3,'2020-06-08 12:00',5,1,2,'2020-06-09 12:00','2020-06-10 12:00',FALSE,'jamOrganizator1');

/* Jam 3 */
INSERT INTO jams(id,name,description,difficulty,inscription_deadline,max_team_size,min_teams,max_teams,start,end,rated,creator_username)
VALUES 			(3,'Jam 3','prueba3',1,'2020-01-08 16:00',5,1,2,'2020-06-09 12:00','2020-06-10 12:00',FALSE,'jamOrganizator1');

/* Jam 3 Team 1 */
INSERT INTO teams(id,name,creation_date,jam_id)
VALUES 			(2,'Grupo 2','2020-02-08 12:00',3);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member3',2);
INSERT INTO teams_members(members_username,team_id)
VALUES 			('member4',2);
