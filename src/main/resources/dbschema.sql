CREATE TABLE IF NOT EXISTS rx_users 
(
	id BIGINT AUTO_INCREMENT, 
	name VARCHAR(255),
	balance DECIMAL(9,2), 
	PRIMARY KEY (id)
);