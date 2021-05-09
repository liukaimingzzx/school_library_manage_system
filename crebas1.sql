/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2020/7/2 15:31:20                            */
/*==============================================================*/


drop table if exists Admin;

drop table if exists Book;

drop table if exists Penalty;

drop table if exists Record;

drop table if exists User;

/*==============================================================*/
/* Table: Admin                                                 */
/*==============================================================*/
create table Admin
(
   admin_email          varchar(30) not null,
   admin_name           varchar(20),
   admin_password       varchar(20),
   primary key (admin_email)
);

/*==============================================================*/
/* Table: Book                                                  */
/*==============================================================*/
create table Book
(
   book_id              varchar(13) not null,
   book_index           varchar(20),
   book_classify        varchar(20),
   book_name            varchar(30),
   book_author          varchar(20),
   book_press           varchar(20),
   book_introduction    text,
   book_restnum         int,
   book_totalnum        int,
   primary key (book_id)
);

/*==============================================================*/
/* Table: Penalty                                               */
/*==============================================================*/
create table Penalty
(
   penalty_id           int not null,
   record_id            int,
   email                varchar(30),
   penalty_bill         float(20),
   penalty_state        int,
   primary key (penalty_id)
);

/*==============================================================*/
/* Table: Record                                                */
/*==============================================================*/
create table Record
(
   record_id            int not null,
   email                varchar(30),
   book_id              varchar(13),
   borrow_time          datetime,
   return_time          datetime,
   record_state         int,
   fine_state           int,
   primary key (record_id)
);

/*==============================================================*/
/* Table: User                                                  */
/*==============================================================*/
create table User
(
   email                varchar(30) not null,
   password             varchar(20),
   current_num          int,
   max_num              int,
   user_name            varchar(10),
   gender               varchar(4),
   tel                  varchar(20),
   qq                   varchar(20),
   introduction         text,
   primary key (email)
);

alter table Penalty add constraint FK_create foreign key (record_id)
      references Record (record_id) on delete restrict on update restrict;

alter table Penalty add constraint FK_generate foreign key (email)
      references User (email) on delete restrict on update restrict;

alter table Record add constraint FK_have foreign key (book_id)
      references Book (book_id) on delete restrict on update restrict;

alter table Record add constraint FK_make foreign key (email)
      references User (email) on delete restrict on update restrict;

