insert into policy(name,code,status,created_at) values ('OAuthTokenPolicy','oauth2_policy','ACTIVE',now());
insert into policy(name,code,status,created_at) values ('APIKEYPolicy','APIKEYPolicy','ACTIVE',now());
insert into policy(name,code,status,created_at) values ('TPINAuthorizePolicy','tpin_policy','ACTIVE',now());


Insert into clients (ID,NAME,SECRET,ROLES,ACCESS_TOKEN_EXPIRED,REFRESH_TOKEN_EXPIRED,CHANNEL,SCOPES,GRANT_TYPE,STATUS,CREATED_AT,MODIFIED_AT,CREATED_BY,MODIFIED_BY,IS_ENABLED_HASH)
values ('12345','demo client','mCN8pPfNw3mj/2ZVBK8zXkKVvxm8VsHkoHA3Z6SDXgM=','mb_client',600000,600000,'INTERNAL','user-profile,payment-service','client_credentials,password,refresh_token','ACTIVE', now(), null,null,null,null);

Insert into CLIENT_POLICY (CLIENT_ID,POLICY_CONFIG,POLICY_CODE,STATUS,CREATED_AT,MODIFIED_AT,CREATED_BY,MODIFIED_BY)
values ('12345','N/A','oauth2_policy','ACTIVE', now(),null,null,null),
       ('12345','{"paths":["/payment-service/api/v1.0/payments/confirm","/payment-service/api/v1.0/gift-transfer/create"],"requiredLength":"10"}','tpin_policy','ACTIVE', now(),null,null,null);


Insert into CLIENT_APIS (CLIENT_ID,API_KEY,STATUS,CREATED_AT,MODIFIED_AT,CREATED_BY,MODIFIED_BY) values ('12345','12345_api_key','ACTIVE', now(),null,null,null);