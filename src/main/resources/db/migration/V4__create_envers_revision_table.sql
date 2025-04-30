-- create the sequence Envers will use
CREATE SEQUENCE revinfo_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;

-- create the revision table
CREATE TABLE revinfo (
  rev       INTEGER NOT NULL PRIMARY KEY,
  revtstmp  BIGINT
);

-- make the rev column auto‐increment via our new sequence
ALTER TABLE revinfo 
  ALTER COLUMN rev 
  SET DEFAULT nextval('revinfo_seq');
