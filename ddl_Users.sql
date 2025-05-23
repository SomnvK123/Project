CREATE TABLE users
(
    id         INT AUTO_INCREMENT NOT NULL,
    created_at datetime           NOT NULL,
    updated_at datetime           NOT NULL,
    tel        VARCHAR(255)       NULL,
    password   VARCHAR(255)       NULL,
    name       VARCHAR(255)       NULL,
    address    VARCHAR(255)       NULL,
    status     BIT(1)             NOT NULL,
    is_deleted BIT(1)             NOT NULL,
    `role`     VARCHAR(255)       NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);