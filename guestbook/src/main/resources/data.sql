DROP TABLE IF EXISTS USERS;
  
CREATE TABLE USERS(
  id INT AUTO_INCREMENT  PRIMARY KEY,
  user_name VARCHAR(250) NOT NULL,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  email VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL,
  role VARCHAR(250) NOT NULL
   
);

DROP TABLE IF EXISTS GUEST_ENTRY;
  
CREATE TABLE GUEST_ENTRY(
  id INT AUTO_INCREMENT  PRIMARY KEY,
  text_entry VARCHAR(250) NULL,
  file_name VARCHAR(250) NULL,
  image_entry LONGBLOB NULL,
  CREATED_BY VARCHAR(250) NOT NULL,
  created_datetime TIMESTAMP NOT NULL,
  updated_datetime TIMESTAMP NULL,
  updated_by VARCHAR(250) NULL,
  status VARCHAR(50) NULL
   
);

INSERT INTO USERS(id,user_name, first_name, last_name, email, password, role) VALUES
 (1,'guest', 'Guest', 'Guest','guest@gmail.com','$2a$10$oMtqtutQPlRMXWtmL1FLpeobNE2jMkMleqURl/CMpdx8gdhVcx5b6','ROLE_GUEST');
 INSERT INTO USERS(id,user_name, first_name, last_name, email, password, role) VALUES
 (2,'admin', 'Admin', 'Admin','admin@gmail.com','$2a$10$vgKPltX/9pZAxvrT9HACPuV9epz/mmnYsj9M/ww8CibowPGbsAlAO','ROLE_ADMIN');
   
INSERT INTO GUEST_ENTRY(id, text_entry, status, CREATED_BY, created_datetime) VALUES
 (1, 'First Entry', 'Pending', 'guest', CURRENT_TIMESTAMP);
 INSERT INTO GUEST_ENTRY(id, text_entry, status, CREATED_BY, created_datetime) VALUES
 (2, 'Second Entry','Pending', 'guest', CURRENT_TIMESTAMP);

