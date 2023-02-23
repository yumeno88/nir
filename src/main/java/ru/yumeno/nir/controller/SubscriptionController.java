package ru.yumeno.nir.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.entity.Subscription;
import ru.yumeno.nir.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
@Api
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    @ApiOperation("Получение всех подписок")
    public List<Subscription> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions();
    }

    @GetMapping("/{id}")
    @ApiOperation("Получение подписки по ее id")
    public Subscription getSubscriptionById(@PathVariable int id) {
        return subscriptionService.getSubscriptionById(id);
    }

    @PostMapping
    @ApiOperation("Добавлние подписки")
    public Subscription addSubscription(@RequestBody Subscription subscription) {
        return subscriptionService.addSubscription(subscription);
    }

    @PutMapping
    @ApiOperation("Обновление подписки")
    public Subscription updateSubscription(@RequestBody Subscription subscription) {
        return subscriptionService.updateSubscription(subscription);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Удаление подписки")
    public void addSubscription(@PathVariable int id) {
        subscriptionService.deleteSubscription(id);
    }
}
