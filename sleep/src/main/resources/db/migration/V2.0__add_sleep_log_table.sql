CREATE TABLE sleep_log
(
    id                SERIAL PRIMARY KEY,
    user_id           INT         NOT NULL,
    date              DATE        NOT NULL,
    time_in_bed_start TIME        NOT NULL,
    time_in_bed_end   TIME        NOT NULL,
    total_time_in_bed BIGINT      NOT NULL,
    morning_feeling   VARCHAR(10) NOT NULL,
    CONSTRAINT unique_user_date UNIQUE (user_id, date) -- Unique constraint
);
