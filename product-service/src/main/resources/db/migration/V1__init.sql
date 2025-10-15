CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    bank_id BIGINT NOT NULL,
    type VARCHAR(120) NOT NULL,
    name VARCHAR(255) NOT NULL,
    rate NUMERIC(10,4) NOT NULL,
    term_months INTEGER NOT NULL,
    currency VARCHAR(10) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_products_type ON products (type);
CREATE INDEX IF NOT EXISTS idx_products_bank_id ON products (bank_id);
