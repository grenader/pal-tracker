CREATE TABLE time_entries (
  id         BIGSERIAL,
  project_id BIGINT,
  user_id    BIGINT,
  date       DATE,
  hours      INT,
  PRIMARY KEY (id)
);

GRANT ALL PRIVILEGES ON TABLE time_entries TO tracker;
GRANT USAGE, SELECT ON SEQUENCE time_entries_id_seq TO tracker;



# GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO tracker;


