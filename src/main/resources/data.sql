INSERT INTO users (id, email, password) VALUES ('1', 'dany@elice.com', '1234') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO users (id, email, password) VALUES ('2', 'heidi@elice.com', '1234') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO users (id, email, password) VALUES ('3', 'rookie@elice.com', '1234') ON DUPLICATE KEY UPDATE id=id;