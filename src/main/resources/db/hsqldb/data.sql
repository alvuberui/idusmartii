INSERT INTO users(username,password,enabled) VALUES ('admin1','admin',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');

INSERT INTO users(username, password, enabled) VALUES ('alvaro_ubeda8', 'asdfg', TRUE);
INSERT INTO authorities(id,username,authority) VALUES (4,'alvaro_ubeda8','player');

INSERT INTO users(username, password, enabled) VALUES ('juagomram4', '12345', TRUE);
INSERT INTO authorities(id,username,authority) VALUES (5,'juagomram4','player');

INSERT INTO users(username, password, enabled) VALUES ('cargarrod12', '123456', TRUE);
INSERT INTO authorities(id,username,authority) VALUES (6,'cargarrod12','player');

INSERT INTO users(username, password, enabled) VALUES ('marpercor8', '123456', TRUE);
INSERT INTO authorities(id,username,authority) VALUES (7,'marpercor8','player');

INSERT INTO users(username, password, enabled) VALUES ('anglorcas', '123456', TRUE);
INSERT INTO authorities(id,username,authority) VALUES (8,'anglorcas','player');

INSERT INTO users(username, password, enabled) VALUES ('p1', 'asdfg', TRUE);
INSERT INTO authorities(id,username,authority) VALUES (9,'p1','player');

INSERT INTO users(username, password, enabled) VALUES ('p2', '12345', TRUE);
INSERT INTO authorities(id,username,authority) VALUES (10,'p2','player');

INSERT INTO users(username, password, enabled) VALUES ('p3', '123456', TRUE);
INSERT INTO authorities(id,username,authority) VALUES (11,'p3','player');

INSERT INTO users(username, password, enabled) VALUES ('p4', '123456', TRUE);
INSERT INTO authorities(id,username,authority) VALUES (12,'p4','player');

INSERT INTO users(username, password, enabled) VALUES ('p5', '123456', TRUE);
INSERT INTO authorities(id,username,authority) VALUES (13,'p5','player');

INSERT INTO users(username, password, enabled) VALUES ('p6', 'asdfg', TRUE);
INSERT INTO authorities(id,username,authority) VALUES (14,'p6','player');
	


INSERT INTO board(id, num_players) VALUES (1,5);
INSERT INTO board(id, num_players) VALUES (2,5);


INSERT INTO Match(id,start_match, winner, num_players, match_status, board_id, votos_leal, votos_traidor)
	VALUES(1, '2021-10-28T11:44:44.797', 1, 5, 2,2,13,4);
INSERT INTO Match(id,start_match, winner, num_players, match_status, board_id, votos_leal, votos_traidor)
	VALUES(2, '2021-12-28T11:44:44.797', null, 5, 0,2,0,0);

INSERT INTO player(id, first_name, last_name, email, username, board_id, match_id)
	VALUES (1, 'Mario','Perez','marpercor8@email.com', 'marpercor8',1, null); -- 40
INSERT INTO player(id, first_name, last_name, email, username, board_id, match_id)
	VALUES (2,'Juan Carlos','JK','jk@email.com', 'juagomram4',1, null); -- 11
INSERT INTO player(id, first_name, last_name, email, username, board_id, match_id) 
	VALUES (3,'Carlos','Garro','carlos@garro.es','cargarrod12',1,null); -- 12
INSERT INTO player(id, first_name, last_name, email, username, board_id, match_id)
	VALUES (4,'Alvaro','Ubeda','ubeda@email.com','alvaro_ubeda8',1,null); -- 13
INSERT INTO player(id, first_name, last_name, email, username, board_id, match_id)
	VALUES (5,'Angel','Lozano','lozano@gmail.com','anglorcas', 1, null); -- 14
INSERT INTO player(id, first_name, last_name, email, username, board_id, match_id)
	VALUES (6, 'p1','prueba1','p1@email.com', 'p1',2, 2);
INSERT INTO player(id, first_name, last_name, email, username, board_id, match_id)
	VALUES (7,'p2','prueba2','p2@email.com', 'p2',2, 2);
INSERT INTO player(id, first_name, last_name, email, username, board_id, match_id) 
	VALUES (8,'p3','prueba3','p3@garro.es','p3',2,2);
INSERT INTO player(id, first_name, last_name, email, username, board_id, match_id)
	VALUES (9,'p4','prueba4','p4@email.com','p4',2,2);
INSERT INTO player(id, first_name, last_name, email, username, board_id, match_id)
	VALUES (10,'p5','prueba5','p5@gmail.com','p5', 2, 2);	

INSERT INTO MATCHS_PLAYED (match_Id, player_Id) VALUES (1,6);
INSERT INTO MATCHS_PLAYED (match_Id, player_Id) VALUES (1,7);
INSERT INTO MATCHS_PLAYED (match_Id, player_Id) VALUES (1,8);
INSERT INTO MATCHS_PLAYED (match_Id, player_Id) VALUES (1,9);
INSERT INTO MATCHS_PLAYED (match_Id, player_Id) VALUES (1,10);

UPDATE board set anfitrion_id=4 where id = 1;
UPDATE board set anfitrion_id=6 where id = 2;

INSERT INTO comment(comment, date, player_id, match_id) 
	VALUES('Buena partida chavales, la he visto como espectador', '2021-10-28T11:58:44.797', 1, 2);
	

INSERT INTO friends(id, player1, player2, friend_state)
    VALUES (1,2,3,0);
INSERT INTO friends(id, player1, player2, friend_state)
    VALUES (2,3,4,1);
INSERT INTO friends(id, player1, player2, friend_state)
    VALUES (3,4,5,1);
INSERT INTO friends(id, player1, player2, friend_state)
    VALUES (4,2,5,0);


INSERT INTO estadistics(id,matchs_wins,matchs_loses,matchs_played,points,ranking_pos,wins_longer_strike,actual_win_strike,match_longer_duration,match_shorter_duration)
	VALUES(1,12,3,15,50,null,1,2,5500,3500);
INSERT INTO estadistics(id,matchs_wins,matchs_loses,matchs_played,points,ranking_pos,wins_longer_strike,actual_win_strike,match_longer_duration,match_shorter_duration)
	VALUES(2,3,20,23,3,null,4,2,5000,4000);
INSERT INTO estadistics(id,matchs_wins,matchs_loses,matchs_played,points,ranking_pos,wins_longer_strike,actual_win_strike,match_longer_duration,match_shorter_duration)
	VALUES(3,4,12,16,8,null,3,2,4500,4500);
INSERT INTO estadistics(id,matchs_wins,matchs_loses,matchs_played,points,ranking_pos,wins_longer_strike,actual_win_strike,match_longer_duration,match_shorter_duration)
	VALUES(4,8,3,11,12,null,2,2,5000,4000);
INSERT INTO estadistics(id,matchs_wins,matchs_loses,matchs_played,points,ranking_pos,wins_longer_strike,actual_win_strike,match_longer_duration,match_shorter_duration)
	VALUES(5,9,1,10,1,null,5,2,6001,3500);

UPDATE player set estadistic_id=1 where id = 1;
UPDATE player set estadistic_id=2 where id = 2;
UPDATE player set estadistic_id=3 where id = 3;
UPDATE player set estadistic_id=4 where id = 4;
UPDATE player set estadistic_id=5 where id = 5;



insert into cards VALUES (100, 1, false, 1, 10);

insert into consul VALUES (1, 10, 100);



INSERT INTO achivement(id,title,description,conditions,aplicable_entity,quantity) VALUES (1,'Starting','Play 1 games',0,3,1);
INSERT INTO achivement(id,title,description,conditions,aplicable_entity,quantity) VALUES (2,'Starting II','Play 5 games',0,3,5);
INSERT INTO achivement(id,title,description,conditions,aplicable_entity,quantity) VALUES (3,'Top Tier','Be on the top ten in global ranking',1,2,10);
