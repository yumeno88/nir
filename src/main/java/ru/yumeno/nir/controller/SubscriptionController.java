package ru.yumeno.nir.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yumeno.nir.dto.SubscriptionDTO;
import ru.yumeno.nir.entity.Subscription;
import ru.yumeno.nir.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping(value = "/subscriptions")
@Api
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping(value = "")
    @ApiOperation("Получение всех подписок")
    public List<SubscriptionDTO> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions().stream().map(this::toSubscriptionDTO).toList();
    }

    @GetMapping(value = "/{id}")
    @ApiOperation("Получение подписки по ее id")
    public SubscriptionDTO getSubscriptionById(@PathVariable int id) {
        return toSubscriptionDTO(subscriptionService.getSubscriptionById(id));
    }

    @PostMapping(value = "")
    @ApiOperation("Добавлние подписки")
    public SubscriptionDTO addSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        return toSubscriptionDTO(subscriptionService.addSubscription(toSubscription(subscriptionDTO)));
    }

    @PutMapping(value = "")
    @ApiOperation("Обновление подписки")
    public SubscriptionDTO updateSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        return toSubscriptionDTO(subscriptionService.updateSubscription(toSubscription(subscriptionDTO)));
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation("Удаление подписки")
    public void deleteSubscription(@PathVariable int id) {
        subscriptionService.deleteSubscription(id);
    }

    private Subscription toSubscription(SubscriptionDTO subscriptionDTO) {
        return Subscription.builder()
                .id(subscriptionDTO.getId())
                .address(subscriptionDTO.getAddress())
                .tags(subscriptionDTO.getTags())
                .email(subscriptionDTO.getEmail())
                .phoneNumber(subscriptionDTO.getPhoneNumber())
                .build();
    }

    private SubscriptionDTO toSubscriptionDTO(Subscription subscription) {
        return SubscriptionDTO.builder()
                .id(subscription.getId())
                .address(subscription.getAddress())
                .tags(subscription.getTags())
                .email(subscription.getEmail())
                .phoneNumber(subscription.getPhoneNumber())
                .build();
    }
}
