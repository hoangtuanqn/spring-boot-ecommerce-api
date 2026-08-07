ALTER TABLE products
    ADD images JSON DEFAULT (JSON_ARRAY())  AFTER quantity;