create table webflux.user (
  id serial PRIMARY KEY,
  name varchar(255) NOT NULL,
  username varchar(255) NOT NULL,
  password varchar(150) NOT NULL,
  authorities varchar(100) NOT NULL
 );