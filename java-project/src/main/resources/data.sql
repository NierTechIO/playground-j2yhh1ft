DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS (
    id INT AUTO_INCREMENT PRIMARY KEY,
    FIRST_NAME VARCHAR(250) NOT NULL,
    LAST_NAME VARCHAR(250) NOT NULL,
    AGE INT NOT NULL
);

INSERT INTO USERS (FIRST_NAME, LAST_NAME, AGE)
VALUES ('Nier', 'Wang', 29),
       ('WL', 'Change', 20),
       ('SJ', 'Pig', 18);