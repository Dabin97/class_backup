create table book(
	bno varchar2(10 byte) primary key,
	title varchar2(500 byte),
	writer varchar2(50 byte),
	publisher varchar2(50 byte),
	wdate date
)
Insert into SCOTT1.BOOK (BNO,TITLE,WRITER,PUBLISHER,WDATE) values ('A0001','C?–¸?–´','?™ê¸¸ë™','ê°œì¸ì¶œíŒ',to_date('21/02/01','RR/MM/DD'));
Insert into SCOTT1.BOOK (BNO,TITLE,WRITER,PUBLISHER,WDATE) values ('A0002','JAVA','?™ê¸¸ë™','ê°œì¸ì¶œíŒ',to_date('21/02/02','RR/MM/DD'));
