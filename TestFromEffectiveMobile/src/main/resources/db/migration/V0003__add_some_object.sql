insert into users(username, email, password, balance, is_active, is_delete)
VALUES ('user1', 'user1@mail.ru','$2a$10$L3zhBcPNSa/FyBgTRUgPDOKGea58RhpVz7RlAQTOpDBP5lHYnZK8e',1000, true, false);

insert into users(username, email, password, balance, is_active, is_delete)
VALUES ('user2', 'user2@mail.ru','$2a$10$wttosIL6eU8oqmx5Z9a45OsQh.EabB/NerXJVA7vTPItCBhnhBC0m',1000, true, false);

insert into user_role(role_id, role) VALUES (1, 'ADMIN');
insert into user_role(role_id, role) VALUES (2, 'USER');

insert into organisation(user_id, name, description, logo, is_active, is_delete) values (1, 'VictorOrg', 'NewViktorOrg', 'logo', true, false);
insert into organisation(user_id, name, description, logo, is_active, is_delete) values (2, 'UserOrg', 'NewUserOrg', 'logo', true, false);

insert into info_sale(sale, time) values (5, 1);
insert into info_sale(sale, time) values (10, 1);

insert into product(organisation_id, info_sale_id, product_name, description, coast, count, is_active, is_delete)
values (1, 1, 'book', 'newBook', 10, 10, true, false);
insert into product(organisation_id, info_sale_id, product_name, description, coast, count, is_active, is_delete)
values (1, 2, 'pan', 'newPan', 1, 5, true, false);
insert into product(organisation_id, info_sale_id, product_name, description, coast, count, is_active, is_delete)
values (2, 2, 'knife', 'newKnife', 5, 7, true, false);

insert into rating(product_id, user_id, rating) values (1, 2, 4);
insert into rating(product_id, user_id, rating) values (1, 1, 3);

insert into rating(product_id, user_id, rating) values (2, 2, 4);
insert into rating(product_id, user_id, rating) values (2, 1, 3);

insert into rating(product_id, user_id, rating) values (3, 2, 4);
insert into rating(product_id, user_id, rating) values (3, 1, 3);

insert into review(product_id, user_id, review) values (1, 2, 'cool');
insert into review(product_id, user_id, review) values (1, 1, 'bad');

insert into review(product_id, user_id, review) values (2, 2, 'cool');
insert into review(product_id, user_id, review) values (2, 1, 'bad');

insert into review(product_id, user_id, review) values (3, 2, 'cool');
insert into review(product_id, user_id, review) values (3, 1, 'bad');

insert into table_product(product_id, characteristic, descriptionсhar) values (1, 'characteristic1','descriptionChar1');
insert into table_product(product_id, characteristic, descriptionсhar) values (1, 'characteristic2','descriptionChar2');

insert into table_product(product_id, characteristic, descriptionсhar) values (2, 'characteristic3','descriptionChar3');

insert into table_product(product_id, characteristic, descriptionсhar) values (3, 'characteristic4','descriptionChar4');

insert into tag(product_id, tag) values (1,'interesting');
insert into tag(product_id, tag) values (1,'big');

insert into tag(product_id, tag) values (2,'fool');

insert into tag(product_id, tag) values (2,'bad');

insert into notification(user_id, header, date, text) values (1, 'header', '2023-03-20 00:00:00', 'text');

