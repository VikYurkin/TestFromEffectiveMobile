 insert into users(user_id, username, email, password, balance, is_active, is_delete)
 VALUES (1, 'user1', 'user1@mail.ru','',1000, true, false);

 insert into organisation(organisation_id, user_id, name, description, logo, is_active) values (1, 1, 'Org1', 'NewOrg1', 'logo1', true);
 insert into organisation(organisation_id, user_id, name, description, logo, is_active) values (2, 1, 'Org2', 'NewOrg2', 'logo2', false);




