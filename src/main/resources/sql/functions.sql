create or replace function cancel_expired_payments()
    returns void as $$
declare
row_updated integer;
begin
update payments
set status = 'CANCELLED'
where status = 'PENDING'
  and created_at < now() - interval '10 seconds';

get diagnostics row_updated = row_count;

raise notice 'Rows updated: %', row_updated;

    if row_updated > 0 then
        insert into payment_timeout_log (affected_rows, source)
        values (row_updated, 'cancel_expired_payments');
end if;
end;
$$ language plpgsql;