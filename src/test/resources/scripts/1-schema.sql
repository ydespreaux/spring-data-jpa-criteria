use db_library;

create table author (
  id bigint not null auto_increment,
  first_name varchar(255),
  last_name varchar(255),
  version integer,
  primary key (id)
)ENGINE=InnoDB
  DEFAULT CHARSET='utf8' COLLATE='utf8_general_ci';

create table book (
  id bigint not null auto_increment,
  description varchar(4000),
  style varchar(20),
  price double precision,
  publication datetime,
  title varchar(100) not null,
  version integer,
  author_id bigint,
  editor_id bigint,
  primary key (id)
)ENGINE=InnoDB
  DEFAULT CHARSET='utf8' COLLATE='utf8_general_ci';


create table editor (
  id bigint not null auto_increment,
  label varchar(255) not null,
  primary key (id)
) ENGINE=InnoDB
  DEFAULT CHARSET='utf8' COLLATE='utf8_general_ci';


alter table book add constraint UK_BOOK_TITLE unique (title);
alter table book add constraint FK_BOOK_AUTHOR_ID foreign key (author_id) references author (id);
alter table book add constraint FK_BOOK_EDITOR_ID foreign key (editor_id) references editor (id);

