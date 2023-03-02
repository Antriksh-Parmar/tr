CREATE SCHEMA fi;

SET search_path TO fi;

CREATE TABLE fi.mutual_funds(
    id VARCHAR(200) PRIMARY KEY,
    amfi_code VARCHAR(100),
    fund_of_funds BOOLEAN,
    category_name VARCHAR(300),
    amc_name VARCHAR(300),
    exchange_traded BOOLEAN,
    purchase_mode INT,
    index VARCHAR(300),
    index_new VARCHAR(300),
    benchmark VARCHAR(300),
    portfolio_turnover NUMERIC(6,3),
    fund_active BOOLEAN,
    fund_name VARCHAR(300),
    isin VARCHAR(200) UNIQUE,
    short_name VARCHAR(300),
    fund_manager VARCHAR(500),
    inception_date DATE,
    category VARCHAR(300),
    sub_category VARCHAR(300),
    plan VARCHAR(300),
    fund_type VARCHAR(300),
    lock_in INT,
    detail_info VARCHAR(300),
    objective VARCHAR(5000),
    risk_level VARCHAR(100),
    aum NUMERIC(20,3),
    expense_ratio NUMERIC(5,3),
    lump_sum_order_allowed BOOLEAN,
    redemption_allowed BOOLEAN,
    sip_order_allowed BOOLEAN,
    switch_order_allowed BOOLEAN,
    swp_order_allowed BOOLEAN,
    stp_order_allowed BOOLEAN,
    direct BOOLEAN,
    maturity_type BOOLEAN
);

CREATE TABLE  fi.key_mappings (
    id SERIAL PRIMARY KEY,
    isin VARCHAR(100) NOT NULL REFERENCES fi.mutual_funds(isin),
    ms_id VARCHAR(100),
    kv_id VARCHAR(100)
);

CREATE TABLE fi.mutual_fund_navs (
    id BIGSERIAL PRIMARY KEY,
    isin VARCHAR(100) REFERENCES fi.mutual_funds(isin),
    nav NUMERIC(10, 3) NOT NULL,
    nav_date DATE
);

CREATE TABLE fi.mutual_fund_exit_loads (
    id BIGSERIAL PRIMARY KEY,
    isin VARCHAR(100) REFERENCES fi.mutual_funds(isin),
    till_days INT,
    exit_load NUMERIC(5, 3)
);

CREATE TABLE fi.mutual_fund_holdings (
    id BIGSERIAL PRIMARY KEY,
    mf_isin VARCHAR(100) REFERENCES fi.mutual_funds(isin),
    name VARCHAR(200),
    holding_type VARCHAR(100),
    weight NUMERIC(10,3),
    number_of_shares NUMERIC(20, 3),
    market_value NUMERIC(20, 3),
    country VARCHAR(100),
    sector_code VARCHAR(200),
    asset_isin VARCHAR(100),
    cusip VARCHAR(100),
    super_sector VARCHAR(300),
    primary_sector VARCHAR(300),
    secondary_sector VARCHAR(300),
    first_bought_date DATE,
    coupon NUMERIC(10, 3)
);
