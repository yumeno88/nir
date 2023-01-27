package ru.yumeno.nir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yumeno.nir.entity.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
}
