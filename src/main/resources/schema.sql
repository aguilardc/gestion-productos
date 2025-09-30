DROP TABLE IF EXISTS products;

CREATE TABLE products (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          price DECIMAL(10, 2) NOT NULL,
                          stock INT NOT NULL,
                          CONSTRAINT chk_price CHECK (price > 0),
                          CONSTRAINT chk_stock CHECK (stock >= 0)
);