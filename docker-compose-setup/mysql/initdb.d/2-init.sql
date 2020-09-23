-- Create a demo DB
CREATE DATABASE IF NOT EXISTS demo DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_bin;

-- Create a keycloak DB
CREATE DATABASE IF NOT EXISTS keycloak DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_bin;

-- Provide a DEMO instance
CREATE USER 'demo'@'%' IDENTIFIED BY 'demo';
GRANT ALL PRIVILEGES ON demo.* TO 'demo'@'%' WITH GRANT OPTION;

-- Access to KEYCLOAK
CREATE USER 'keycloak'@'%' IDENTIFIED BY 'keycloak';
GRANT ALL PRIVILEGES ON keycloak.* TO 'keycloak'@'%' WITH GRANT OPTION;

-- Ensure ROOT can do everything
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;

-- Just to make sure everything is commited
COMMIT;