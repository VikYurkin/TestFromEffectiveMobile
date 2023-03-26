 insert into users(user_id, username, email, password, balance, is_active, is_delete)
 VALUES (1, 'user1', 'user1@mail.ru','',1000, true, false);

 insert into organisation(organisation_id, user_id, name, description, logo) values (1, 1, 'Org', 'NewOrg', 'logo');

 insert into product(product_id, organisation_id, info_sale_id, product_name, description, coast, count, is_active, is_delete)
 values (1, 1, null, 'book', 'newBook', 10, 10, true, false);

 insert into table_product(product_id, characteristic, descriptionñhar) values (1, 'characteristic1','descriptionChar1');
 insert into table_product(product_id, characteristic, descriptionñhar) values (1, 'characteristic2','descriptionChar2');




