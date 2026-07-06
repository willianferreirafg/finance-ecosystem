CREATE TABLE tb_categories (
                               id UUID NOT NULL,
                               name VARCHAR(255) NOT NULL,
                               icon VARCHAR(255),
                               color VARCHAR(50),
                               user_id UUID NOT NULL,
                               CONSTRAINT pk_tb_categories PRIMARY KEY (id),
                               CONSTRAINT fk_categories_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE tb_transactions (
                                 id UUID NOT NULL,
                                 description VARCHAR(255) NOT NULL,
                                 amount NUMERIC(19, 2) NOT NULL,
                                 date DATE NOT NULL,
                                 type VARCHAR(50) NOT NULL,
                                 paid BOOLEAN NOT NULL,
                                 user_id UUID NOT NULL,
                                 category_id UUID NOT NULL,
                                 CONSTRAINT pk_tb_transactions PRIMARY KEY (id),
                                 CONSTRAINT fk_transactions_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                                 CONSTRAINT fk_transactions_category FOREIGN KEY (category_id) REFERENCES tb_categories (id) ON DELETE RESTRICT
);

CREATE INDEX idx_categories_user ON tb_categories(user_id);
CREATE INDEX idx_transactions_user ON tb_transactions(user_id);