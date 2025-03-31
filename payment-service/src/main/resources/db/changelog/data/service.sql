INSERT INTO services (id, service_id, name, description, currency, is_has_fee, txn_type, service_commands, channel, status, created_at)
VALUES
    (gen_random_uuid(), 'transfer_own', 'Transfer Own', 'Transfer Own Account', 'USD', FALSE, 'OWN', 'PAYMENT', 'INTERNAL', 'ACTIVE', NOW()),
    (gen_random_uuid(), 'transfer_own', 'Transfer Own', 'Transfer Own Account', 'KHR', FALSE, 'OWN', 'PAYMENT', 'INTERNAL', 'ACTIVE', NOW()),
    (gen_random_uuid(), 'transfer_within', 'Transfer Within', 'Transfer Other Account', 'USD', FALSE, 'WITHIN', 'PAYMENT,REFUND', 'INTERNAL', 'ACTIVE', NOW()),
    (gen_random_uuid(), 'transfer_within', 'Transfer Within', 'Transfer Other Account', 'KHR', FALSE, 'WITHIN', 'PAYMENT,REFUND', 'INTERNAL', 'ACTIVE', NOW()),
    (gen_random_uuid(), 'transfer_gift', 'Transfer Gift', 'Transfer Gift', 'USD', FALSE, 'GIFT', 'PAYMENT', 'INTERNAL', 'ACTIVE', NOW()),
    (gen_random_uuid(), 'transfer_gift', 'Transfer Gift', 'Transfer Gift', 'KHR', FALSE, 'GIFT', 'PAYMENT', 'INTERNAL', 'ACTIVE', NOW());