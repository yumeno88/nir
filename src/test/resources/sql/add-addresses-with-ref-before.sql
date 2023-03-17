delete
from news_address;
delete
from news;
delete
from address;
delete
from district;
delete
from street;

insert into district (id, name)
values (4, 'district1'),
       (5, 'district2');

insert into street (id, name)
values (4, 'street1'),
       (5, 'street2');

insert into address (id, apartment, house, porch, district_id, street_id)
values (4, 'apart1', 'house1', 'porch1', 4, 5),
       (5, 'apart2', 'house2', 'porch2', 5, 4);

insert into news (id, body, create_date, header, image_url)
values (4, 'body', '2023-03-14 21:24:54.635517', 'header', 'url'),
       (5, 'body2', '2023-03-14 21:25:36.546418', 'header2', 'url2');

insert into news_address (news_id, address_id)
values (4, 4),
       (5, 5);