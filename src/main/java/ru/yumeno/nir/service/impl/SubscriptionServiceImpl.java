package ru.yumeno.nir.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yumeno.nir.entity.Subscription;
import ru.yumeno.nir.exception_handler.exceptions.ResourceNotFoundException;
import ru.yumeno.nir.repository.SubscriptionRepository;
import ru.yumeno.nir.service.SubscriptionService;

import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Subscription getSubscriptionById(int id) {
        Optional<Subscription> optional = subscriptionRepository.findById(id);
        return optional.
                orElseThrow(() -> new ResourceNotFoundException("Subscription not exist with id : " + id));
    }

    @Override
    public Subscription addSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription updateSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public void deleteSubscription(int id) {
        Optional<Subscription> optional = subscriptionRepository.findById(id);
        if (optional.isPresent()) {
            subscriptionRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Subscription not exist with id : " + id);
        }
    }
}
