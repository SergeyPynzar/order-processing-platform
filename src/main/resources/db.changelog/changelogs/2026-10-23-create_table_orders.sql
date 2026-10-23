--liquibase formatted sql

--changeset few:2026-10-23-create_table_orders
CREATE TABLE orders
(
    id       UUID           NOT NULL,
    name     VARCHAR(255)   NOT NULL,
    quantity INTEGER        NOT NULL,
    price    NUMERIC(19, 2) NOT NULL,
    status   VARCHAR(32),
    created  TIMESTAMP,
    CONSTRAINT pk_orders PRIMARY KEY (id),
    CONSTRAINT chk_orders_quantity_positive CHECK (quantity > 0),
    CONSTRAINT chk_orders_price_positive CHECK (price > 0)
);

--rollback DROP TABLE orders;

--changeset few:2026-10-23-orders-id-default dbms:postgresql
ALTER TABLE orders ALTER COLUMN id SET DEFAULT gen_random_uuid();

--rollback ALTER TABLE orders ALTER COLUMN id DROP DEFAULT;
