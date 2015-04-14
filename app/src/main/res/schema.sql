CREATE TABLE users(user_id INTEGER PRIMARY KEY, name TEXT, username TEXT UNIQUE, password TEXT);
INSERT INTO `users` VALUES (1,'John Smith','john@smith.com','abc123');
INSERT INTO `users` VALUES (2,'Johnny Ive','johnny@ive.com','abc123');
INSERT INTO `users` VALUES (3,'John Doe','john@doe.com','abc123');
CREATE TABLE bid(bid_id INTEGER PRIMARY KEY, object_id INTEGER, user_id INTEGER, bid_time TEXT, bid_amount TEXT, FOREIGN KEY(object_id) REFERENCES auction_items(object_id), FOREIGN KEY(user_id) REFERENCES users(user_id));
CREATE TABLE auction_items(object_id INTEGER PRIMARY KEY, user_id INTEGER, won_by_user_id INTEGER, expiry_date TEXT, item_name TEXT, item_description TEXT, price TEXT, image_filename TEXT, is_sold INTEGER, FOREIGN KEY(user_id) REFERENCES users(user_id));
INSERT INTO `auction_items` VALUES (1,1,0,'Thu Feb 26 08:45:35 GMT+05:00 2015','Antique Item','This is an antique item','25.5',NULL,0);
INSERT INTO `auction_items` VALUES (2,2,0,'Thu Feb 26 15:45:35 GMT+05:00 2015','Car Auction','This is a car','299.0',NULL,0);
INSERT INTO `auction_items` VALUES (3,3,0,'Thu Feb 26 17:45:35 GMT+05:00 2015','Jet Plane','This is a jet plane','1000.0',NULL,0);
INSERT INTO `auction_items` VALUES (4,1,0,'Fri Feb 27 04:45:35 GMT+05:00 2015','Aircraft Carrier','This is an aircraft carrier','29.9',NULL,0);
INSERT INTO `auction_items` VALUES (5,2,0,'Fri Feb 27 22:45:35 GMT+05:00 2015','Space rocket','This is a space rocket','599.0',NULL,0);