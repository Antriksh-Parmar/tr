SET search_path TO fi;

ALTER TABLE fi.mutual_funds ALTER COLUMN maturity_type VARCHAR(100);

CREATE TABLE fi.mutual_fund_asset_classes (
    id SERIAL PRIMARY KEY,
    mf_id VARCHAR(300) REFERENCES fi.mutual_funds(id),
    isin VARCHAR(300),
    allocation_category VARCHAR(100),
    net_allocation NUMERIC(6,3),
    short_allocation NUMERIC(6,3),
    long_allocation NUMERIC(6,3),
    local_allocation BOOLEAN
);

CREATE TABLE fi.mutual_fund_sector_allocation (
    id SERIAL PRIMARY KEY,
    mf_id VARCHAR(300) REFERENCES fi.mutual_funds(id),
    portfolio_investment_category  VARCHAR(50),
    portfolio_type VARCHAR(200),
    portfolio_name VARCHAR(200),
    sector_name VARCHAR(200),
    allocation_value NUMERIC(15, 6)
);

