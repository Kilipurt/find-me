CREATE TABLE RELATIONSHIP (
    ID NUMBER PRIMARY KEY,
    USER_FROM NUMBER REFERENCES USERS(ID) ON DELETE CASCADE,
    USER_TO NUMBER REFERENCES USERS(ID) ON DELETE CASCADE,
    STATUS NVARCHAR2(30),
    CONSTRAINT STATUS_CH CHECK(STATUS IN('FRIENDS', 'REQUEST_SENT')),
    CONSTRAINT USERS_CH CHECK(USER_FROM != USER_TO),
    CONSTRAINT USERS_UNIQ UNIQUE (USER_FROM, USER_TO)
);