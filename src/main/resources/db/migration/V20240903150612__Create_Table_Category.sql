create table category (
    id uuid primary key,
    title varchar(200) not null,
    color varchar(50) not null
);

alter table category add constraint uk_category_title unique (title);