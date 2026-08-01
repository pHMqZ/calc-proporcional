-- V1 Initial Schema (with UUID)
-- Note: This migration drops the old BIGINT PK schema and creates it with UUID.
-- As agreed in BD_POST-US-03, there is no real data in production that needs to be preserved at this point.
-- Therefore, we are safe to start fresh with a solid foundation.

CREATE TABLE IF NOT EXISTS people (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    salary NUMERIC(38, 2) NOT NULL CHECK (salary >= 0),
    reserve_percentage DOUBLE PRECISION NOT NULL CHECK (reserve_percentage >= 0 AND reserve_percentage <= 100),
    client_id VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS bills (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    description VARCHAR(255) NOT NULL,
    total_amount NUMERIC(38, 2) NOT NULL CHECK (total_amount >= 0),
    client_id VARCHAR(255) NOT NULL
);
