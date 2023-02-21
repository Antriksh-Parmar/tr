CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE mutual_funds(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    isin VARCHAR(100) NOT NULL,
    expense_ratio NUMERIC(4,2) NOT NULL,
    sip_available BOOLEAN,
    lump_available BOOLEAN,
    category VARCHAR(100) NOT NULL,
    lockin_period INT,
    detail_info_link VARCHAR(200),
    fund_type VARCHAR(20) NOT NULL,
    switch_allowed BOOLEAN,
    tax_period INT,
    fund_category VARCHAR(200) NOT NULL,
    plan VARCHAR(200),
    expense_ratio_date TIMESTAMP,
    crisil_rating VARCHAR(100),
    portfolio_turnover NUMERIC(15, 5),
    maturity_type VARCHAR(200),
    aum NUMERIC(20, 5) NOT NULL,
    fund_rating INT,
    rating_date TIMESTAMP
);

CREATE TABLE mf_names(
    id SERIAL PRIMARY KEY,
    isin VARCHAR(100),
    name VARCHAR(300) NOT NULL,
    short_name VARCHAR(200),
    amc VARCHAR(200) NOT NULL
);

CREATE TABLE mf_navs(
   id SERIAL PRIMARY KEY,
   mf_id uuid NOT NULL,
   schema_code INT,
   isin VARCHAR(100) NOT NULL,
   nav NUMERIC(15,5) NOT NULL,
   nav_date TIMESTAMP,
   CONSTRAINT fk_mf_id FOREIGN KEY (mf_id) REFERENCES mutual_funds(id)
);

CREATE INDEX idx_mf_navs_nav_date_isin ON mf_navs (nav_date, isin);

CREATE TABLE mf_exit_loads(
    id SERIAL PRIMARY KEY,
    mf_id uuid NOT NULL,
    isin VARCHAR(100) NOT NULL,
    days INT NOT NULL,
    exit_load NUMERIC(5,2),
    CONSTRAINT fk_mf_id FOREIGN KEY (mf_id) REFERENCES mutual_funds(id)
);

CREATE INDEX idx_mf_exit_loads_isin ON mf_exit_loads (isin);