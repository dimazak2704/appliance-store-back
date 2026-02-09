INSERT INTO orders (client_id, contact_phone, delivery_address, delivery_cost, delivery_type, goods_total_amount, status, total_amount, created_at)
VALUES (1, '+380991111111', 'Київ, вул. Хрещатик 1', 0.00, 'SELF_PICKUP', 15000.00, 'COMPLETED', 15000.00, DATEADD('DAY', -10, CURRENT_DATE));
INSERT INTO order_row (amount, number, appliance_id, order_id) VALUES (15000.00, 1, 1, 1);


INSERT INTO orders (client_id, contact_phone, delivery_address, delivery_cost, delivery_type, goods_total_amount, status, total_amount, created_at)
VALUES (2, '+380992222222', 'Львів, відділення 5', 100.00, 'POST', 2500.00, 'NEW', 2600.00, CURRENT_DATE);
INSERT INTO order_row (amount, number, appliance_id, order_id) VALUES (2500.00, 1, 2, 2);


INSERT INTO orders (client_id, contact_phone, delivery_address, delivery_cost, delivery_type, goods_total_amount, status, total_amount, created_at)
VALUES (3, '+380993333333', 'Одеса, вул. Дерибасівська 10', 200.00, 'COURIER', 45000.00, 'CONFIRMED', 45200.00, DATEADD('HOUR', -5, CURRENT_DATE));
INSERT INTO order_row (amount, number, appliance_id, order_id) VALUES (40000.00, 1, 3, 3);
INSERT INTO order_row (amount, number, appliance_id, order_id) VALUES (5000.00, 2, 2, 3);


INSERT INTO orders (client_id, contact_phone, delivery_address, delivery_cost, delivery_type, goods_total_amount, status, total_amount, created_at)
VALUES (4, '+380994444444', 'Харків, площа Свободи', 100.00, 'POST', 1200.00, 'CANCELED', 1300.00, DATEADD('DAY', -2, CURRENT_DATE));
INSERT INTO order_row (amount, number, appliance_id, order_id) VALUES (1200.00, 1, 4, 4);


INSERT INTO orders (client_id, contact_phone, delivery_address, delivery_cost, delivery_type, goods_total_amount, status, total_amount, created_at)
VALUES (5, '+380995555555', 'Дніпро, пр. Яворницького', 100.00, 'POST', 8000.00, 'SHIPPED', 8100.00, DATEADD('DAY', -1, CURRENT_DATE));
INSERT INTO order_row (amount, number, appliance_id, order_id) VALUES (8000.00, 2, 5, 5);


INSERT INTO orders (client_id, contact_phone, delivery_address, delivery_cost, delivery_type, goods_total_amount, status, total_amount, created_at)
VALUES (6, '+380996666666', 'Вінниця, вул. Соборна', 0.00, 'COURIER', 60000.00, 'NEW', 60000.00, CURRENT_DATE);
INSERT INTO order_row (amount, number, appliance_id, order_id) VALUES (60000.00, 2, 3, 6);


INSERT INTO orders (client_id, contact_phone, delivery_address, delivery_cost, delivery_type, goods_total_amount, status, total_amount, created_at)
VALUES (1, '+380991111111', 'Київ, вул. Хрещатик 1', 200.00, 'COURIER', 3200.00, 'NEW', 3400.00, CURRENT_DATE);
INSERT INTO order_row (amount, number, appliance_id, order_id) VALUES (3200.00, 1, 4, 7);


INSERT INTO orders (client_id, contact_phone, delivery_address, delivery_cost, delivery_type, goods_total_amount, status, total_amount, created_at)
VALUES (7, '+380997777777', 'Полтава, центр', 100.00, 'POST', 1500.00, 'CONFIRMED', 1600.00, DATEADD('HOUR', -12, CURRENT_DATE));
INSERT INTO order_row (amount, number, appliance_id, order_id) VALUES (1500.00, 1, 2, 8);


INSERT INTO orders (client_id, contact_phone, delivery_address, delivery_cost, delivery_type, goods_total_amount, status, total_amount, created_at)
VALUES (8, '+380998888888', 'Чернігів', 100.00, 'POST', 9000.00, 'COMPLETED', 9100.00, DATEADD('MONTH', -1, CURRENT_DATE));
INSERT INTO order_row (amount, number, appliance_id, order_id) VALUES (9000.00, 1, 1, 9);


INSERT INTO orders (client_id, contact_phone, delivery_address, delivery_cost, delivery_type, goods_total_amount, status, total_amount, created_at)
VALUES (9, '+380999999999', 'Ужгород', 0.00, 'SELF_PICKUP', 500.00, 'CANCELED', 500.00, DATEADD('DAY', -5, CURRENT_DATE));
INSERT INTO order_row (amount, number, appliance_id, order_id) VALUES (500.00, 1, 5, 10);