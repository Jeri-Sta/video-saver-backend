create table video (
    id uuid primary key,
    title varchar(200) not null,
    description text,
    url varchar(600)
);

alter table video add constraint uk_video_url unique (url);