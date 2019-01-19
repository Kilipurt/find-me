CREATE TABLE USERS (
    ID NUMBER PRIMARY KEY,
    FIRST_NAME NVARCHAR2(50),
    LAST_NAME NVARCHAR2(50),
    PHONE NVARCHAR2(15),
    COUNTRY NVARCHAR2(50),
    CITY NVARCHAR2(50),
    AGE NUMBER(3),
    DATE_REGISTERED DATE,
    DATE_LAST_ACTIVE DATE,
    RELATIONSHIP_STATUS NVARCHAR2(50),
    RELIGION NVARCHAR2(50),
    SCHOOL NVARCHAR2(50),
    UNIVERSITY NVARCHAR2(50)
);