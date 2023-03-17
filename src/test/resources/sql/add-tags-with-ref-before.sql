delete
from news_tag;
delete
from news;
delete
from tag;

insert into tag (name)
values ('gas'),
       ('water');

insert into news (id, body, create_date, header, image_url)
values (4, 'body', '2023-03-14 21:24:54.635517', 'header', 'url'),
       (5, 'body2', '2023-03-14 21:25:36.546418', 'header2', 'url2');

insert into news_tag (news_id, tag_name)
values (4, 'water'),
       (5, 'gas');