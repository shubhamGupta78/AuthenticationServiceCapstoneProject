CREATE TABLE `role`
(
    id         INT AUTO_INCREMENT NOT NULL,
    created_at datetime NULL,
    updated_at datetime NULL,
    is_deleted BIT(1) NULL,
    `role`     VARCHAR(255) NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE session
(
    id             INT AUTO_INCREMENT NOT NULL,
    created_at     datetime NULL,
    updated_at     datetime NULL,
    is_deleted     BIT(1) NULL,
    token          VARCHAR(255) NULL,
    expiry_date    datetime NULL,
    user_id        INT NULL,
    session_status SMALLINT NULL,
    CONSTRAINT pk_session PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    id         INT AUTO_INCREMENT NOT NULL,
    created_at datetime NULL,
    updated_at datetime NULL,
    is_deleted BIT(1) NULL,
    role_id    INT NULL,
    user_id    INT NULL,
    CONSTRAINT pk_userrole PRIMARY KEY (id)
);

CREATE TABLE user_table
(
    id         INT AUTO_INCREMENT NOT NULL,
    created_at datetime NULL,
    updated_at datetime NULL,
    is_deleted BIT(1) NULL,
    username   VARCHAR(255) NULL,
    email      VARCHAR(255) NULL,
    password   VARCHAR(255) NULL,
    CONSTRAINT pk_user_table PRIMARY KEY (id)
);

ALTER TABLE session
    ADD CONSTRAINT FK_SESSION_ON_USER FOREIGN KEY (user_id) REFERENCES user_table (id);

ALTER TABLE user_role
    ADD CONSTRAINT FK_USERROLE_ON_ROLE FOREIGN KEY (role_id) REFERENCES `role` (id);

ALTER TABLE user_role
    ADD CONSTRAINT FK_USERROLE_ON_USER FOREIGN KEY (user_id) REFERENCES user_table (id);