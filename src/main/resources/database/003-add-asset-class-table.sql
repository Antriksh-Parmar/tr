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

CREATE TABLE fi.mutual_fund_sector_allocation (
    id SERIAL PRIMARY KEY,
    mf_id VARCHAR(300) REFERENCES fi.mutual_funds(id),
    portfolio_investment_category  VARCHAR(50),
    portfolio_type VARCHAR(200),
    portfolio_name VARCHAR(200),
    sector_name VARCHAR(200),
    allocation_value NUMERIC(15, 6)
);

CREATE TABLE fi.mutual_fund_performance_indicators (
    id SERIAL PRIMARY KEY,
    mf_id VARCHAR(300) REFERENCES fi.mutual_funds(id),
    asset_category VARCHAR(100),
    upside NUMERIC(10, 5),
    downside NUMERIC(10, 55),
    maxDrawDown NUMERIC(25, 15),
    peakDate DATE,
    valleyDate DATE,
    duration NUMERIC(10, 5)
);

CREATE TABLE fi.mutual_fund_risk_parameters (
    id SERIAL PRIMARY KEY,
    mf_id VARCHAR(300) REFERENCES fi.mutual_funds(id),
    asset_category VARCHAR(100),
    duration INT,
    alpha NUMERIC(10, 5),
    beta NUMERIC(10, 5),
    rSquared NUMERIC(10, 5),
    standardDeviation NUMERIC(10, 5),
    sharpeRatio NUMERIC(10, 5)
);

CREATE TABLE fi.indexes (
    id SERIAL PRIMARY KEY,
    index_key INT,
    name VARCHAR(400)
);