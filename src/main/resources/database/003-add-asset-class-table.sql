SET search_path TO fi;

ALTER TABLE fi.mutual_funds ALTER COLUMN maturity_type VARCHAR(100);

CREATE TABLE fi.mutual_funds_asset_classes (
    id SERIAL PRIMARY KEY,
    mf_id VARCHAR(300) REFERENCES fi.mutual_funds(id),
    isin VARCHAR(300),
    allocation_category VARCHAR(100),
    net_allocation NUMERIC(6,3),
    short_allocation NUMERIC(6,3),
    long_allocation NUMERIC(6,3),
    local_allocation BOOLEAN
);