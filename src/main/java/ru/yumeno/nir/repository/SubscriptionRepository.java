package ru.yumeno.nir.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yumeno.nir.entity.Address;
import ru.yumeno.nir.entity.Subscription;
import ru.yumeno.nir.entity.Tag;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    List<Subscription> findAllByTags(Tag tag);

    List<Subscription> findAllByAddress(Address address);

    Optional<Subscription> findByChatId(String chatId);
}
