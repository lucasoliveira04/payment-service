create table if not exists payment_timeout_log
(
    id            bigserial primary key,
    executed_at   timestamptz not null default now(),
    affected_rows integer     not null,
    source        text        not null
);