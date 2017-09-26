DROP TABLE Account IF EXISTS;
DROP TABLE Customer IF EXISTS;

CREATE TABLE Customer(ID INTEGER PRIMARY KEY,FirstName VARCHAR(20),LastName VARCHAR(30),Street VARCHAR(50),City VARCHAR(25));
CREATE TABLE Account( ID INTEGER PRIMARY KEY,CustomerID INTEGER,Total DECIMAL, FOREIGN KEY (CustomerId) REFERENCES Customer(ID) ON DELETE CASCADE);

-- Contrainte d'intégrité : le montant d'un compte doit être positif ou nul
ALTER TABLE Account ADD CONSTRAINT PositiveBalance CHECK (Total >= 0.0);

INSERT INTO Customer VALUES(0,'Laura','Steel','429 Seventh Av.','Dallas');
INSERT INTO Customer VALUES(1,'Susanne','King','366 - 20th Ave.','Olten');

INSERT INTO Account VALUES(0, 0, 100.0); -- Le client #0 dispose de 100 €
INSERT INTO Account VALUES(1, 1,  50.0); -- Le client #1 dispose de  50 €

COMMIT;
