INSERT INTO user_auth_details
(cif, mobile_number, password, is_enable_biometric, biometric_token, key_metric, remark, is_reactivate, blocked_by, profile_id, full_name, gender, dob, address, last_login_date, status, created_at)
VALUES
('000000001', '85512121212', 'zsA9oj7rpAMQEhJuEa2iKcYo7Q/wA/X8s9qExjVNrUw=', false, NULL, 'QLweMepLS8ugYDzh', null, true, null, null, 'Kok Dara', 'Male', '1992-07-30', '999 Pine St, FL', null, 'ACTIVE', now()),
('000000002', '85513131313', '1GmEs3E5l7A82Z4yifBp34JCJxgi7PU3bIvHA1VhAxc=', false, NULL, 'RjcJJzGw91K2ULSe', null, true, null, null, 'Sok Pisey', 'Female', '1998-07-30', '7777 Pine St, BD', null, 'ACTIVE', now()),
('000000003', '85510101010', 'P1vv+45A0YeOAQ7vEq/50IyXgpFFR4AfD4zCJVee1jU=', false, NULL, 'QhQbT5Js0lx0kPhD', null, true, null, null, 'Kuy Emm', 'Male', '1997-07-30', '333 Pine St, XX', null, 'ACTIVE', now());


INSERT INTO user_accounts (cif, account_no, currency, balance, account_name, is_hide, description, is_default, status, created_at)
VALUES 
('000000001', '000000001001', 'USD', 50000000.75, 'Saving Account', false, null, true, 'ACTIVE', now()),
('000000001', '000000001002', 'KHR', 4000000000, 'Saving Account', false, null, false, 'ACTIVE', now()),
('000000002', '000000002001', 'USD', 10000.75, 'Saving Account', false, null, false, 'ACTIVE', now()),
('000000002', '000000002002', 'KHR', 9000000000, 'Saving Account', false, null, true, 'ACTIVE', now()),
('000000003', '000000003001', 'USD', 2750.00, 'Saving Account', false, null, true, 'ACTIVE', now());