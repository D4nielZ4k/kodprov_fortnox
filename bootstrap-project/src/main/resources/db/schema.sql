DROP TABLE IF EXISTS cars;
DROP TABLE IF EXISTS rents;

CREATE TABLE IF NOT EXISTS cars(id BIGSERIAL PRIMARY KEY,name name, price decimal(18,2));
CREATE TABLE IF NOT EXISTS rents(id BIGSERIAL PRIMARY KEY,car_id int,driver_name name,driver_age int,start_time date, end_time date);
