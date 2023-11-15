CREATE TABLE IF NOT EXISTS module_Data (

  module_name VARCHAR(100),
  module_data  CLOB
);

CREATE TABLE IF NOT EXISTS user_info (
  user_name VARCHAR(100),
  password  VARCHAR(100),
  full_name VARCHAR(100)
);