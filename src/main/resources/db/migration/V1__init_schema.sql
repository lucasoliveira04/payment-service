create table if not exists payment_timeout_log (
                                                   id            bigserial primary key,
                                                   executed_at   timestamptz not null default now(),
    affected_rows integer     not null,
    source        text        not null
    );

create or replace function cancel_expired_payments()
returns void as $$
declare
row_updated integer;
begin
update payments
set status = 'cancelled'
where status = 'pending'
  and created_at < now() - interval '10 seconds';

get diagnostics row_updated = row_count;

raise notice 'rows updated: %', row_updated;

    if row_updated > 0 then
        insert into payment_timeout_log (affected_rows, source)
        values (row_updated, 'cancel_expired_payments');
end if;
end;
$$ language plpgsql;