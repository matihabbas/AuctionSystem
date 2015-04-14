CREATE TABLE users(user_id INTEGER PRIMARY KEY, name TEXT, username TEXT UNIQUE, password TEXT);
INSERT INTO users VALUES (1,'Test User','a@b.com','abc');
INSERT INTO users VALUES (2,'John Smith','john@smith.com','abc123');
INSERT INTO users VALUES (3,'Johnny Ive','johnny@ive.com','abc123');
INSERT INTO users VALUES (4,'John Doe','john@doe.com','abc123');
CREATE TABLE bid(bid_id INTEGER PRIMARY KEY, object_id INTEGER, user_id INTEGER, bid_time TEXT, bid_amount TEXT, FOREIGN KEY(object_id) REFERENCES auction_items(object_id), FOREIGN KEY(user_id) REFERENCES users(user_id));
CREATE TABLE auction_items(object_id INTEGER PRIMARY KEY, user_id INTEGER, won_by_user_id INTEGER, expiry_date TEXT, item_name TEXT, item_description TEXT, price TEXT, image_filename TEXT, is_sold INTEGER, FOREIGN KEY(user_id) REFERENCES users(user_id));
INSERT INTO auction_items VALUES (1,1,0,'Fri Feb 27 08:45:35 GMT+05:00 2015','Antique Item','This is an antique item','25.5',NULL,0);
INSERT INTO auction_items VALUES (2,2,0,'Sat Feb 28 15:45:35 GMT+05:00 2015','Car Auction','This is a car','299.0',NULL,0);
INSERT INTO auction_items VALUES (3,3,0,'Sun Mar 01 17:45:35 GMT+05:00 2015','Jet Plane','This is a jet plane','1000.0',NULL,0);
INSERT INTO auction_items VALUES (4,1,0,'Mon Mar 02 04:45:35 GMT+05:00 2015','Aircraft Carrier','This is an aircraft carrier','29.9',NULL,0);
INSERT INTO auction_items VALUES (5,2,0,'Tue Mar 03 22:45:35 GMT+05:00 2015','Space rocket','This is a space rocket','599.0',NULL,0);
INSERT INTO auction_items VALUES (6,3,0,'Sun Mar 01 17:45:35 GMT+05:00 2015','Starship Enterprise','This is the Starship Enterprise','1000.0',NULL,0);
INSERT INTO auction_items VALUES (7,1,0,'Mon Mar 02 04:45:35 GMT+05:00 2015','Death Star','This is the Death Star','29.9',NULL,0);
INSERT INTO auction_items VALUES (8,2,0,'Tue Mar 03 22:45:35 GMT+05:00 2015','Planet','This is a Planet','599.0',NULL,0);
INSERT INTO auction_items VALUES (9,2,0,'Wed Mar 04 22:45:35 GMT+05:00 2015','Universe','This is the Universe','1024.0',NULL,0);
INSERT INTO auction_items VALUES (10,3,0,'Thu Mar 05 22:45:35 GMT+05:00 2015','Atom','This is an atom','3.0',NULL,0);
INSERT INTO auction_items VALUES (11,1,0,'Fri Mar 06 22:45:35 GMT+05:00 2015','Neutron','This is a neutron','2.0',NULL,0);
INSERT INTO auction_items VALUES (12,2,0,'Sat Mar 07 22:45:35 GMT+05:00 2015','Electron','This is an Electron','1.0',NULL,0);