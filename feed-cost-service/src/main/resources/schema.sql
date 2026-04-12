CREATE TABLE IF NOT EXISTS feed_cost (
    id BIGSERIAL PRIMARY KEY,
    feed_type VARCHAR(50) NOT NULL UNIQUE,
    cost_per_kg BIGINT NOT NULL,
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('MILHO', 250) ON CONFLICT (feed_type) DO NOTHING;
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('SOJA', 380) ON CONFLICT (feed_type) DO NOTHING;
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('FARELO_SOJA', 420) ON CONFLICT (feed_type) DO NOTHING;
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('SORGO', 210) ON CONFLICT (feed_type) DO NOTHING;
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('TRIGO', 290) ON CONFLICT (feed_type) DO NOTHING;
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('SUPLEMENTO_MINERAL', 550) ON CONFLICT (feed_type) DO NOTHING;
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('NUCLEO_PROTEICO', 675) ON CONFLICT (feed_type) DO NOTHING;
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('SAL_BRANCO', 120) ON CONFLICT (feed_type) DO NOTHING;
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('UREIA', 330) ON CONFLICT (feed_type) DO NOTHING;
INSERT INTO feed_cost (feed_type, cost_per_kg) VALUES ('SILAGEM_MILHO', 180) ON CONFLICT (feed_type) DO NOTHING;