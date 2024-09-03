alter table video add column category uuid;

alter table video add constraint fk_video_category_id foreign key (category) references category (id);