package ru.yumeno.nir.service;

import ru.yumeno.nir.entity.Subscription;

import java.util.List;

public interface SubscriptionService {
    List<Subscription> getAllSubscriptions();

    Subscription getSubscriptionById(int id);

    Subscription addSubscription(Subscription subscription);

    Subscription updateSubscription(Subscription subscription);

    void deleteSubscription(int id);
}
