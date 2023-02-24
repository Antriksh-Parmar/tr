CREATE SCHEMA pf;

SET search_path TO pf;

CREATE TABLE pf.users(
    id VARCHAR(200) PRIMARY KEY,
    first_name VARCHAR(300),
    last_name VARCHAR(300),
    email VARCHAR(300),
    pswd_hash VARCHAR(1000)
);

CREATE TABLE pf.portfolios(
    id VARCHAR(200) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    user_id VARCHAR(200) NOT NULL REFERENCES pf.users(id),
    created_date DATE NOT NULL,
    updated_date DATE NOT NULL
);

CREATE TABLE pf.mutual_funds_investments(
    id VARCHAR(200) PRIMARY KEY,
    portfolio_id VARCHAR(200) NOT NULL REFERENCES pf.portfolios(id),
    name VARCHAR(200) NOT NULL,
    source VARCHAR(100) NOT NULL,
    investment_type VARCHAR(100) NOT NULL,
    sip_interval VARCHAR(100),
    sip_start_date DATE,
    sip_amount NUMERIC(15, 3),
    lumpsum_investment_date DATE,
    lumpsum_amount NUMERIC(20, 3),
    created_date DATE NOT NULL,
    updated_date DATE NOT NULL
);

CREATE TABLE pf.equity_investments(
    id VARCHAR(200) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    portfolio_id VARCHAR(200) NOT NULL REFERENCES pf.portfolios(id),
    source VARCHAR(100) NOT NULL,
    amount NUMERIC(20, 3),
    created_date DATE NOT NULL,
    updated_date DATE NOT NULL
);
