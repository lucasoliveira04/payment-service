package org.payments.payment_api.repository;

import org.payments.payment_api.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);

    long countByIdempotencyKey(String idempotencyKey);

    @Modifying
    @Query("""
        UPDATE Payment p
        SET p.status = 'CANCELLED'
        WHERE p.status = 'PENDING'
          AND p.createdAt < :limit
    """)
    void cancelExpired(OffsetDateTime limit);

}
