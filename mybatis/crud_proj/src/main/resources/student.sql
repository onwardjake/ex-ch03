CREATE DATABASE testdb;
USE testdb;


CREATE TABLE student(
    id		BIGINT		AUTO_INCREMENT PRIMARY KEY,
    name		VARCHAR(30) NOT NULL,
    email		VARCHAR(30),
    age		INT,
    created_at	TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at	TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

SELECT * FROM student;

INSERT INTO student(name, email, age) VALUES
('이정재', 'ljj@example.com', 45),
('홍길동', 'hgd@example.com', 21),
('이영희', 'lyh@example.com', 33);