CREATE SCHEMA fi;

SET search_path TO fi;

CREATE TABLE fi.mutual_funds(
    id VARCHAR(200) PRIMARY KEY,
    isin VARCHAR(100) UNIQUE NOT NULL,
    sb_code INT NOT NULL,
    rta_code VARCHAR(20),
    amc_code VARCHAR(5),
    fund_name VARCHAR(200),
    previous_name VARCHAR(200),
    fund_manager VARCHAR(500),
    inception_date DATE,
    div_frequency VARCHAR(100),
    sebi_category_name VARCHAR(100),
    amc_name VARCHAR(200),
    sub_asset_class_code VARCHAR(200),
    benchmark VARCHAR(200),
    offerings2 VARCHAR(100),
    offerings1 VARCHAR(100),
    objective VARCHAR(10000),
    downside_protection_measure INT,
    risk_level VARCHAR(50),
    asset_class VARCHAR(100),
    aum NUMERIC(20, 5),
    expense_ratio NUMERIC(5, 3),
    inception_nav NUMERIC(15,7),
    lump_sum_order_allowed BOOLEAN,
    etf BOOLEAN,
    redemption_order_allowed BOOLEAN,
    sip_order_allowed BOOLEAN,
    switch_order_allowed BOOLEAN,
    swp_order_allowed BOOLEAN,
    stp_order_allowed BOOLEAN,
    fund_active BOOLEAN,
    us_canada_supported BOOLEAN,
    div_payout BOOLEAN,
    div_reinv BOOLEAN,
    regular BOOLEAN,
    growth BOOLEAN  
);

CREATE TABLE fi.mutual_fund_navs(
    id BIGSERIAL PRIMARY KEY,
    isin VARCHAR(100) REFERENCES fi.mutual_funds(isin),
    nav NUMERIC(10, 3) NOT NULL,
    adjusted_nav NUMERIC(100, 3),
    nav_date DATE
);

CREATE TABLE fi.mutual_fund_exit_loads (
    id BIGSERIAL PRIMARY KEY,
    isin VARCHAR(100) REFERENCES fi.mutual_funds(isin),
    till_days INT,
    exit_load NUMERIC(5, 3)
);

CREATE TABLE fi.mutual_fund_asset_classes(
    id BIGSERIAL PRIMARY KEY,
    isin VARCHAR(100) REFERENCES fi.mutual_funds(isin),
    asset_class VARCHAR(100),
    allocation_pc NUMERIC(5, 2)
);

CREATE TABLE fi.mutual_fund_holdings(
    id BIGSERIAL PRIMARY KEY,
    isin VARCHAR(100) REFERENCES fi.mutual_funds(isin),
    name VARCHAR(200),
    weight NUMERIC(5, 2),
    holding_type VARCHAR(5)
);

CREATE TABLE fi.mutual_fund_sectors(
    id BIGSERIAL PRIMARY KEY,
    isin VARCHAR(100) REFERENCES fi.mutual_funds(isin),
    name VARCHAR(200),
    weight NUMERIC(5, 2),
    amount NUMERIC(20, 5)
);