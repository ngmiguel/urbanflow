CREATE TABLE notifications (
    id               UUID PRIMARY KEY,
    user_id          VARCHAR(100) NOT NULL,
    type             VARCHAR(50)  NOT NULL,
    status           VARCHAR(50)  NOT NULL,
    title            VARCHAR(255) NOT NULL,
    message          VARCHAR(1000) NOT NULL,
    severity         VARCHAR(50)  NOT NULL,
    zone_id          VARCHAR(100) NOT NULL,
    source_event_id  VARCHAR(100),
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL,
    read_at          TIMESTAMP WITH TIME ZONE
);

CREATE TABLE subscriptions (
    id          UUID PRIMARY KEY,
    user_id     VARCHAR(100) NOT NULL,
    zone_id     VARCHAR(100),
    enabled     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_notifications_user_id ON notifications (user_id);
CREATE INDEX idx_notifications_created_at ON notifications (created_at DESC);
CREATE INDEX idx_subscriptions_user_id ON subscriptions (user_id);
CREATE INDEX idx_subscriptions_zone_id ON subscriptions (zone_id);
