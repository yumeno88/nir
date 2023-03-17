delete
from subscription_tag;
delete
from subscription;
delete
from address;
delete
from district;
delete
from street;
delete
from tag;

insert into district (id, name)
values (4, 'district1'),
       (5, 'district2');

insert into street (id, name)
values (4, 'street1'),
       (5, 'street2');

insert into address (id, apartment, house, porch, district_id, street_id)
values (4, 'apart1', 'house1', 'porch1', 4, 5),
       (5, 'apart2', 'house2', 'porch2', 5, 4);

insert into tag (name)
values ('gas'),
       ('water');

insert into subscription (id, address_id, chat_id)
values (4, 4, 'chatId1'),
       (5, 5, 'chatId2');

insert into subscription_tag (subscription_id, tag_name)
values (4, 'gas'),
       (5, 'water');