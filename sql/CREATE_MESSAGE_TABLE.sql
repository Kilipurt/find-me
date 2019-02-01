CREATE TABLE MESSAGE (
    ID NUMBER PRIMARY KEY,
    TEXT NVARCHAR2(1000),
    DATE_SENT DATE,
    DATE_READ DATE,
    USER_FROM NUMBER REFERENCES USERS(ID) ON DELETE SET NULL,
    USER_TO NUMBER REFERENCES USERS(ID) ON DELETE SET NULL,
    CONSTRAINT DATE_READ_CH CHECK(DATE_SENT <= DATE_READ),
    CONSTRAINT UNIQ_USER_CH CHECK(USER_FROM != USER_TO)
);