-- === Insert sample restaurants ===
INSERT INTO restaurant (id, name, location, cuisine, description, address, phone, thumbnail_url, opening_hours, has_early_bird, has_last_minute)
VALUES
    (1, 'Bella Italia', 'Cork', 'Italian', 'Authentic Italian cuisine in the heart of Cork', '15 Oliver Plunkett Street, Cork', '+353 21 345 6789', 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800&auto=format&fit=crop', 'Daily: 12:00-22:30', TRUE, TRUE),
    (2, 'The Sizzling Grill', 'Dublin', 'Steakhouse', 'Premium steaks and grilled specials', '12 Castle Road, Dublin', '+353 1 234 5678', 'https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&auto=format&fit=crop', 'Mon-Sun: 12:00-22:00', TRUE, FALSE);

-- === Insert customers ===
INSERT INTO customer (customer_name, customer_email, phone, password_hash)
VALUES
    ('John Smith', 'john@example.com', '+353 87 123 4567', 'hashedpassword1'),
    ('Jane Doe', 'jane@example.com', '+353 87 987 6543', 'hashedpassword2');

-- === Insert offers ===
INSERT INTO offer (id, restaurant_id, type, title, description, available_from, available_to, discount)
VALUES
    (1, 1, 'EARLY_BIRD', 'Early Dinner Special', '3 courses for €25', '17:00', '19:00', NULL),
    (2, 1, 'LAST_MINUTE', 'Late Night Deal', '20% off after 20:30', '20:30', '22:00', '20%');

-- === Insert availability slots ===
INSERT INTO availability_slot (id, restaurant_id, date, time, type, offer_id, is_available)
VALUES
    (1, 1, '2025-05-30', '17:30', 'EARLY_BIRD', 1, TRUE),
    (2, 1, '2025-05-30', '18:00', 'EARLY_BIRD', 1, TRUE),
    (3, 1, '2025-05-30', '19:30', 'REGULAR', NULL, TRUE),
    (4, 1, '2025-05-30', '20:30', 'LAST_MINUTE', 2, TRUE);

-- === Insert a sample booking ===
INSERT INTO booking (id, date, time, party_size, status, confirmation_code, restaurant_id, customer_id, available_slot_id)
VALUES
    (1, '2025-05-30', '17:30', 4, 'CONFIRMED', 'DINE12345', 1, 1, 1);